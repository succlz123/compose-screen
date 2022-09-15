package org.succlz123.lib.screen.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.lifecycle.ScreenLifecycleObserver
import kotlin.coroutines.CoroutineContext

internal class ScreenLifecycleCoroutineScope(
    val screenLifecycle: ScreenLifecycle,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, ScreenLifecycleObserver {

    init {
        if (screenLifecycle.getCurrentState() <= ScreenLifecycle.State.DESTROYED) {
            coroutineContext.cancel()
        }
    }

    fun register() {
        launch(Dispatchers.Main.immediate) {
            if (screenLifecycle.getCurrentState() >= ScreenLifecycle.State.CREATED) {
                screenLifecycle.addObserver(this@ScreenLifecycleCoroutineScope)
            } else {
                coroutineContext.cancel()
            }
        }
    }

    override fun onChanged(state: ScreenLifecycle.State) {
        if (state <= ScreenLifecycle.State.DESTROYED) {
            screenLifecycle.removeObserver(this)
            coroutineContext.cancel()
        }
    }
}