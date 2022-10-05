package org.succlz123.lib.screen.viewmodel

import org.succlz123.lib.screen.ScreenManager

interface ScreenSavableViewModel {

    fun bind(screenManager: ScreenManager)

    fun getScreenManager(): ScreenManager?

    fun bind(globalViewModelOwner: ScreenViewModelStoreOwner)

    fun getGlobalViewModelOwner(): ScreenViewModelStoreOwner?

}