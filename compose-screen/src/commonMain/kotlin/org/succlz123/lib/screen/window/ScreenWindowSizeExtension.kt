package org.succlz123.lib.screen.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

/**
 * Partitions a [Dp] into an enumerated [ScreenWindowSizeClass] class.
 */
fun getWindowSizeClass(windowWidthDpSize: Dp): ScreenWindowSizeClass = when {
    windowWidthDpSize < 0.dp -> {
        throw IllegalArgumentException("Dp value cannot be negative")
    }
    windowWidthDpSize < 600.dp -> {
        ScreenWindowSizeClass.Compact
    }
    windowWidthDpSize < 840.dp -> {
        ScreenWindowSizeClass.Medium
    }
    else -> {
        ScreenWindowSizeClass.Expanded
    }
}