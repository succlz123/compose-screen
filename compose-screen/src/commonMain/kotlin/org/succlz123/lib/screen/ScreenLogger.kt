package org.succlz123.lib.screen

object ScreenLogger {
    const val TAG = "ScreenLogger"

    var isDebug: Boolean = false

    var LOGGER_FILTER = ""
//    var LOGGER_FILTER = "remove viewModel"

    fun debugLog(msg: String?) {
        if (msg == null) {
            return
        }
        if (isDebug) {
            if (LOGGER_FILTER.isEmpty()) {
                println("$TAG: $msg")
            } else if (msg.contains(LOGGER_FILTER)) {
                println("$TAG: $msg")
            }
        }
    }
}