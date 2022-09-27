package org.succlz123.app.screen.ui.push

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenRecord
import org.succlz123.lib.screen.ScreenArgs
import org.succlz123.lib.screen.value

@Composable
fun PushPopScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenRecord = LocalScreenRecord.current
    val countValue = screenRecord.arguments.value("KEY_COUNT", 0)
    BaseScreenUI(screenNavigator, Manifest.PushPopScreen, countValue.toString()) {
        val nextValue = countValue + 1
        screenNavigator.push(
            screenName = Manifest.PushPopScreen,
            arguments = ScreenArgs.putValue("KEY_COUNT", nextValue)
        )
    }
}