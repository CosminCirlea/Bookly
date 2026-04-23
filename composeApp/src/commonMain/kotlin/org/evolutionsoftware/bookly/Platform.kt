package org.evolutionsoftware.bookly

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform