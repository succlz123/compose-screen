package org.succlz123.lib.screen.lifecycle

class DefaultScreenLifecycle : ScreenLifecycle {
    private val observers = arrayListOf<ScreenLifecycleObserver>()
    private var state: ScreenLifecycle.State = ScreenLifecycle.State.UNKNOWN

    override fun setCurrentState(state: ScreenLifecycle.State) {
        this.state = state
        for (observer in observers) {
            observer.onChanged(state)
        }
    }

    override fun getCurrentState(): ScreenLifecycle.State {
        return state
    }

    override fun removeObserver(observer: ScreenLifecycleObserver): Boolean {
        return observers.remove(observer)
    }

    override fun removeAllObserver() {
        observers.clear()
    }

    override fun hasObserver(): Boolean {
        return observers.isNotEmpty()
    }

    override fun addObserver(observer: ScreenLifecycleObserver) {
        observers.add(observer)
        observer.onChanged(state)
    }

    override fun observerSize(): Int {
        return observers.size
    }

    override fun toString(): String {
        return "observerSize: ${observers.size}, observerList: ${observers.joinToString()}"
    }
}
