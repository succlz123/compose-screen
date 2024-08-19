package org.succlz123.lib.screen.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.succlz123.lib.screen.LocalScreenWindowSizeOwner

@Composable
fun rememberIsWindowExpanded(): Boolean {
    val windowSizeOwner = LocalScreenWindowSizeOwner.current
    return windowSizeOwner.getWindowHolder().sizeClass.collectAsState().value == ScreenWindowSizeClass.Expanded
}

@Composable
fun rememberIsWindowMedium(): Boolean {
    val windowSizeOwner = LocalScreenWindowSizeOwner.current
    return windowSizeOwner.getWindowHolder().sizeClass.collectAsState().value == ScreenWindowSizeClass.Medium
}

@Composable
fun rememberIsWindowCompact(): Boolean {
    val windowSizeOwner = LocalScreenWindowSizeOwner.current
    return windowSizeOwner.getWindowHolder().sizeClass.collectAsState().value == ScreenWindowSizeClass.Compact
}