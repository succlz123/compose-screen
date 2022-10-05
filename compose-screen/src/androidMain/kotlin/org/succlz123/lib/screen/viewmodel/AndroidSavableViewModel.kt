package org.succlz123.lib.screen.viewmodel

import androidx.lifecycle.ViewModel
import org.succlz123.lib.screen.ScreenManager

class AndroidSavableViewModel : ViewModel(), ScreenSavableViewModel {
    private var screenManager: ScreenManager? = null

    private var globalViewModelOwner: ScreenViewModelStoreOwner? = null

    override fun bind(screenManager: ScreenManager) {
        this.screenManager = screenManager
    }

    override fun getScreenManager(): ScreenManager? {
        return screenManager
    }

    override fun bind(globalViewModelOwner: ScreenViewModelStoreOwner) {
        this.globalViewModelOwner = globalViewModelOwner
    }

    override fun getGlobalViewModelOwner(): ScreenViewModelStoreOwner? {
        return globalViewModelOwner
    }
}