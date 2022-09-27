package org.succlz123.app.screen.ui.push

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.operation.PushOptions

@Composable
fun PushSingleTopRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator,
        Manifest.PushSingleTopRootScreen,
        "Click to ${Manifest.PushSingleTopScreen}"
    ) {
        screenNavigator.push(Manifest.PushSingleTopScreen)
    }
}

@Composable
fun PushSingleTopScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator,
        Manifest.PushSingleTopScreen,
        "Click to ${Manifest.PushSingleTopScreen} in SingleTop Model"
    ) {
        screenNavigator.push(
            Manifest.PushSingleTopScreen,
            pushOptions = PushOptions().apply {
                removePredicate = PushOptions.SingleTopPredicate(Manifest.PushSingleTopScreen)
            }
        )
    }
}
