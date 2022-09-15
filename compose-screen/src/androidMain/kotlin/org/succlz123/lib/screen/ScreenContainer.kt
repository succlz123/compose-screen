package org.succlz123.lib.screen

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import kotlinx.coroutines.flow.distinctUntilChanged
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcher
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcherOwner
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.lifecycle.ScreenLifecycleOwner
import org.succlz123.lib.screen.lifecycle.rememberScreenLifecycleOwner
import org.succlz123.lib.screen.viewmodel.rememberScreenViewModelStoreOwner
import org.succlz123.lib.screen.window.rememberScreenWindowSizeOwner

val LocalAndroidScreenViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("${ScreenLogger.TAG}: There is no android view model store owner in the local!")
}

@Composable
fun ScreenContainer(
    androidLifecycleOwner: LifecycleOwner,
    androidViewModelStoreOwner: ViewModelStoreOwner,
    androidOnBackPressedDispatcherOwner: OnBackPressedDispatcherOwner,
    content: @Composable () -> Unit,
) {
    val screenLifecycleOwner = rememberScreenLifecycleOwner()
    bind2AndroidLifecycle(androidLifecycleOwner, screenLifecycleOwner)
    val screenViewModelStoreOwner = rememberScreenViewModelStoreOwner()
    val screenOnBackPressedDispatcherOwner = rememberOnBackPressedDispatcherOwner(
        androidLifecycleOwner, androidOnBackPressedDispatcherOwner.onBackPressedDispatcher
    )
    val screenWindowSizeOwner = rememberScreenWindowSizeOwner()

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    LaunchedEffect(Unit) {
        snapshotFlow { configuration }.distinctUntilChanged().collect {
            screenWindowSizeOwner.getWindowHolder().size =
                Size(it.screenWidthDp.dp.value * density.density, it.screenHeightDp.dp.value * density.density)
        }
    }

    CompositionLocalProvider(
        LocalScreenScreenLifecycleOwner provides screenLifecycleOwner,

        LocalAndroidScreenViewModelStoreOwner provides androidViewModelStoreOwner,
        LocalScreenViewModelStoreOwner provides screenViewModelStoreOwner,

        LocalScreenOnBackPressedDispatcherOwner provides screenOnBackPressedDispatcherOwner,
        LocalScreenWindowSizeOwner provides screenWindowSizeOwner,
    ) {
        content.invoke()
    }
}

@Composable
public fun bind2AndroidLifecycle(
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
fun rememberOnBackPressedDispatcherOwner(
    androidLifecycleOwner: LifecycleOwner, androidOnBackPressedDispatcher: OnBackPressedDispatcher
): ScreenOnBackPressedDispatcherOwner {
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
                    androidOnBackPressedDispatcher.addCallback(androidLifecycleOwner, this)
                }
            }

            override fun getOnBackPressedDispatcher(): ScreenOnBackPressedDispatcher {
                return screenOnBackPressedDispatcher
            }
        }
    }
}
