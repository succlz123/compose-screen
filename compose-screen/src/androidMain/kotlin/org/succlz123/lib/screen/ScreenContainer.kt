package org.succlz123.lib.screen

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcher
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcherOwner
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.lifecycle.ScreenLifecycleOwner
import org.succlz123.lib.screen.lifecycle.rememberScreenLifecycleOwner
import org.succlz123.lib.screen.viewmodel.AndroidSavableViewModel
import org.succlz123.lib.screen.viewmodel.androidViewModel
import org.succlz123.lib.screen.viewmodel.rememberScreenViewModelStoreOwner
import org.succlz123.lib.screen.window.rememberScreenWindowSizeOwner
import org.succlz123.lib.screen.window.rememberWindowSize
import org.succlz123.lib.screen.window.rememberWindowSizeClass

val LocalAndroidScreenViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("${ScreenLogger.TAG}: There is no android view model store owner in the local!")
}

@Composable
fun ScreenContainer(
    activity: AppCompatActivity,
    content: @Composable () -> Unit,
) {
    ScreenContainer(activity, activity, activity, activity, content)
}

@Composable
fun ScreenContainer(
    activity: Activity,
    androidLifecycleOwner: LifecycleOwner,
    androidViewModelStoreOwner: ViewModelStoreOwner,
    androidOnBackPressedDispatcherOwner: OnBackPressedDispatcherOwner,
    content: @Composable () -> Unit,
) {
    val screenLifecycleOwner = rememberScreenLifecycleOwner()
    Bind2AndroidLifecycle(androidLifecycleOwner, screenLifecycleOwner)

    val savableViewModel = androidViewModelStoreOwner.androidViewModel<AndroidSavableViewModel>()
    val screenViewModelStoreOwner = rememberScreenViewModelStoreOwner(savableViewModel)

    val screenOnBackPressedDispatcherOwner = rememberOnBackPressedDispatcherOwner(
        androidOnBackPressedDispatcherOwner.onBackPressedDispatcher
    )
    val screenWindowSizeOwner = rememberScreenWindowSizeOwner()
    val windowSize = activity.rememberWindowSize()
    val windowSizeClass = activity.rememberWindowSizeClass()
    val configuration = LocalConfiguration.current

    LaunchedEffect(configuration) {
        screenWindowSizeOwner.getWindowHolder().size.value = windowSize
        screenWindowSizeOwner.getWindowHolder().sizeClass.value = windowSizeClass
    }

    CompositionLocalProvider(
        LocalScreenScreenLifecycleOwner provides screenLifecycleOwner,

        LocalAndroidScreenViewModelStoreOwner provides androidViewModelStoreOwner,
        LocalScreenSavableViewModel provides savableViewModel,
        LocalScreenViewModelStoreOwner provides screenViewModelStoreOwner,

        LocalScreenOnBackPressedDispatcherOwner provides screenOnBackPressedDispatcherOwner,
        LocalScreenWindowSizeOwner provides screenWindowSizeOwner,
    ) {
        content.invoke()
    }
}

@Composable
fun Bind2AndroidLifecycle(
    androidLifecycleOwner: LifecycleOwner, screenLifecycleOwner: ScreenLifecycleOwner
) {
    DisposableEffect(Unit) {
        val androidXLifecycleObserver = object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
            }

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                screenLifecycleOwner.getLifecycle().setCurrentState(ScreenLifecycle.State.RESUMED)
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                screenLifecycleOwner.getLifecycle().setCurrentState(ScreenLifecycle.State.PAUSED)
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
            }
        }
        androidLifecycleOwner.lifecycle.addObserver(androidXLifecycleObserver)
        onDispose {
            androidLifecycleOwner.lifecycle.removeObserver(androidXLifecycleObserver)
        }
    }
}

@Composable
fun rememberOnBackPressedDispatcherOwner(androidOnBackPressedDispatcher: OnBackPressedDispatcher): ScreenOnBackPressedDispatcherOwner {
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

    return remember {
        object : ScreenOnBackPressedDispatcherOwner {
            private var callback: OnBackPressedCallback? = null

            private val screenOnBackPressedDispatcher = ScreenOnBackPressedDispatcher {
                callback?.isEnabled = it
            }

            init {
                callback = object : OnBackPressedCallback(screenOnBackPressedDispatcher.isEnable()) {
                    override fun handleOnBackPressed() {
                        screenOnBackPressedDispatcher.onBackPressed()
                    }
                }.apply {
                    androidOnBackPressedDispatcher.addCallback(composeLifecycleOwner, this)
                }
            }

            override fun get(): ScreenOnBackPressedDispatcher {
                return screenOnBackPressedDispatcher
            }

            override fun sendBackPressedToSystem() {
                androidOnBackPressedDispatcher.onBackPressed()
            }
        }
    }
}
