package org.evolutionsoftware.bookly

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.evolutionsoftware.bookly.core.CoreContext

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLocaleController.localizedContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppLocaleController.attach(this)
        CoreContext.init(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}