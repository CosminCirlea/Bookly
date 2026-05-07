package org.evolutionsoftware.bookly.core

import android.content.Context

object CoreContext {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}
