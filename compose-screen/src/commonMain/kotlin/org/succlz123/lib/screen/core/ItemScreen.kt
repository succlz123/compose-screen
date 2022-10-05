package org.succlz123.lib.screen.core

import androidx.compose.runtime.Composable
import org.succlz123.lib.screen.*
import org.succlz123.lib.screen.transition.ScreenPopTransition
import org.succlz123.lib.screen.transition.ScreenPushTransition
import org.succlz123.lib.screen.transition.ScreenTransitionPopNone
import org.succlz123.lib.screen.transition.ScreenTransitionPushNone

open class ItemScreen(
    name: String,
    val replaceable: Boolean = false,
    val overlayIndex: Int = 0,
    val pushTransition: ScreenPushTransition = ScreenTransitionPushNone(),
    val popTransition: ScreenPopTransition = ScreenTransitionPopNone(),
    content: @Composable (ScreenRecord) -> Unit,
) : Screen(name, content) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        val otherName = (other as? ItemScreen)?.name
        if (name != otherName) {
            return false
        }
        if (replaceable != other.replaceable) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "ItemScreen: $name"
    }
}

