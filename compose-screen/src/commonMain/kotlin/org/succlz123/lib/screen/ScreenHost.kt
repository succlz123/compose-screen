package org.succlz123.lib.screen

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.succlz123.lib.screen.back.ScreenOnBackPressedDispatcherOwner
import org.succlz123.lib.screen.ext.popupwindow.ScreenPopupWindowSaveId
import org.succlz123.lib.screen.ext.toast.ScreenToastSaveId
import org.succlz123.lib.screen.lifecycle.ScreenLifecycle
import org.succlz123.lib.screen.lifecycle.ScreenLifecycleObserver
import org.succlz123.lib.screen.lifecycle.ScreenLifecycleOwner
import org.succlz123.lib.screen.operation.PushOptions
import org.succlz123.lib.screen.operation.ScreenStackState
import org.succlz123.lib.screen.viewmodel.ScreenSavableViewModel
import org.succlz123.lib.screen.viewmodel.ScreenViewModelStoreOwner
import org.succlz123.lib.screen.window.ScreenWindowSizeOwner

val LocalScreenScreenLifecycleOwner = compositionLocalOf<ScreenLifecycleOwner> {
    error("${ScreenLogger.TAG}: There is no ScreenLifecycleOwner in the local!")
}

val LocalScreenSavableViewModel = compositionLocalOf<ScreenSavableViewModel> {
    error("${ScreenLogger.TAG}: There is no ScreenSavableViewModel in the local!")
}

val LocalScreenViewModelStoreOwner = compositionLocalOf<ScreenViewModelStoreOwner> {
    error("${ScreenLogger.TAG}: There is no ScreenViewModel in the local!")
}

val LocalScreenOnBackPressedDispatcherOwner = compositionLocalOf<ScreenOnBackPressedDispatcherOwner> {
    error("${ScreenLogger.TAG}: There is no OnBackPressedDispatcherOwner in the local!")
}

val LocalScreenWindowSizeOwner = compositionLocalOf<ScreenWindowSizeOwner> {
    error("${ScreenLogger.TAG}: There is no ScreenWindowSizeOwner in the local!")
}

val LocalScreenNavigator = compositionLocalOf<ScreenNavigator> {
    error("${ScreenLogger.TAG}: There is no ScreenNavigator in the local!")
}

val LocalScreenRecord = compositionLocalOf<ScreenRecord> {
    error("${ScreenLogger.TAG}: There is no ScreenRecord in the local!")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScreenHost(
    modifier: Modifier = Modifier,
    screenNavigator: ScreenNavigator,
    rootScreenName: String,
    collector: ScreenCollector.() -> Unit,
) {
    CompositionLocalProvider(
        LocalScreenNavigator provides screenNavigator
    ) {
        val lifecycleOwner = LocalScreenScreenLifecycleOwner.current
        val savableViewModel = LocalScreenSavableViewModel.current
        val viewModelStoreOwner = LocalScreenViewModelStoreOwner.current
        val onBackPressedDispatcher = LocalScreenOnBackPressedDispatcherOwner.current

        val saveableStateHolder = rememberSaveableStateHolder()

        val screenCollector = remember {
            ScreenCollector(rootScreenName).apply {
                collector.invoke(this)
                init()
            }
        }

        val manager = remember {
            val sm = savableViewModel.getScreenManager() ?: ScreenManager()
            sm.apply {
                this.init(
                    screenCollector, lifecycleOwner.getLifecycle(), onBackPressedDispatcher.getOnBackPressedDispatcher()
                )
                screenNavigator.init(this)
                savableViewModel.bind(this)
            }
        }

        DisposableEffect(manager, lifecycleOwner, viewModelStoreOwner) {
            lifecycleOwner.getLifecycle().removeAllObserver()
            lifecycleOwner.getLifecycle().addObserver(manager)
            onDispose {
                lifecycleOwner.getLifecycle().removeAllObserver()
                if (savableViewModel == null) {
                    manager.clearAllVM()
                }
                ScreenLogger.debugLog("screen host: ON_DISPOSE")
            }
        }
        LaunchedEffect(manager, rootScreenName) {
            manager.initRecordStack(rootScreenName)
            ScreenLogger.debugLog("screen manager initRecordStack")
        }

        ScreenLogger.debugLog("screen host in composition")
        val curRecord = manager.recordStack.getCurrentGroupRecord() ?: return@CompositionLocalProvider
        ScreenLogger.debugLog("screen host in animation")
        AnimatedContent(curRecord, transitionSpec = {
            val blackStackList = manager.recordStack.getRecordList()
            val isPush = blackStackList.contains(initialState)
            val isSingleTopOrSingleTask =
                targetState.pushOptions.removePredicate is PushOptions.SingleTopPredicate || targetState.pushOptions.removePredicate is PushOptions.SingleTaskPredicate
            val isTheSameScreenName = targetState.screen == initialState.screen
            if (isSingleTopOrSingleTask && isTheSameScreenName) {
                return@AnimatedContent (EnterTransition.None with ExitTransition.None).apply {
                    targetContentZIndex = targetState.pushOptions.pushTransition.contentZIndex(
                        blackStackList, isPush, initialState, targetState
                    )
                }
            }
            ScreenLogger.debugLog("screen animation: targetState - $targetState")
            ScreenLogger.debugLog("screen animation: initialState - $initialState")
            if (isPush) {
                val pushEnterTransition = targetState.pushOptions.pushTransition.enterTransition(this)
                val pushExitTransition = targetState.pushOptions.pushTransition.exitTransition(this)
                (pushEnterTransition with pushExitTransition).apply {
                    targetContentZIndex = targetState.pushOptions.pushTransition.contentZIndex(
                        blackStackList, true, initialState, targetState
                    )
                }
            } else {
                val popEnterTransition = initialState.pushOptions.popTransition.enterTransition(this)
                val popExitTransition = initialState.pushOptions.popTransition.exitTransition(this)
                (popEnterTransition with popExitTransition).apply {
                    targetContentZIndex = targetState.pushOptions.popTransition.contentZIndex(
                        blackStackList, false, initialState, targetState
                    )
                }
            }
        }) { animatedRecord ->
            Screen(saveableStateHolder, lifecycleOwner, viewModelStoreOwner, animatedRecord)
            animatedRecord.popupWindowRecord.value?.let { popupWindow ->
                Screen(saveableStateHolder, lifecycleOwner, viewModelStoreOwner, popupWindow)
            }
            for (popupRecord in animatedRecord.popupScreenList) {
                Screen(saveableStateHolder, lifecycleOwner, viewModelStoreOwner, popupRecord)
            }
            manager.recordStack.toastRecord.value?.let { toast ->
                Screen(saveableStateHolder, lifecycleOwner, viewModelStoreOwner, toast)
            }
        }
    }
}

@Composable
private fun Screen(
    saveableStateHolder: SaveableStateHolder,
    screenLifecycleOwner: ScreenLifecycleOwner,
    screenViewModelStoreOwner: ScreenViewModelStoreOwner,
    record: ScreenRecord
) {
    if (record.saveId == ScreenPopupWindowSaveId || record.saveId == ScreenToastSaveId) {
        CompositionLocalProvider(
            LocalScreenScreenLifecycleOwner provides screenLifecycleOwner,
            LocalScreenViewModelStoreOwner provides screenViewModelStoreOwner,
            LocalScreenRecord provides record
        ) {
            record.screen.content.invoke(record)
        }
    } else {
        ScreenLogger.debugLog("screen render: ${record.saveId} $record")
        saveableStateHolder.SaveableStateProvider(record.saveId) {
            CompositionLocalProvider(
                LocalScreenScreenLifecycleOwner provides screenLifecycleOwner,
                LocalScreenViewModelStoreOwner provides screenViewModelStoreOwner,
                LocalScreenRecord provides record
            ) {
                record.screen.content.invoke(record)
            }
        }
    }
}

@Composable
fun ScreenLifecycleCallback(
    screenRecord: ScreenRecord,
    screenLifecycleState: ((ScreenLifecycle.State) -> Unit)? = null,
    hostLifecycleState: ((ScreenLifecycle.State) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    DisposableEffect(Unit) {
        val observer = object : ScreenLifecycleObserver {

            override fun onChanged(state: ScreenLifecycle.State) {
                ScreenLogger.debugLog(">>screen host<< state: ${state.name}")
                if (state.isAtLeast(ScreenLifecycle.State.RESUMED)) {
                    ScreenLogger.debugLog(">>>screen<<< state: $screenRecord - ON_SCREEN")
                    screenLifecycleState?.invoke(ScreenLifecycle.State.RESUMED)
                } else {
                    ScreenLogger.debugLog(">>>screen<<< state: $screenRecord - OFF_SCREEN")
                    screenLifecycleState?.invoke(ScreenLifecycle.State.PAUSED)
                }
                hostLifecycleState?.invoke(state)
            }

            override fun toString(): String {
                return screenRecord.toString()
            }
        }

        scope.launch {
            screenRecord.stackStateFlow.collect {
                if (it == ScreenStackState.IN_STACK) {
                    screenLifecycleState?.invoke(ScreenLifecycle.State.CREATED)

                    ScreenLogger.debugLog(">>>screen host<<< state: before add ${screenRecord.hostLifecycle}")
                    screenRecord.hostLifecycle.addObserver(observer)
                    ScreenLogger.debugLog(">>>screen host<<< state: add $observer")

                    // the maximum of lifecycle observer size is 3 lifecycle.
                    // 1. ScreenManager 2. the new one that pushed in the stack
                    // 1. ScreenManager 2. the previous one 3. the new one that pushed in the stack
                    ScreenLogger.debugLog(">>>screen host<<< state: after add ${screenRecord.hostLifecycle}")
                } else {
                    screenLifecycleState?.invoke(ScreenLifecycle.State.DESTROYED)
                }
            }
        }

        onDispose {
            ScreenLogger.debugLog(
                ">>>screen host<<< state change: remove ${
                    screenRecord.hostLifecycle.removeObserver(observer)
                } $observer"
            )
            // the maximum of lifecycle observer size is 1 lifecycle.
            // 1. ScreenManager
            // 1. ScreenManager 2. the new one that pushed in the stack
            ScreenLogger.debugLog(">>>screen host<<< state: after remove ${screenRecord.hostLifecycle}")

            ScreenLogger.debugLog(">>>screen state<<< change: $screenRecord - OFF_SCREEN")
            if (screenRecord.stackStateFlow.value == ScreenStackState.IN_STACK) {
                screenLifecycleState?.invoke(ScreenLifecycle.State.PAUSED)
            }
        }
    }
}

@Composable
fun ScreenStackStateCallback(screenRecord: ScreenRecord, stackState: ((ScreenStackState) -> Unit)? = null) {
    LaunchedEffect(Unit) {
        screenRecord.stackStateFlow.collect {
            stackState?.invoke(it)
        }
    }
}