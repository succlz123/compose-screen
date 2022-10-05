package org.succlz123.lib.screen

import androidx.compose.runtime.Composable
import org.succlz123.lib.screen.core.GroupScreen
import org.succlz123.lib.screen.core.ItemScreen
import org.succlz123.lib.screen.core.Screen
import org.succlz123.lib.screen.ext.popupwindow.ScreenPopupWindowPopupScreen
import org.succlz123.lib.screen.ext.toast.ScreenToastPopupScreen
import org.succlz123.lib.screen.transition.ScreenPopTransition
import org.succlz123.lib.screen.transition.ScreenPushTransition
import org.succlz123.lib.screen.transition.ScreenTransitionPopNone
import org.succlz123.lib.screen.transition.ScreenTransitionPushNone

class ScreenCollector(private val rootScreenName: String) {
    internal val screenList = arrayListOf<Screen>()

    fun init(): ScreenCollector {
        if (rootScreenName.isEmpty() && screenList.isEmpty()) {
            error("${ScreenLogger.TAG}: Please add a screen to the navigation!")
        } else {
            screenList.find { it.name == rootScreenName } ?: error("${ScreenLogger.TAG}: Can't find the root screen!")
        }
        initToastScreen()
        initPopupWindowScreen()
        return this
    }

    fun groupScreen(
        screenName: String,
        deepLinks: List<String> = emptyList(),
        pushTransition: ScreenPushTransition = ScreenTransitionPushNone(),
        popTransition: ScreenPopTransition = ScreenTransitionPopNone(),
        content: @Composable (ScreenRecord) -> Unit,
    ) {
        addScreen(
            GroupScreen(
                name = screenName,
                deepLinks = deepLinks,
                pushTransition = pushTransition,
                popTransition = popTransition,
                content = content,
            )
        )
    }

    fun itemScreen(
        screenName: String,
        replaceable: Boolean = false,
        pushTransition: ScreenPushTransition = ScreenTransitionPushNone(),
        popTransition: ScreenPopTransition = ScreenTransitionPopNone(),
        content: @Composable (ScreenRecord) -> Unit
    ) {
        addScreen(
            ItemScreen(
                name = screenName,
                replaceable = replaceable,
                pushTransition = pushTransition,
                popTransition = popTransition,
                content = content
            )
        )
    }

    fun addScreen(screen: Screen) {
        if (screenList.contains(screen)) {
            error("${ScreenLogger.TAG}: The >>> ${screen.name} <<< is already exist in the screen manager!")
        }
        screenList.add(screen)
    }

    private fun initToastScreen() {
        addScreen(ItemScreen(name = ScreenToastPopupScreen) {
            ScreenToastPopupScreen()
        })
    }

    private fun initPopupWindowScreen() {
        addScreen(ItemScreen(name = ScreenPopupWindowPopupScreen) {
            ScreenPopupWindowPopupScreen()
        })
    }
}
