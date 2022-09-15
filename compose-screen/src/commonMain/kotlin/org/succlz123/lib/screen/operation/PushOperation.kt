package org.succlz123.lib.screen.operation

import org.succlz123.lib.screen.ScreenArgs
import org.succlz123.lib.screen.ScreenLogger
import org.succlz123.lib.screen.ScreenManager
import org.succlz123.lib.screen.ScreenRecord
import org.succlz123.lib.screen.core.GroupScreen
import org.succlz123.lib.screen.core.ItemScreen
import org.succlz123.lib.screen.core.Screen

object PushOperation {

    fun pushDeeplink(manager: ScreenManager, deeplink: String, arguments: ScreenArgs, pushOptions: PushOptions?) {
        val deeplinkHostAndPath = deeplink.split("?").firstOrNull()
        if (deeplinkHostAndPath.isNullOrEmpty()) {
            return
        }
        val findScreen = manager.screenCollector.screenList.find {
            if (it is GroupScreen) {
                var isThisDeeplinkScreen = false
                for (link in it.deepLinks) {
                    if (deeplinkHostAndPath == link.split("?").firstOrNull()) {
                        isThisDeeplinkScreen = true
                        break
                    }
                }
                isThisDeeplinkScreen
            } else {
                false
            }
        }
        if (findScreen == null) {
            ScreenLogger.debugLog("Push Operation: Can't find the deeplink destination - $deeplink")
            return
        }
        push(manager, findScreen, arguments, pushOptions)
    }

    fun push(manager: ScreenManager, screen: Screen, arguments: ScreenArgs, pushOptions: PushOptions?) {
        val removePredicate = pushOptions?.removePredicate
        var isTaskRootReplaced = false
//        val destroyList = ArrayList<ScreenRecord>()
        if (removePredicate != null) {
            val previousRecordList = manager.recordStack.getRecordList()
            for (i in previousRecordList.indices.reversed()) {
                val oldRecord = previousRecordList[i]
                val oldScreen = oldRecord.screen
                if (!removePredicate.apply(oldScreen)) {
                    continue
                }
//                destroyList.add(oldRecord)
                ScreenStackState.moveState(oldRecord, ScreenStackState.OUT_STACK)
                manager.recordStack.remove(oldRecord)
                if (i == 0) {
                    isTaskRootReplaced = true
                }
            }
        }
//        if (removePredicate != null) {
//            val previousRecordList = manager.recordStack.getRecordList()
//            var removeIndex = -1
//            for (i in previousRecordList.indices.reversed()) {
//                val oldRecord = previousRecordList[i]
//                val oldScreen = oldRecord.screen
//                if (!removePredicate.apply(oldScreen)) {
//                    removeIndex = i
//                    if (i == 0) {
//                        isTaskRootReplaced = true
//                    }
//                    break
//                }
//            }
//            if (removeIndex != -1) {
//                val removeRecordList = previousRecordList.subList(removeIndex, previousRecordList.size - 1)
//                for (removeRecord in removeRecordList) {
//                    ScreenStackState.moveState(removeRecord, ScreenStackState.OUT_STACK)
//                    manager.recordStack.remove(removeRecord)
//                }
//            }
//        }
        when (screen) {
            is GroupScreen -> {
                val curSaveId = (manager.recordSaveId++) * manager.recordSaveIdCoe
                val record =
                    ScreenRecord.newInstance(screen, curSaveId, manager.hostLifecycle, arguments, pushOptions)
                ScreenStackState.moveState(record, ScreenStackState.IN_STACK)
                manager.recordStack.push(record)
            }

            is ItemScreen -> {
                val currentGroupRecord = manager.recordStack.getCurrentGroupRecord() ?: return
                val currentPopupRecord = currentGroupRecord.getCurrentPopupRecord()
                val curPopupSaveId = if (currentPopupRecord != null) {
                    currentPopupRecord.saveId + 1
                } else {
                    currentGroupRecord.saveId + 1
                }
                if (screen.replaceable) {
                    val popups = currentGroupRecord.getAllPopupScreen()
                    val destroyPopupList = popups.filter { it.screen == screen }
                    for (screenRecord in destroyPopupList) {
                        ScreenStackState.moveState(screenRecord, ScreenStackState.OUT_STACK)
                        currentGroupRecord.removePopupRecord(screenRecord)
                    }
                }
                val record =
                    ScreenRecord.newInstance(screen, curPopupSaveId, manager.hostLifecycle, arguments, pushOptions)
                ScreenStackState.moveState(record, ScreenStackState.IN_STACK)
                currentGroupRecord.popupScreenList.add(record)
            }
        }
    }
}