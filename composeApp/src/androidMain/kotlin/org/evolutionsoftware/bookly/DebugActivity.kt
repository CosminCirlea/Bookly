package org.evolutionsoftware.bookly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.evolutionsoftware.bookly.core.CoreContext

class DebugActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        CoreContext.init(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(startWithDebugMenu = true)
        }
    }
}
