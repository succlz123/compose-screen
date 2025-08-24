package org.succlz123.lib.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalArkUIViewController
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.initMainHandler
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcher
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcherOwner
import org.succlz123.lib.screen.lifecycle.rememberScreenLifecycleOwner
import org.succlz123.lib.screen.viewmodel.ScreenDefaultEmptySavableViewModel
import org.succlz123.lib.screen.viewmodel.rememberScreenViewModelStoreOwner
import org.succlz123.lib.screen.window.getWindowSizeClass
import org.succlz123.lib.screen.window.rememberScreenWindowSizeOwner

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenContainer(
    content: @Composable () -> Unit,
) {
    val screenLifecycleOwner = rememberScreenLifecycleOwner()
    val savableViewModel = remember { ScreenDefaultEmptySavableViewModel() }
    val screenViewModelStoreOwner = rememberScreenViewModelStoreOwner(savableViewModel)

    val screenOnBackPressedDispatcherOwner = rememberOnBackPressedDispatcherOwner()

    val screenWindowSizeOwner = rememberScreenWindowSizeOwner()
    val windowSize = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current
    val windowSizeClass = remember(windowSize, density) {
        val windowDpSize = with(density) {
            windowSize.toSize().toDpSize()
        }
        getWindowSizeClass(windowDpSize.width)
    }
    LaunchedEffect(windowSize, density) {
        screenWindowSizeOwner.getWindowHolder().size.value = windowSize.toSize()
        screenWindowSizeOwner.getWindowHolder().sizeClass.value = windowSizeClass
    }

    CompositionLocalProvider(
        LocalScreenScreenLifecycleOwner provides screenLifecycleOwner,
        LocalScreenViewModelStoreOwner provides screenViewModelStoreOwner,
        LocalScreenSavableViewModel provides savableViewModel,
        LocalScreenOnBackPressedDispatcherOwner provides screenOnBackPressedDispatcherOwner,
        LocalScreenWindowSizeOwner provides screenWindowSizeOwner,
    ) {
        content.invoke()
    }
}

@Composable
fun rememberOnBackPressedDispatcherOwner(): ScreenOnBackPressedDispatcherOwner {
    val composeLifecycle = remember {
        object : Lifecycle() {
            private val observerList = arrayListOf<LifecycleObserver>()

            var state = State.INITIALIZED

            override val currentState: State
                get() = state

            override fun addObserver(observer: LifecycleObserver) {
                observerList.add(observer)
            }

            override fun removeObserver(observer: LifecycleObserver) {
                observerList.remove(observer)
            }

            fun setCurrentState(source: LifecycleOwner, state: State) {
                this.state = state
                for (lifecycleObserver in observerList) {
                    if (lifecycleObserver is LifecycleEventObserver) {
                        when (state) {
                            State.DESTROYED -> {
                                lifecycleObserver.onStateChanged(source, Event.ON_DESTROY)
                            }

                            State.STARTED -> {
                                lifecycleObserver.onStateChanged(source, Event.ON_START)
                            }

                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }
    val composeLifecycleOwner = remember {
        object : LifecycleOwner {
            override val lifecycle: Lifecycle
                get() = composeLifecycle
        }
    }
    DisposableEffect(Unit) {
        composeLifecycle.setCurrentState(composeLifecycleOwner, Lifecycle.State.STARTED)
        onDispose {
            composeLifecycle.setCurrentState(composeLifecycleOwner, Lifecycle.State.DESTROYED)
        }
    }

    val isEnable = remember { mutableStateOf(true) }
    val systemOnBackPressedDispatcher = LocalArkUIViewController.current.onBackPressedDispatcher
    val screenOnBackPressedDispatcher = remember {
        ScreenOnBackPressedDispatcher {
            isEnable.value = it
        }
    }
    DisposableEffect(Unit) {
        val cancel = systemOnBackPressedDispatcher.addOnBackPressedCallback {
            if (isEnable.value) {
                screenOnBackPressedDispatcher.onBackPressed()
                true
            } else {
                false
            }
        }
        onDispose(cancel)
    }

    return remember {
        object : ScreenOnBackPressedDispatcherOwner {
            override fun get(): ScreenOnBackPressedDispatcher {
                return screenOnBackPressedDispatcher
            }

            override fun sendBackPressedToSystem() {
            }
        }
    }
}
