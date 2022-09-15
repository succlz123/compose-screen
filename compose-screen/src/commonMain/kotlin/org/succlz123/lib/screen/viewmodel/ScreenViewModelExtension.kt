package org.succlz123.lib.screen.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.succlz123.lib.screen.*
import org.succlz123.lib.screen.operation.ScreenStackState
import kotlin.reflect.KClass

@Composable
fun rememberScreenViewModelStoreOwner(): ScreenViewModelStoreOwner {
    return remember {
        object : ScreenViewModelStoreOwner {
            var screenViewModelStore = ScreenViewModelStore()

            override fun getViewModelStore(): ScreenViewModelStore {
                return screenViewModelStore
            }
        }
    }
}

internal fun <T : ScreenViewModel> vmSignature(vmClass: KClass<T>, key: String?): String {
    return vmClass.simpleName + " / " + key
}

@Composable
inline fun <reified T : ScreenViewModel> globalViewModel(
    key: String? = null,
    noinline creator: () -> T,
): T = globalViewModel(T::class, key, creator)

@Composable
fun <T : ScreenViewModel> globalViewModel(
    vmClass: KClass<T>,
    key: String? = null,
    creator: () -> T,
): T {
    val screenNavigator = LocalScreenNavigator.current
    val sm = screenNavigator.screenManager
    return if (sm == null) {
        remember { creator() }
    } else {
        remember { sm.getViewModel(vmClass, key, creator) }
    }
}

@Composable
inline fun <reified T : ScreenViewModel> sharedViewModel(
    key: String? = null,
    noinline creator: () -> T,
): T = sharedViewModel(T::class, key, creator)

@Composable
fun <T : ScreenViewModel> sharedViewModel(
    vmClass: KClass<T>,
    key: String? = null,
    creator: () -> T,
): T {
    val hostViewModelStoreOwner = LocalScreenViewModelStoreOwner.current
    val screenRecord = LocalScreenRecord.current
    val screenViewModel = remember {
        hostViewModelStoreOwner.getViewModel(vmClass, key, creator)
    }
    ScreenStackStateCallback(screenRecord) {
        if (it == ScreenStackState.OUT_STACK) {
            screenViewModel.unBindRecord(screenRecord)
            if (screenViewModel.key != null) {
                hostViewModelStoreOwner.removeViewModel(screenViewModel::class, screenViewModel.key)
            }
            hostViewModelStoreOwner.removeViewModel(screenViewModel::class, null)
        } else if (it == ScreenStackState.IN_STACK) {
            screenViewModel.bindRecord(screenRecord)
        }
    }
    return screenViewModel
}

@Composable
inline fun <reified T : ScreenViewModel> viewModel(
    key: String? = null,
    noinline creator: () -> T,
): T = viewModel(T::class, key, creator)

@Composable
fun <T : ScreenViewModel> viewModel(
    vmClass: KClass<T>,
    key: String? = null,
    creator: () -> T,
): T {
    val screenRecord = LocalScreenRecord.current
    ScreenStackStateCallback(screenRecord) {
        if (it == ScreenStackState.OUT_STACK) {
            screenRecord.clearAllVM()
        }
    }
    return remember {
        screenRecord.getViewModel(vmClass, key, creator)
    }
}

private fun <T : ScreenViewModel> ScreenViewModelStoreOwner.getViewModel(
    vmClass: KClass<T>,
    key: String? = null,
    creator: () -> T,
): T {
    return getViewModelStore().getViewModel(vmClass, key, creator)
}

private inline fun <reified T : ScreenViewModel> ScreenViewModelStore.getViewModel(
    noinline creator: () -> T,
): T {
    return getViewModel(T::class, null, creator)
}

/**
 * @vmSignature
 * - vmSignature format
 * - no key -> "${vm class simple name}"
 * - has key -> "${vm class simple name} / ${key}"
 */
private fun <T : ScreenViewModel> ScreenViewModelStore.getViewModel(
    vmClass: KClass<T>,
    key: String?,
    creator: () -> T,
): T {
    val vmSignature = vmSignature(vmClass, key)
    val vm = this.getVM(vmSignature)
    if (vm != null && vmClass.isInstance(vm)) {
        @Suppress("UNCHECKED_CAST") return vm as T
    } else {
        if (vm != null) {
            ScreenLogger.debugLog("A same view model signature existed: $vmSignature ${vm::class}")
        }
    }
    val viewModel = creator()
    viewModel.key = key
    putVM(vmSignature, viewModel)
    return viewModel
}

private fun <T : ScreenViewModel> ScreenViewModelStoreOwner.removeViewModel(
    vmClass: KClass<T>,
    key: String? = null,
): T? {
    return getViewModelStore().removeViewModel(vmClass, key)
}

private inline fun <reified T : ScreenViewModel> ScreenViewModelStore.removeViewModel(): T? {
    return removeViewModel(T::class, null)
}

private fun <T : ScreenViewModel> ScreenViewModelStore.removeViewModel(
    vmClass: KClass<T>,
    key: String?,
): T? {
    val vmSignature = vmSignature(vmClass, key)
    val find = this.getVM(vmSignature)
    if (find != null && find.recordList.size > 0) {
        return null
    }
    val vm = this.removeVM(vmSignature)
    if (vm != null && vmClass.isInstance(vm)) {
        ScreenLogger.debugLog("Remove viewModel: $vmSignature ${vm::class}")
    } else {
        if (vm != null) {
            ScreenLogger.debugLog("Remove viewModel: something wrong - signature existed: $vmSignature ${vm::class}")
        }
    }
    @Suppress("UNCHECKED_CAST") return vm as? T
}
