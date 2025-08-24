package org.succlz123.lib.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.succlz123.lib.screen.ext.toast.ARGS_TOAST_TIME_SHORT
import org.succlz123.lib.screen.ext.toast.KEY_TOAST_MSG
import org.succlz123.lib.screen.ext.toast.KEY_TOAST_TIME
import org.succlz123.lib.screen.operation.PopOptions
import org.succlz123.lib.screen.operation.PushOptions

@Composable
fun rememberScreenNavigator(): ScreenNavigator {
    return remember { ScreenNavigator() }
}

class ScreenNavigator {
    var screenManager: ScreenManager? = null

    internal fun init(manager: ScreenManager) {
        this.screenManager = manager
    }

    fun push(
        screenName: String,
        screenKey: String? = null,
        arguments: ScreenArgs = ScreenArgs(),
        pushOptions: PushOptions? = null
    ) {
        arguments.putScreenKey(screenKey)
        screenManager?.push(screenName, arguments, pushOptions)
    }

    fun pushDeeplink(
        deeplink: String,
        screenKey: String? = null,
        arguments: ScreenArgs = ScreenArgs(),
        pushOptions: PushOptions? = null
    ) {
        arguments.putDeeplink(deeplink)
        arguments.putScreenKey(screenKey)
        screenManager?.pushDeeplink(deeplink, arguments, pushOptions)
    }

    fun toast(msg: String, time: Long = ARGS_TOAST_TIME_SHORT, content: (@Composable (ScreenRecord) -> Unit)? = null) {
        screenManager?.toast(ScreenArgs.putValue(KEY_TOAST_MSG, msg).putValue(KEY_TOAST_TIME, time), content)
    }

    fun toast(arguments: ScreenArgs = ScreenArgs(), content: (@Composable (ScreenRecord) -> Unit)? = null) {
        screenManager?.toast(arguments, content)
    }

    fun cancelToast() {
        screenManager?.cancelToast()
    }

    fun popupWindow(arguments: ScreenArgs = ScreenArgs(), content: (@Composable () -> Unit)? = null) {
        screenManager?.popupWindow(arguments, content)
    }

    fun cancelPopupWindow() {
        screenManager?.cancelPopupWindow()
    }

    fun remove(screenName: String, screenKey: String? = null) {
        screenManager?.remove(screenName, screenKey)
    }

    fun pop(
        result: Any? = null,
        finishCurGroupScreen: Boolean = false,
        finishAllPopupScreen: Boolean = false,
        popOptions: PopOptions? = null
    ) {
        screenManager?.pop(result, finishCurGroupScreen, finishAllPopupScreen, popOptions)
    }

    fun popTo(screenName: String, screenKey: String?, result: Any? = null) {
        screenManager?.popTo(screenName, screenKey, result)
    }

    fun popToRoot(result: Any? = null) {
        screenManager?.popToRoot(result)
    }

    fun popPopupScreen() {
        screenManager?.popPopupScreen()
    }

    fun popAllPopupScreen() {
        screenManager?.popAllPopupScreen()
    }

    fun exitScreen() {
        screenManager?.exitScreen()
    }

    fun getStackHistory(): String {
        return screenManager?.getStackHistory() ?: "Screen: Unknown Stack"
    }

    fun getHostVMSize(): Int {
        return screenManager?.keysVM()?.size ?: -1
    }
}
