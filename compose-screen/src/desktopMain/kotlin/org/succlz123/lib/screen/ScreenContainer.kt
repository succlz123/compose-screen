package org.succlz123.lib.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.distinctUntilChanged
import org.succlz123.lib.screen.back.rememberOnBackPressedDispatcherOwner
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.lifecycle.rememberScreenLifecycleOwner
import org.succlz123.lib.screen.viewmodel.rememberScreenViewModelStoreOwner
import org.succlz123.lib.screen.window.rememberScreenWindowSizeOwner
import java.awt.Dimension

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenContainer(
    enableEscBack: Boolean = false,
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    minimumSize: Dimension? = null,
    visible: Boolean = true,
    title: String = "",
    icon: Painter? = null,
    undecorated: Boolean = false,
    transparent: Boolean = false,
    resizable: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    content: @Composable FrameWindowScope.() -> Unit,
) {
    val screenLifecycleOwner = rememberScreenLifecycleOwner()
    val screenViewModelStoreOwner = rememberScreenViewModelStoreOwner()
    val screenOnBackPressedDispatcherOwner = rememberOnBackPressedDispatcherOwner()
    val screenWindowSizeOwner = rememberScreenWindowSizeOwner()

    LaunchedEffect(Unit) {
        snapshotFlow { state.isMinimized }.distinctUntilChanged().collect {
            screenLifecycleOwner.getLifecycle().setCurrentState(
                if (state.isMinimized) {
                    ScreenLifecycle.State.PAUSED
                } else {
                    ScreenLifecycle.State.RESUMED
                }
            )
        }
    }

    val density = LocalDensity.current
    LaunchedEffect(Unit) {
        snapshotFlow { state.size }.distinctUntilChanged().collect {
            screenWindowSizeOwner.getWindowHolder().size =
                Size(it.width.value * density.density, it.height.value * density.density)
        }
    }

    CompositionLocalProvider(
        LocalScreenScreenLifecycleOwner provides screenLifecycleOwner,
        LocalScreenViewModelStoreOwner provides screenViewModelStoreOwner,
        LocalScreenOnBackPressedDispatcherOwner provides screenOnBackPressedDispatcherOwner,
        LocalScreenWindowSizeOwner provides screenWindowSizeOwner,
    ) {
        Window(onCloseRequest = {
            screenLifecycleOwner.getLifecycle().setCurrentState(ScreenLifecycle.State.DESTROYED)
            onCloseRequest.invoke()
        },
            state = state,
            visible = visible,
            title = title,
            icon = icon,
            undecorated = undecorated,
            transparent = transparent,
            resizable = resizable,
            enabled = enabled,
            focusable = focusable,
            alwaysOnTop = alwaysOnTop,
            onPreviewKeyEvent = {
                if (enableEscBack && it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                    screenOnBackPressedDispatcherOwner.getOnBackPressedDispatcher().onBackPressed()
                }
                onPreviewKeyEvent(it)
            },
            onKeyEvent = onKeyEvent,
            content = {
                if (minimumSize != null) {
                    window.minimumSize = minimumSize
                }
                content.invoke(this)
            })
    }
}
