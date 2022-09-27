package org.succlz123.app.screen.ui.push

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.operation.PushOptions

@Composable
fun PushSingleTaskRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator, Manifest.PushSingleTaskRootScreen, "Click to ${Manifest.PushSingleTaskScreen}"
    ) {
        screenNavigator.push(Manifest.PushSingleTaskScreen)
    }
}

@Composable
fun PushSingleTaskScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator,
        Manifest.PushSingleTaskScreen,
        "Click to ${Manifest.MainScreen} in SingleTask Model"
    ) {
        screenNavigator.push(Manifest.MainScreen, pushOptions = PushOptions().apply {
            removePredicate = PushOptions.SingleTaskPredicate(Manifest.MainScreen)
        })
    }
}
