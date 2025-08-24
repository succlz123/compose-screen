package org.succlz123.lib.screen

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcher
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcherOwner
import org.succlz123.lib.screen.core.ItemScreen
import org.succlz123.lib.screen.ext.popupwindow.ScreenPopupWindowPopupScreen
import org.succlz123.lib.screen.ext.popupwindow.ScreenPopupWindowSaveId
import org.succlz123.lib.screen.ext.toast.ARGS_TOAST_TIME_SHORT
import org.succlz123.lib.screen.ext.toast.KEY_TOAST_TIME
import org.succlz123.lib.screen.ext.toast.ScreenToastPopupScreen
import org.succlz123.lib.screen.ext.toast.ScreenToastSaveId
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.lifecycle.ScreenLifecycleObserver
import org.succlz123.lib.screen.operation.*
import org.succlz123.lib.screen.transition.ScreenTransitionPopNone
import org.succlz123.lib.screen.transition.ScreenTransitionPushNone
import org.succlz123.lib.screen.viewmodel.ScreenViewModelStore

class ScreenManager : ScreenLifecycleObserver, ScreenViewModelStore() {
    lateinit var screenCollector: ScreenCollector

    lateinit var hostLifecycle: ScreenLifecycle

    private lateinit var onBackPressedDispatcherOwner: ScreenOnBackPressedDispatcherOwner

    var recordSaveId = 0

    var recordSaveIdCoe = 10000

    val recordStack = ScreenRecordStack()

    val job = Job()

    val managerScope: CoroutineScope = CoroutineScope(job)

    var toastJob: Job? = null

    private val screenOnBackPressedCallback = {
        if (recordStack.canPop()) {
            pop(null, false, false, null)
        }
    }

    fun init(
        screenCollector: ScreenCollector,
        hostLifecycle: ScreenLifecycle,
        onBackPressedDispatcherOwer: ScreenOnBackPressedDispatcherOwner
    ) {
        this.screenCollector = screenCollector
        this.hostLifecycle = hostLifecycle
        this.onBackPressedDispatcherOwner = onBackPressedDispatcherOwer
        onBackPressedDispatcherOwer.get().setRootCallback(screenOnBackPressedCallback)
        updateBackPressedDispatcherEnable()
    }

    fun initRecordStack(rootScreenName: String) {
        if (recordStack.getRecordList().isNotEmpty()) {
            return
        }
        push(rootScreenName, ScreenArgs(), null)
    }

    fun getStackHistory(): String {
        return recordStack.getStackHistory()
    }

    private fun updateBackPressedDispatcherEnable() {
        onBackPressedDispatcherOwner.get().setEnable(recordStack.canPop())
    }

    fun exitScreen() {
        onBackPressedDispatcherOwner.get().setEnable(false)
        onBackPressedDispatcherOwner.sendBackPressedToSystem()
    }

    fun push(screenName: String, arguments: ScreenArgs, pushOptions: PushOptions?) {
        val find = screenCollector.screenList.find { it.name == screenName }
        if (find == null) {
            ScreenLogger.debugLog("Push Operation: Can't find the target screen - $screenName")
            return
        }
        PushOperation.push(this, find, arguments, pushOptions)
        updateBackPressedDispatcherEnable()
    }

    fun pushDeeplink(deeplink: String, arguments: ScreenArgs, pushOptions: PushOptions?) {
        PushOperation.pushDeeplink(this, deeplink, arguments, pushOptions)
        updateBackPressedDispatcherEnable()
    }

    fun toast(arguments: ScreenArgs, content: (@Composable (ScreenRecord) -> Unit)?) {
        val screen = if (content != null) {
            ItemScreen(
                "ScreenAnonymityToastPopupScreen",
                false,
                0,
                ScreenTransitionPushNone(),
                ScreenTransitionPopNone(),
                content
            )
        } else {
            screenCollector.screenList.find { it.name == ScreenToastPopupScreen }
        }
        if (screen == null) {
            return
        }
        recordStack.toastRecord.value = ScreenRecord.newInstance(
            screen, ScreenToastSaveId, hostLifecycle, arguments, null
        )
        val time = arguments.value(KEY_TOAST_TIME, ARGS_TOAST_TIME_SHORT)
        toastJob?.cancel()
        toastJob = managerScope.launch {
            delay(time)
            cancelToast()
        }
    }

    fun cancelToast() {
        recordStack.toastRecord.value = null
    }

    fun popupWindow(arguments: ScreenArgs, content: (@Composable () -> Unit)?) {
        val currentGroupRecord = recordStack.getCurrentGroupRecord()
        val screen = screenCollector.screenList.find { it.name == ScreenPopupWindowPopupScreen }
        if (currentGroupRecord == null || screen == null) {
            return
        }
        currentGroupRecord.popupWindowRecord.value = ScreenRecord.newInstance(
            screen, ScreenPopupWindowSaveId, hostLifecycle, arguments, null
        ).apply {
            innerContent = content
        }
    }

    fun cancelPopupWindow() {
        recordStack.getCurrentGroupRecord()?.popupWindowRecord?.value = null
    }

    fun remove(screenName: String, screenKey: String? = null) {
        RemoveOperation.remove(recordStack, screenName, screenKey)
        updateBackPressedDispatcherEnable()
    }

    fun pop(
        result: Any?, finishCurGroupScreen: Boolean, finishAllPopupScreen: Boolean, popOptions: PopOptions?
    ) {
        if (!recordStack.canPop()) {
            return
        }
        if (finishCurGroupScreen) {
            if (popOptions?.popPredicate != null) {
                PopOperation.popPredicateGroupScreen(recordStack, result, popOptions)
            } else {
                PopOperation.popGroupScreen(recordStack, result, popOptions)
            }
        } else {
            val currentGroupRecord = recordStack.getCurrentGroupRecord()
            val currentPopupRecordList = currentGroupRecord?.popupScreenList
            val currentToastPopupWindowRecord = currentGroupRecord?.popupWindowRecord
            if (currentPopupRecordList.isNullOrEmpty()) {
                if (currentToastPopupWindowRecord?.value != null) {
                    currentToastPopupWindowRecord.value = null
                } else if (popOptions?.popPredicate != null) {
                    PopOperation.popPredicateGroupScreen(recordStack, result, popOptions)
                } else {
                    PopOperation.popGroupScreen(recordStack, result, popOptions)
                }
            } else {
                if (finishAllPopupScreen) {
                    PopOperation.popAllPopupScreen(recordStack)
                } else {
                    PopOperation.popCurPopupScreen(recordStack)
                }
            }
        }
        updateBackPressedDispatcherEnable()
    }

    fun popTo(screenName: String, screenKey: String?, result: Any?, popOptions: PopOptions? = null) {
        if (!recordStack.canPop()) {
            return
        }
        PopOperation.popToGroupScreen(recordStack, screenName, screenKey, result, popOptions)
        updateBackPressedDispatcherEnable()
    }

    fun popToRoot(result: Any?, popOptions: PopOptions? = null) {
        if (!recordStack.canPop()) {
            return
        }
        PopOperation.popToRootGroupScreen(recordStack, result, popOptions)
        updateBackPressedDispatcherEnable()
    }

    fun popAllPopupScreen() {
        PopOperation.popAllPopupScreen(recordStack)
        updateBackPressedDispatcherEnable()
    }

    fun popPopupScreen() {
        PopOperation.popCurPopupScreen(recordStack)
        updateBackPressedDispatcherEnable()
    }

    override fun onChanged(state: ScreenLifecycle.State) {
        when (state) {
            ScreenLifecycle.State.DESTROYED -> {
                recordStack.clearStackList()
            }

            else -> {}
        }
    }

    override fun toString(): String {
        return "ScreenManager"
    }
}
