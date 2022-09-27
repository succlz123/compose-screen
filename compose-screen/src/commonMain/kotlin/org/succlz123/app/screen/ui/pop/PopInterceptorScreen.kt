package org.succlz123.app.screen.ui.pop

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenRecord
import org.succlz123.lib.screen.ScreenArgs
import org.succlz123.lib.screen.value

@Composable
fun PopInterceptorScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenRecord = LocalScreenRecord.current
    val countValue = screenRecord.arguments.value("KEY_COUNT", 0)
    val nextValue = countValue + 1
    BaseScreenUI(
        screenNavigator, Manifest.PopInterceptorScreen, if (nextValue > 3) {
            "Can't click to back - The System OnBackPressedDispatcher is also can't work"
        } else {
            countValue.toString()
        }
    ) {
        if (nextValue > 3) {
            screenRecord.popFinalInterceptor = { backstackList, destroyList, _ ->
                backstackList.size > 1 && destroyList.firstOrNull()?.screen?.name == Manifest.PopInterceptorScreen
            }
            screenNavigator.pop()
        } else {
            screenNavigator.push(
                screenName = Manifest.PopInterceptorScreen,
                arguments = ScreenArgs.putValue("KEY_COUNT", nextValue)
            )
        }
    }
}