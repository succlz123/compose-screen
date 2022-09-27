package org.succlz123.app.screen.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenWindowSizeOwner
import org.succlz123.lib.screen.window.ScreenWindowSizeClass

@Composable
fun AdaptiveLayoutsRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenWindowSize = LocalScreenWindowSizeOwner.current.getWindowHolder()
    val windowWidth = screenWindowSize.size.collectAsState().value.width
    val windowHeight = screenWindowSize.size.collectAsState().value.height
    val sizeClass = screenWindowSize.sizeClass.collectAsState().value
    BaseScreenUI(
        screenNavigator,
        Manifest.AdaptiveLayoutsRootScreen,
        "Current window size - width: ${windowWidth}, height: ${windowHeight}\nIs expanded layout: $sizeClass"
    ) {
    }
}
