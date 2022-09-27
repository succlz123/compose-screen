package org.succlz123.app.screen.ui.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenRecord
import org.succlz123.lib.screen.deeplink

@Composable
fun DeeplinkRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val url = "https://www.baidu.com/test/text?id=3&type=air"
    BaseScreenUI(
        screenNavigator, Manifest.DeeplinkRootScreen, "Click to ${Manifest.DeeplinkScreen} - $url"
    ) {
        screenNavigator.pushDeeplink(url)
    }
}

@Composable
fun DeeplinkScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val arguments = LocalScreenRecord.current.arguments
    val deeplink = arguments.deeplink()
    LaunchedEffect(Unit) {
        screenNavigator.toast("Deeplink is $deeplink")
    }
    BaseScreenUI(
        screenNavigator, Manifest.DeeplinkScreen, "Click to root"
    ) {
        screenNavigator.popToRoot()
    }
}
