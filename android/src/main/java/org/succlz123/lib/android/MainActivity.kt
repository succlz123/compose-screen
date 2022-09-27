package org.succlz123.lib.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import org.succlz123.app.screen.ui.main.DemoMainContent
import org.succlz123.lib.screen.ScreenContainer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenContainer(this, this, this, this) {
                DemoMainContent()
            }
        }
    }
}

