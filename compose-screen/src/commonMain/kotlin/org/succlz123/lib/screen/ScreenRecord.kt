package org.succlz123.lib.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import org.succlz123.lib.screen.core.GroupScreen
import org.succlz123.lib.screen.core.ItemScreen
import org.succlz123.lib.screen.core.Screen
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.operation.PushOptions
import org.succlz123.lib.screen.operation.ScreenStackState
import org.succlz123.lib.screen.transition.ScreenPopTransition
import org.succlz123.lib.screen.transition.ScreenPushTransition
import org.succlz123.lib.screen.transition.ScreenTransitionPopNone
import org.succlz123.lib.screen.transition.ScreenTransitionPushNone
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
    val pushOptions: PushOptions,
    val onResult: (result: Any?) -> Unit
) : ScreenViewModelStore() {

    companion object {

        fun newInstance(
            screen: Screen,
            saveId: Int,
            screenLifecycle: ScreenLifecycle,
            arguments: ScreenArgs,
            pushOptions: PushOptions?,
            onResult: (result: Any?) -> Unit
        ): ScreenRecord {
            return ScreenRecord(screen, saveId, screenLifecycle, arguments, pushOptions ?: (PushOptions()), onResult)
        }
    }

    internal val popupScreenList = mutableStateListOf<ScreenRecord>()

    internal val popupWindowRecord = mutableStateOf<ScreenRecord?>(null)

    fun getPopupWindowRecord(): ScreenRecord? {
        return popupWindowRecord.value
    }

    val stackStateFlow = MutableStateFlow(ScreenStackState.UNKNOWN)

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

    internal fun pushT(): ScreenPushTransition {
        return if (pushOptions.pushTransition is ScreenTransitionPushNone) {
            when (screen) {
                is GroupScreen -> {
                    screen.pushTransition
                }

                is ItemScreen -> {
                    screen.pushTransition
                }

                else -> {
                    pushOptions.pushTransition
                }
            }
        } else {
            pushOptions.pushTransition
        }
    }

    internal fun popT(): ScreenPopTransition {
        return if (pushOptions.popTransition is ScreenTransitionPopNone) {
            when (screen) {
                is GroupScreen -> {
                    screen.popTransition
                }

                is ItemScreen -> {
                    screen.popTransition
                }

                else -> {
                    pushOptions.popTransition
                }
            }
        } else {
            pushOptions.popTransition
        }
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