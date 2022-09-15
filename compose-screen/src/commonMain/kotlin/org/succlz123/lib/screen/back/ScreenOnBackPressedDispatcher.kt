package org.succlz123.lib.screen.back

class ScreenOnBackPressedDispatcher(private val enableChanger: (Boolean) -> Unit) {
    private var enable: Boolean = false

    private var onBackPressedCallback: (() -> Unit)? = null

    fun isEnable(): Boolean {
        return enable
    }

    fun setEnable(isEnable: Boolean) {
        enableChanger.invoke(isEnable)
        enable = isEnable
    }

    fun setRootCallback(cb: (() -> Unit)? = null) {
        onBackPressedCallback = cb
    }

    fun onBackPressed() {
        onBackPressedCallback?.invoke()
    }
}