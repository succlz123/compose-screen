package org.succlz123.lib.screen.back;

interface ScreenOnBackPressedDispatcherOwner {

    fun get(): ScreenOnBackPressedDispatcher

    fun sendBackPressedToSystem()
}
