package org.succlz123.lib.screen.lifecycle

interface ScreenLifecycle {

    fun addObserver(observer: ScreenLifecycleObserver)

    fun removeObserver(observer: ScreenLifecycleObserver): Boolean

    fun removeAllObserver()

    fun hasObserver(): Boolean

    fun setCurrentState(state: State)

    fun getCurrentState(): State

    fun observerSize(): Int

    enum class State {
        UNKNOWN,
        DESTROYED,
        CREATED,
        PAUSED,
        RESUMED;

        fun isAtLeast(state: State): Boolean {
            return compareTo(state) >= 0
        }
    }
}
