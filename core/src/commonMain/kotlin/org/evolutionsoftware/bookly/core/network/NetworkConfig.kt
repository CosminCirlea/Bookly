package org.evolutionsoftware.bookly.core.network

const val BASE_URL = "https://bookly-backend-batrinutsilvius-projects.vercel.app/"

data class NetworkConfig(
    val baseUrl: String = BASE_URL,
    val enableLogging: Boolean = true,
    val requestTimeoutMillis: Long = 15_000,
    val connectTimeoutMillis: Long = 15_000,
    val socketTimeoutMillis: Long = 30_000,
)
