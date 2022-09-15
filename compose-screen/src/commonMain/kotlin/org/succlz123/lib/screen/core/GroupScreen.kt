package org.succlz123.lib.screen.core

import androidx.compose.runtime.Composable
import org.succlz123.lib.screen.ScreenPopTransition
import org.succlz123.lib.screen.ScreenPushTransition
import org.succlz123.lib.screen.ScreenRecord

class GroupScreen(
    name: String,
    val deepLinks: List<String>,
    val pushTransition: ScreenPushTransition,
    val popTransition: ScreenPopTransition,
    content: @Composable (ScreenRecord) -> Unit,
) : Screen(name, content) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        val otherName = (other as? GroupScreen)?.name
        if (name != otherName) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "GroupScreen: $name"
    }
}
