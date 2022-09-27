package org.succlz123.app.screen.ui.push

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenRecord
import org.succlz123.lib.screen.operation.PushOptions
import org.succlz123.lib.screen.screenKey

@Composable
fun PushClearTaskRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenKey = 1
    BaseScreenUI(
        screenNavigator, Manifest.PushClearTaskRootScreen, "Click to ${Manifest.PushClearTaskScreen}"
    ) {
        screenNavigator.push(
            Manifest.PushClearTaskScreen, screenKey = screenKey.toString()
        )
    }
}

@Composable
fun PushClearTaskScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val arguments = LocalScreenRecord.current.arguments
    val screenKey = (arguments.screenKey()?.toIntOrNull() ?: 0) + 1
    BaseScreenUI(
        screenNavigator, Manifest.PushClearTaskScreen, when (screenKey) {
            4 -> {
                "Click to ${Manifest.PushClearTaskScreen}-$screenKey and clear all the previous tasks"
            }

            5 -> {
                "Click to ${Manifest.PushClearTaskScreen}-$screenKey\n(The new root stack)"
            }

            6 -> {
                "Pop to the bottom of stack"
            }

            else -> {
                "Click to ${Manifest.PushClearTaskScreen}-$screenKey"
            }
        }
    ) {
        when (screenKey) {
            4 -> {
                screenNavigator.push(screenName = Manifest.PushClearTaskScreen,
                    screenKey = screenKey.toString(),
                    pushOptions = PushOptions().apply {
                        removePredicate = PushOptions.ClearTaskPredicate()
                    })
            }

            5 -> {
                screenNavigator.push(
                    screenName = Manifest.PushClearTaskScreen, screenKey = screenKey.toString()
                )
            }

            6 -> {
                screenNavigator.popToRoot()
            }

            else -> {
                screenNavigator.push(
                    screenName = Manifest.PushClearTaskScreen, screenKey = screenKey.toString()
                )
            }
        }
    }
}
