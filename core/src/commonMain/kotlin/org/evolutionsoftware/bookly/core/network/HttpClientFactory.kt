package org.evolutionsoftware.bookly.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.evolutionsoftware.bookly.core.logging.Logger

class HttpClientFactory(
    private val config: NetworkConfig,
    private val tokenStore: AuthTokenStore,
    private val tokenRefresher: TokenRefresher = NoopTokenRefresher,
) {
    private val clientLogger = Logger.withTag("HttpClient")

    fun create(): HttpClient =
        platformHttpClient {
            expectSuccess = false

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }

            install(DefaultRequest) {
                if (config.baseUrl.isNotBlank()) {
                    url(config.baseUrl)
                }
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = config.requestTimeoutMillis
                connectTimeoutMillis = config.connectTimeoutMillis
                socketTimeoutMillis = config.socketTimeoutMillis
            }

            install(Logging) {
                level = if (config.enableLogging) LogLevel.BODY else LogLevel.NONE
                logger =
                    object : KtorLogger {
                        override fun log(message: String) {
                            if (!config.enableLogging) return
                            clientLogger.d(sanitizeLogMessage(message))
                        }
                    }
                sanitizeHeader { header ->
                    header.equals(HttpHeaders.Authorization, ignoreCase = true)
                }
            }

            install(HttpCallValidator) {
                validateResponse { response ->
                    if (!config.enableLogging) return@validateResponse
                    if (!response.status.isSuccess()) {
                        val request = response.call.request
                        clientLogger.d(
                            "HTTP ${response.status.value} ${response.status.description} " +
                                "${request.method.value} ${request.url}",
                        )
                    }
                }
                handleResponseExceptionWithRequest { cause, request ->
                    if (!config.enableLogging) return@handleResponseExceptionWithRequest
                    clientLogger.e("HTTP request failed: ${request.method.value} ${request.url}", cause)
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        tokenStore.read()?.toBearerTokens()
                    }
                    refreshTokens {
                        val refreshed = tokenRefresher.refresh(tokenStore.read())
                        if (refreshed != null) {
                            tokenStore.write(refreshed)
                        }
                        refreshed?.toBearerTokens()
                    }
                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        !isAuthFreePath(path)
                    }
                }
            }
        }

    private fun AuthToken.toBearerTokens(): BearerTokens =
        BearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken ?: accessToken,
        )

    private fun sanitizeLogMessage(message: String): String =
        message
            .lineSequence()
            .joinToString("\n") { line ->
                val index = line.indexOf("Authorization:", ignoreCase = true)
                if (index == -1) line
                else line.substring(0, index) + "Authorization: **REDACTED**"
            }

    private fun isAuthFreePath(path: String): Boolean {
        val normalized = if (path.startsWith("/")) path else "/$path"
        return normalized in AUTH_FREE_PATHS
    }

    private companion object {
        val AUTH_FREE_PATHS =
            setOf(
                "/api/auth/login",
                "/api/auth/signup",
                "/api/auth/refresh",
            )
    }
}
