package org.succlz123.lib.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import org.succlz123.lib.screen.core.Screen

class ScreenRecordStack {
    private var backStackList = mutableStateListOf<ScreenRecord>()

    val toastRecord = mutableStateOf<ScreenRecord?>(null)

    fun push(record: ScreenRecord) {
        backStackList.add(record)
    }

    fun pop() {
        backStackList.removeAt(backStackList.size - 1)
    }

    fun remove(record: ScreenRecord) {
        backStackList.remove(record)
    }

    fun getCurrentGroupRecord(list: List<ScreenRecord>): ScreenRecord? {
        return if (list.isNotEmpty()) {
            list[list.size - 1]
        } else {
            null
        }
    }

    fun getCurrentGroupRecord(): ScreenRecord? {
        return if (backStackList.size > 0) {
            backStackList[backStackList.size - 1]
        } else {
            null
        }
    }

    fun getRecordList(): List<ScreenRecord> {
        return backStackList
    }

    fun clearStackList() {
        backStackList.clear()
    }

    fun getRecordByScreen(screen: Screen): ScreenRecord? {
        for (record in backStackList) {
            if (record.screen == screen) {
                return record
            }
        }
        return null
    }

    fun getPreviousScreen(): ScreenRecord? {
        return if (backStackList.size < 2) {
            null
        } else {
            backStackList[backStackList.size - 2]
        }
    }

    fun canPop(): Boolean {
        return (backStackList.size > 1)
                || (backStackList.lastOrNull()?.popupScreenList?.isNotEmpty() == true)
                || (backStackList.lastOrNull()?.popupWindowRecord?.value != null)
    }

    fun getStackHistory(): String {
        val stringBuilder = StringBuilder("Screen history:")
        for (record in backStackList) {
            stringBuilder.append("\n-> " + record.screen.name + " - key: " + record.arguments.screenKey())
        }
        return stringBuilder.toString()
    }
}