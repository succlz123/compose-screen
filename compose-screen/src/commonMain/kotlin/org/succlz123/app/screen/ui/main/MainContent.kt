package org.succlz123.app.screen.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.succlz123.app.screen.ui.animation.AnimationRootScreen
import org.succlz123.app.screen.ui.animation.AnimationScreen
import org.succlz123.app.screen.ui.back.BackPressedRootScreen
import org.succlz123.app.screen.ui.deeplink.DeeplinkRootScreen
import org.succlz123.app.screen.ui.deeplink.DeeplinkScreen
import org.succlz123.app.screen.ui.lifecycle.LifecycleRootScreen
import org.succlz123.app.screen.ui.lifecycle.LifecycleScreen
import org.succlz123.app.screen.ui.pop.*
import org.succlz123.app.screen.ui.popup.DialogPopupScreen
import org.succlz123.app.screen.ui.popup.DialogRootPopupScreen
import org.succlz123.app.screen.ui.popup.PopupWindowRootPopupScreen
import org.succlz123.app.screen.ui.popup.ToastRootPopupScreen
import org.succlz123.app.screen.ui.push.*
import org.succlz123.app.screen.ui.viewmodel.ViewModelRootScreen
import org.succlz123.app.screen.ui.viewmodel.ViewModelScreen
import org.succlz123.app.screen.ui.window.AdaptiveLayoutsRootScreen
import org.succlz123.lib.screen.ScreenHost
import org.succlz123.lib.screen.rememberScreenNavigator

val colorBg = Color(0xFFE6E6E6)

@Composable
fun DemoMainContent() {
    val screenNavigator = rememberScreenNavigator()
    Box(modifier = Modifier.fillMaxSize().background(colorBg)) {
        ScreenHost(screenNavigator = screenNavigator, rootScreenName = Manifest.MainScreen) {
            // main
            groupScreen(screenName = (Manifest.MainScreen)) {
                MainScreen(screenNavigator)
            }
            // push
            groupScreen(screenName = Manifest.PushPopScreen) {
                PushPopScreen()
            }
            // push single top
            groupScreen(screenName = Manifest.PushSingleTopRootScreen) {
                PushSingleTopRootScreen()
            }
            groupScreen(screenName = Manifest.PushSingleTopScreen) {
                PushSingleTopScreen()
            }
            // push single task
            groupScreen(screenName = Manifest.PushSingleTaskRootScreen) {
                PushSingleTaskRootScreen()
            }
            groupScreen(screenName = Manifest.PushSingleTaskScreen) {
                PushSingleTaskScreen()
            }
            // push clear task
            groupScreen(screenName = Manifest.PushClearTaskRootScreen) {
                PushClearTaskRootScreen()
            }
            groupScreen(screenName = Manifest.PushClearTaskScreen) {
                PushClearTaskScreen()
            }
            // push close current
            groupScreen(screenName = Manifest.PushCloseItselfRootScreen) {
                PushCloseItselfRootScreen()
            }
            groupScreen(screenName = Manifest.PushCloseItselfScreen) {
                PushCloseItselfScreen()
            }
            // push remove any
            groupScreen(screenName = Manifest.PushRemoveAnyRootScreen) {
                PushRemoveAnyRootScreen()
            }
            groupScreen(screenName = Manifest.PushRemoveAnyScreen) {
                PushRemoveAnyScreen()
            }
            // pop to root
            groupScreen(screenName = Manifest.PopToRootScreen) {
                PopToRootScreen()
            }
            groupScreen(screenName = Manifest.PopToScreen) {
                PopToScreen()
            }
            // pop result
            groupScreen(screenName = Manifest.PopOnResultRootScreen) {
                PopOnResultRootScreen()
            }
            groupScreen(screenName = Manifest.PopOnResultScreen) {
                PopOnResultScreen()
            }
            // pop interceptor
            groupScreen(screenName = Manifest.PopInterceptorScreen) {
                PopInterceptorScreen()
            }
            // popup
            groupScreen(screenName = Manifest.DialogRootPopupScreen) {
                DialogRootPopupScreen()
            }
            itemScreen(screenName = Manifest.DialogPopupScreen) {
                DialogPopupScreen()
            }
            groupScreen(screenName = Manifest.ToastRootPopupScreen) {
                ToastRootPopupScreen()
            }
            groupScreen(screenName = Manifest.PopupWindowRootPopupScreen) {
                PopupWindowRootPopupScreen()
            }
            // other
            groupScreen(screenName = Manifest.LifecycleRootScreen) {
                LifecycleRootScreen()
            }
            groupScreen(screenName = Manifest.LifecycleScreen) {
                LifecycleScreen()
            }
            groupScreen(screenName = Manifest.ViewModelRootScreen) {
                ViewModelRootScreen()
            }
            groupScreen(screenName = Manifest.ViewModelScreen) {
                ViewModelScreen()
            }
            groupScreen(screenName = Manifest.AnimationRootScreen) {
                AnimationRootScreen()
            }
            groupScreen(screenName = Manifest.AnimationScreen) {
                AnimationScreen()
            }
            groupScreen(screenName = Manifest.AdaptiveLayoutsRootScreen) {
                AdaptiveLayoutsRootScreen()
            }
            groupScreen(screenName = Manifest.DeeplinkRootScreen) {
                DeeplinkRootScreen()
            }
            groupScreen(
                screenName = Manifest.DeeplinkScreen, deepLinks = arrayListOf("https://www.baidu.com/test/text")
            ) {
                DeeplinkScreen()
            }
            groupScreen(screenName = Manifest.BackPressedRootScreen) {
                BackPressedRootScreen()
            }
        }
    }
}