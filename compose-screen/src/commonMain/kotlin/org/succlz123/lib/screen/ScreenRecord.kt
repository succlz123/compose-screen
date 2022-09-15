package org.succlz123.lib.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import org.succlz123.lib.screen.core.Screen
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.operation.PushOptions
import org.succlz123.lib.screen.operation.ScreenStackState
import org.succlz123.lib.screen.viewmodel.ScreenViewModelStore

fun ScreenRecord?.isTargetRecord(screenName: String, screenKey: String?): Boolean {
    if (this == null) {
        return false
    }
    return screen.name == screenName && if (screenKey.isNullOrEmpty()) {
        true
    } else {
        arguments.screenKey() == screenKey
    }
}


class ScreenRecord(
    val screen: Screen,
    val saveId: Int,
    val hostLifecycle: ScreenLifecycle,
    val arguments: ScreenArgs,
    val pushOptions: PushOptions
) : ScreenViewModelStore() {

    companion object {

        fun newInstance(
            screen: Screen,
            saveId: Int,
            screenLifecycle: ScreenLifecycle,
            arguments: ScreenArgs,
            pushOptions: PushOptions?
        ): ScreenRecord {
            return ScreenRecord(screen, saveId, screenLifecycle, arguments, pushOptions ?: (PushOptions()))
        }
    }

    internal val popupScreenList = mutableStateListOf<ScreenRecord>()

    internal val popupWindowRecord = mutableStateOf<ScreenRecord?>(null)

    val stackStateFlow = MutableStateFlow(ScreenStackState.UNKNOWN)

    var result: Any? = null
        internal set

    var removeFlag: Boolean = false
        internal set

    var popFinalInterceptor: PopStackFinalInterceptor? = null

    internal var innerContent: (@Composable () -> Unit)? = null

    internal fun getCurrentPopupRecord(): ScreenRecord? {
        return if (popupScreenList.size > 0) {
            popupScreenList[popupScreenList.size - 1]
        } else {
            null
        }
    }

    internal fun getAllPopupScreen(): MutableList<ScreenRecord> {
        return popupScreenList.toMutableList()
    }

    internal fun getCurrentPopupScreen(): Screen? {
        return getCurrentPopupRecord()?.screen
    }

    internal fun removePopupRecord(screenRecord: ScreenRecord) {
        popupScreenList.remove(screenRecord)
    }

    override fun toString(): String {
        val key = arguments.screenKey()
        return "${screen.name}${
            if (key.isNullOrEmpty()) {
                ""
            } else {
                " key: $key"
            }
        }"
    }
}