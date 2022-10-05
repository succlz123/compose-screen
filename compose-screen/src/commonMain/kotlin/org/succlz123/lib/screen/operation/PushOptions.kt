package org.succlz123.lib.screen.operation

import org.succlz123.lib.screen.transition.ScreenPopTransition
import org.succlz123.lib.screen.transition.ScreenPushTransition
import org.succlz123.lib.screen.transition.ScreenTransitionPopNone
import org.succlz123.lib.screen.transition.ScreenTransitionPushNone
import org.succlz123.lib.screen.core.Screen

class PushOptions(
    var pushTransition: ScreenPushTransition = ScreenTransitionPushNone(),
    var popTransition: ScreenPopTransition = ScreenTransitionPopNone(),
    var removePredicate: Predicate<Screen>? = null
) {

    open class CountPredicate(private var mCount: Int) : Predicate<Screen> {

        override fun apply(screen: Screen): Boolean {
            if (mCount <= 0) {
                return false
            }
            mCount--
            return true
        }
    }

    class ClearTaskPredicate : Predicate<Screen> {

        override fun apply(screen: Screen): Boolean {
            return true
        }
    }

    class ReplacePredicate : CountPredicate(1)

    class PopItselfPredicate : Predicate<Screen> {
        private var finish = false

        override fun apply(screen: Screen): Boolean {
            if (!finish) {
                finish = true
                return true
            }
            return false
        }
    }

    class SingleTopPredicate(private val screenName: String) : Predicate<Screen> {
        private var finish = false

        override fun apply(screen: Screen): Boolean {
            if (!finish && screen.name == screenName) {
                finish = true
                return true
            } else {
                finish = true
            }
            return false
        }
    }

    class SingleTaskPredicate(private val screenName: String) : Predicate<Screen> {
        private var finish = false

        override fun apply(screen: Screen): Boolean {
            if (screen.name != screenName) {
                return true
            }
            if (!finish) {
                finish = true
                return true
            }
            return false
        }
    }

    class RemoveAnyPredicate(private val screenName: String, private val key: String? = null) : Predicate<Screen> {

        override fun apply(screen: Screen): Boolean {
            return screen.name == screenName
        }
    }
}