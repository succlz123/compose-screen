package org.succlz123.lib.screen.viewmodel

import org.succlz123.lib.screen.ScreenManager

class ScreenDefaultEmptySavableViewModel : ScreenSavableViewModel {

    override fun bind(screenManager: ScreenManager) {
    }

    override fun bind(globalViewModelOwner: ScreenViewModelStoreOwner) {
    }

    override fun getScreenManager(): ScreenManager? {
        return null
    }

    override fun getGlobalViewModelOwner(): ScreenViewModelStoreOwner? {
        return null
    }
}