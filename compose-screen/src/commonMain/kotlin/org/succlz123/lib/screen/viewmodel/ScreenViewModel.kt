package org.succlz123.lib.screen.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.succlz123.lib.screen.ScreenRecord

abstract class ScreenViewModel {
    internal var recordList = ArrayList<ScreenRecord>()

    var key: String? = null

    private val job = SupervisorJob()

    val viewModelScope: CoroutineScope = CoroutineScope(job)

    private var canceled = false

    internal fun bindRecord(record: ScreenRecord) {
        if (!recordList.contains(record)) {
            recordList.add(record)
        }
    }

    internal fun unBindRecord(record: ScreenRecord) {
        if (recordList.contains(record)) {
            recordList.remove(record)
        }
    }

    open fun clear() {
        job.cancel()
        canceled = true
        onCleared()
    }

    protected open fun onCleared() {}

    override fun toString(): String {
        return "ScreenViewModel: class = ${this::class}, key = $key, sharedCount = ${recordList.size}, isCanceled = $canceled"
    }
}
