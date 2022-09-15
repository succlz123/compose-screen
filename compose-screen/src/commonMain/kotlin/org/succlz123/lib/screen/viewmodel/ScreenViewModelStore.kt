package org.succlz123.lib.screen.viewmodel

open class ScreenViewModelStore {
    private val vmMap = hashMapOf<String, ScreenViewModel>()

    fun putVM(vmSignature: String, viewModel: ScreenViewModel) {
        val vm = vmMap.put(vmSignature, viewModel)
        vm?.clear()
    }

    fun getVM(vmSignature: String): ScreenViewModel? {
        return vmMap[vmSignature]
    }

    fun removeVM(vmSignature: String): ScreenViewModel? {
        val vm = vmMap.remove(vmSignature)
        vm?.clear()
        return vm
    }

    fun keysVM(): Set<String> {
        return HashSet(vmMap.keys)
    }

    fun getAllVM(): MutableList<ScreenViewModel> {
        return vmMap.values.toMutableList()
    }

    fun clearAllVM() {
        for (vm in vmMap.values) {
            vm.clear()
        }
        vmMap.clear()
    }
}
