package org.succlz123.app.screen.ui.push

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.operation.PushOptions

@Composable
fun PushCloseItselfRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val scope = rememberCoroutineScope()
    BaseScreenUI(
        screenNavigator, Manifest.PushCloseItselfRootScreen,
        "Click to ${Manifest.PushCloseItselfScreen} and close itself"
    ) {
        // option-1
//        screenNavigator.pop()
//        screenNavigator.push(Manifest.PushCloseCurrentScreen)
        // option-2
        screenNavigator.push(
            Manifest.PushCloseItselfScreen,
            pushOptions = PushOptions(removePredicate = PushOptions.PopItselfPredicate())
        )
    }
}

@Composable
fun PushCloseItselfScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator, Manifest.PushCloseItselfScreen,
        "Click back but the previous was gone"
    ) {
        screenNavigator.pop()
    }
}
