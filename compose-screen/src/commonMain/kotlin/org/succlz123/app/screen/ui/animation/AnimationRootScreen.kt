package org.succlz123.app.screen.ui.animation

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.transition.ScreenTransitionRightInLeftOutPop
import org.succlz123.lib.screen.transition.ScreenTransitionRightInLeftOutPush
import org.succlz123.lib.screen.operation.PushOptions

@Composable
fun AnimationRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator,
        Manifest.AnimationRootScreen,
        "Click to ${Manifest.AnimationScreen} - RightInLeftOut"
    ) {
        screenNavigator.push(
            Manifest.AnimationScreen,
            pushOptions = PushOptions(
                pushTransition = ScreenTransitionRightInLeftOutPush(),
                popTransition = ScreenTransitionRightInLeftOutPop()
            )
        )
    }
}

@Composable
fun AnimationScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator, Manifest.AnimationScreen, "Click to back"
    ) {
        screenNavigator.pop()
    }
}
