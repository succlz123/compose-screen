package org.succlz123.app.screen.ui.back

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator

@Composable
fun BackPressedRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator,
        Manifest.DeeplinkRootScreen,
        "Android - Click the System Back Button\nDesktop - Click the Esc Button",
        showBack = false
    ) {}
}
