package org.evolutionsoftware.bookly.core.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.HttpHeaders
import org.evolutionsoftware.bookly.core.CoreContext

internal actual fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(OkHttp) {
        engine {
            addInterceptor(
                ChuckerInterceptor
                    .Builder(CoreContext.appContext)
                    .redactHeaders(
                        HttpHeaders.Authorization,
                        HttpHeaders.Cookie,
                        HttpHeaders.SetCookie,
                    ).build(),
            )
        }
        config()
    }
