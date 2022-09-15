package org.succlz123.lib.screen.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberScreenLifecycleOwner(): ScreenLifecycleOwner {
    return remember {
        object : ScreenLifecycleOwner {
            var curLifecycle = DefaultScreenLifecycle()

            override fun getLifecycle(): ScreenLifecycle {
                return curLifecycle
            }
        }
    }
}