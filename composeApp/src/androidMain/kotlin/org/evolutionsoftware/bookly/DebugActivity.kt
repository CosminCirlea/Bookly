package org.evolutionsoftware.bookly

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.evolutionsoftware.bookly.core.CoreContext

class DebugActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLocaleController.localizedContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppLocaleController.attach(this)
        CoreContext.init(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(startWithDebugMenu = true)
        }
    }
}
