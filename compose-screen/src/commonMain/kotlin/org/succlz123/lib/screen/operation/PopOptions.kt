package org.succlz123.lib.screen.operation

import org.succlz123.lib.screen.core.Screen

/**
 * @param popPredicate
 *
 */
class PopOptions(var popPredicate: Predicate<Screen>? = null) {

    class CountUtilPredicate(private var count: Int) : Predicate<Screen> {

        override fun apply(screen: Screen): Boolean {
            if (count <= 0) {
                return true
            }
            count--
            return false
        }
    }
}