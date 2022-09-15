package org.succlz123.lib.screen.operation

import org.succlz123.lib.screen.ScreenLogger
import org.succlz123.lib.screen.ScreenRecord

enum class ScreenStackState {
    UNKNOWN, IN_STACK, IN_STACK_REMOVED_FLAG, OUT_STACK;

    companion object {

        internal fun moveState(record: ScreenRecord, state: ScreenStackState) {
            record.stackStateFlow.value = state
            when (state) {
                UNKNOWN -> {}
                IN_STACK -> {
                    ScreenLogger.debugLog("Stack is in: $record, remove: false")
                }

                IN_STACK_REMOVED_FLAG -> {
                    ScreenLogger.debugLog("Stack is in: $record, remove: true")
                }

                OUT_STACK -> {
                    ScreenLogger.debugLog("Stack is out: $record")
                }
            }
        }
    }
}