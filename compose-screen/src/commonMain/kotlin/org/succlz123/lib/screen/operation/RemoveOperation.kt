package org.succlz123.lib.screen.operation

import org.succlz123.lib.screen.ScreenRecordStack
import org.succlz123.lib.screen.isTargetRecord

object RemoveOperation {

    fun remove(backStackList: ScreenRecordStack, screenName: String, screenKey: String?) {
        // if the screen is current, remove it directly
        if (backStackList.getCurrentGroupRecord().isTargetRecord(screenName, screenKey)) {
            PopOperation.popGroupScreen(backStackList, null, null)
            return
        }
        // if the screen is in the stack, add a remove flag to this screen
        val list = backStackList.getRecordList()
        for (i in list.size - 1 downTo 0) {
            val record = list[i]
            if (record.isTargetRecord(screenName, screenKey)) {
                record.removeFlag = true
                ScreenStackState.moveState(record, ScreenStackState.IN_STACK_REMOVED_FLAG)
                break
            }
        }
    }
}