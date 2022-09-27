package org.succlz123.app.screen.ui.main

import AppToolbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.succlz123.lib.screen.ScreenNavigator

@Composable
fun MainScreen(screenNavigator: ScreenNavigator) {
    Column {
        AppToolbar(screenNavigator, "Compose Screen Demo", false)
        LazyColumn(modifier = Modifier.fillMaxSize().padding(24.dp, 0.dp, 24.dp, 24.dp)) {
            item {
                Text(
                    modifier = Modifier, text = "Push Usage:", fontSize = 14.sp, color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    NavigationItemView(modifier = Modifier.weight(1f), "Push & Pop\n导航和返回") {
                        screenNavigator.push(Manifest.PushPopScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "SingleTop\n栈顶复用") {
                        screenNavigator.push(Manifest.PushSingleTopRootScreen)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    NavigationItemView(modifier = Modifier.weight(1f), "SingleTask\n栈内复用") {
                        screenNavigator.push(Manifest.PushSingleTaskRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "Clear Task\nPush时清空栈") {
                        screenNavigator.push(Manifest.PushClearTaskRootScreen)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    NavigationItemView(
                        modifier = Modifier.weight(1f), "Close Current Screen\nPush同时移除当前页面"
                    ) {
                        screenNavigator.push(Manifest.PushCloseItselfRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "Remove Any Screen\n移除中间任意的页面") {
                        screenNavigator.push(Manifest.PushRemoveAnyRootScreen)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier, text = "Pop Usage:", fontSize = 14.sp, color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    NavigationItemView(modifier = Modifier.weight(1f), "Pop To\n返回某一页面") {
                        screenNavigator.push(Manifest.PopToRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "On Result\n返回获得结果") {
                        screenNavigator.push(Manifest.PopOnResultRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "BackInterceptor\n返回拦截") {
                        screenNavigator.push(Manifest.PopInterceptorScreen)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.padding(6.dp), text = "Popup Usage:", fontSize = 14.sp, color = Color.Gray
                )
                Row {
                    NavigationItemView(modifier = Modifier.weight(1f), "Dialog\n弹窗") {
                        screenNavigator.push(Manifest.DialogRootPopupScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "Toast\n吐司") {
                        screenNavigator.push(Manifest.ToastRootPopupScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "PopWindow\n浮动窗口") {
                        screenNavigator.push(Manifest.PopupWindowRootPopupScreen)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.padding(6.dp), text = "Other Usage:", fontSize = 14.sp, color = Color.Gray
                )
                Row {
                    NavigationItemView(modifier = Modifier.weight(1f), "Screen Lifecycle\n生命周期") {
                        screenNavigator.push(Manifest.LifecycleRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "Screen ViewModel\n视图模块") {
                        screenNavigator.push(Manifest.ViewModelRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "Animation\n转场动画") {
                        screenNavigator.push(Manifest.AnimationRootScreen)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    NavigationItemView(modifier = Modifier.weight(1f), "Adaptive Layouts\n布局适配") {
                        screenNavigator.push(Manifest.AdaptiveLayoutsRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "Deeplink\n应用超链接") {
                        screenNavigator.push(Manifest.DeeplinkRootScreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    NavigationItemView(modifier = Modifier.weight(1f), "BackPressed\n响应宿主系统返回") {
                        screenNavigator.push(Manifest.BackPressedRootScreen)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationItemView(modifier: Modifier = Modifier, text: String, click: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        elevation = 0.dp,
        backgroundColor = Color.White,
        onClick = click
    ) {
        Text(
            modifier = Modifier.padding(6.dp, 12.dp), textAlign = TextAlign.Center, text = text, fontSize = 12.sp
        )
    }
}
