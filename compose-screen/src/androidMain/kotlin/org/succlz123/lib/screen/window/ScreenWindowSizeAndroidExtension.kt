package org.succlz123.lib.screen.window

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.window.layout.WindowMetricsCalculator

@Composable
fun Activity.rememberWindowSize(): Size {
    val configuration = LocalConfiguration.current
    // WindowMetricsCalculator implicitly depends on the configuration
    // through the activity, so re-calculate it upon changes.
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    return windowMetrics.bounds.toComposeRect().size
}

/**
 * Remembers the [ScreenWindowSizeClass] class for the window corresponding to the current window metrics.
 */
@Composable
fun Activity.rememberWindowSizeClass(): ScreenWindowSizeClass {
    // Get the size (in pixels) of the window
    val windowSize = rememberWindowSize()
    // Convert the window size to [Dp]
    val windowDpSize = with(LocalDensity.current) {
        windowSize.toDpSize()
    }
    // Calculate the window size class
    return getWindowSizeClass(windowDpSize.width)
}
