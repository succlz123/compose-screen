package org.succlz123.lib.screen.operation

import org.succlz123.lib.screen.ScreenLogger
import org.succlz123.lib.screen.ScreenRecord
import org.succlz123.lib.screen.ScreenRecordStack
import org.succlz123.lib.screen.isTargetRecord

object PopOperation {

    fun popGroupScreen(backStackList: ScreenRecordStack, result: Any?, popOptions: PopOptions?) {
        popCountGroupScreen(backStackList, 1, result, popOptions)
    }

    fun popPredicateGroupScreen(backStackList: ScreenRecordStack, result: Any?, popOptions: PopOptions) {
        val popPredicate = popOptions.popPredicate
        val recordList = backStackList.getRecordList()
        // count > 1
        if (popPredicate != null) {
            var count = 0
            for (i in recordList.size - 1 downTo 0) {
                val record = recordList[i]
                if (popPredicate.apply(record.screen)) {
                    break
                }
                count++
            }
            if (count != 0) {
                popCountGroupScreen(backStackList, count, result, popOptions)
            } else {
                ScreenLogger.debugLog("PopOperation: something may intercept the pop operation")
            }
        } else {
            popCountGroupScreen(backStackList, 1, result, popOptions)
        }
    }

    fun popToGroupScreen(
        backStackList: ScreenRecordStack, screenName: String, screenKey: String?, result: Any?, popOptions: PopOptions?
    ) {
        val recordList = backStackList.getRecordList()
        var returnRecord: ScreenRecord? = null
        var popCount = 0
        for (i in recordList.size - 1 downTo 0) {
            val record = recordList[i]
            if (record.isTargetRecord(screenName, screenKey)) {
                returnRecord = record
                break
            }
            popCount++
        }
        requireNotNull(returnRecord) { "Cant find $screenName in backStack" }
        if (popCount == 0) {
            return
        }
        popCountGroupScreen(backStackList, popCount, result, popOptions)
    }

    fun popToRootGroupScreen(backStackList: ScreenRecordStack, result: Any?, popOptions: PopOptions?) {
        val recordList = backStackList.getRecordList()
        val popCount = recordList.size - 1
        if (popCount == 0) {
            return
        }
        popCountGroupScreen(backStackList, popCount, result, popOptions)
    }

    fun popCountGroupScreen(backStackList: ScreenRecordStack, popCount: Int, result: Any?, popOptions: PopOptions?) {
        val recordList = backStackList.getRecordList()
        val currentGroupRecord = recordList.last()

        if (popCount >= recordList.size) {
            if (recordList.size > 1) {
                popCountGroupScreen(backStackList, recordList.size - 1, result, popOptions)
            }
            return
        }

        val destroyRecordList = ArrayList<ScreenRecord>()
        val recordListSize = recordList.size
        var isLatestIndex = recordListSize
        recordList.forEachIndexed { index, record ->
            if (index in (recordListSize - popCount) until recordListSize) {
                destroyRecordList.add(record)
                if (index < isLatestIndex) {
                    isLatestIndex = index
                }
            } else if (record.removeFlag) {
                destroyRecordList.add(record)
            }
        }
        val returnRecord = recordList[isLatestIndex - 1]

        if (currentGroupRecord.popFinalInterceptor?.invoke(recordList, destroyRecordList, returnRecord) == true) {
            return
        }

        returnRecord.result = result

        for (removeRecord in destroyRecordList) {
            ScreenStackState.moveState(removeRecord, ScreenStackState.OUT_STACK)
            backStackList.remove(removeRecord)
        }
        ScreenStackState.moveState(returnRecord, ScreenStackState.IN_STACK)
    }

    fun popAllPopupScreen(backStackList: ScreenRecordStack) {
        val currentRecord = backStackList.getCurrentGroupRecord() ?: return
        for (curPopupRecord in currentRecord.popupScreenList) {
            ScreenStackState.moveState(curPopupRecord, ScreenStackState.OUT_STACK)
        }
        currentRecord.popupScreenList.clear()
    }

    fun popCurPopupScreen(backStackList: ScreenRecordStack) {
        val currentRecord = backStackList.getCurrentGroupRecord() ?: return
        val curPopupRecord = currentRecord.getCurrentPopupRecord()
        if (curPopupRecord != null) {
            ScreenStackState.moveState(curPopupRecord, ScreenStackState.OUT_STACK)
            currentRecord.removePopupRecord(curPopupRecord)
        }
    }
}