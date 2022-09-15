package org.succlz123.lib.screen.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberScreenWindowSizeOwner(): ScreenWindowSizeOwner {
    return remember {
        object : ScreenWindowSizeOwner {
            private val holder = ScreenWindowSizeHolder()

            override fun getWindowHolder(): ScreenWindowSizeHolder {
                return holder
            }
        }
    }
}