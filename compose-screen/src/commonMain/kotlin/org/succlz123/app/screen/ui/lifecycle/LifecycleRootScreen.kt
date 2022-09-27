package org.succlz123.app.screen.ui.lifecycle

import androidx.compose.runtime.Composable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.*
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.viewmodel.ScreenViewModel
import org.succlz123.lib.screen.viewmodel.viewModel

class TrackingViewModel() : ScreenViewModel() {

    init {
//        Analytics.report("what you want to track.")
    }
}

@Composable
fun LifecycleComposeListener(screenRecord: ScreenRecord, countValue: Int) {
    println("\n")
    val screenNavigator = LocalScreenNavigator.current
    ScreenLifecycleCallback(screenRecord, screenLifecycleState = {
        println("Screen lifecycle - $countValue: ${it.name}")
        if (it == ScreenLifecycle.State.DESTROYED) {
            screenNavigator.toast("Screen lifecycle - $countValue: is destroyed")
        }
    }, hostLifecycleState = {
        println(">>> Host lifecycle: ${it.name} <<<")
    })
}

@Composable
fun LifecycleRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenRecord = LocalScreenRecord.current
    LifecycleComposeListener(screenRecord, 0)
    BaseScreenUI(
        screenNavigator, Manifest.LifecycleRootScreen, "Click to ${Manifest.LifecycleScreen}"
    ) {
        screenNavigator.push(Manifest.LifecycleScreen, screenKey = "1")
    }
}

@Composable
fun LifecycleScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenRecord = LocalScreenRecord.current
    val countValue = screenRecord.arguments.screenKey()?.toIntOrNull() ?: 0
    LifecycleComposeListener(screenRecord, countValue)
    // on
    val trackingViewModel = viewModel { TrackingViewModel() }
    BaseScreenUI(
        screenNavigator, Manifest.LifecycleScreen, "Click and see the print log"
    ) {
        val nextValue = countValue + 1
        screenNavigator.push(
            Manifest.LifecycleScreen, screenKey = nextValue.toString()
        )
    }
}
