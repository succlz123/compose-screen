package org.succlz123.lib.screen.back

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberOnBackPressedDispatcherOwner(): ScreenOnBackPressedDispatcherOwner {
    return remember {
        object : ScreenOnBackPressedDispatcherOwner {
            private val screenOnBackPressedDispatcher = ScreenOnBackPressedDispatcher {}

            override fun get(): ScreenOnBackPressedDispatcher {
                return screenOnBackPressedDispatcher
            }

            override fun sendBackPressedToSystem() {
            }
        }
    }
}
