package org.succlz123.app.screen.ui.popup

import AppToolbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noRippleClickable
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.ScreenArgs
import org.succlz123.lib.screen.ext.popupwindow.PopupWindowLayout
import org.succlz123.lib.screen.ext.toast.KEY_TOAST_MSG

@Composable
fun PopupWindowRootPopupScreen() {
    val screenNavigator = LocalScreenNavigator.current
    Column(modifier = Modifier.fillMaxSize()) {
        AppToolbar(screenNavigator, Manifest.PopupWindowRootPopupScreen, true)
        Card(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            shape = RoundedCornerShape(6.dp),
            elevation = 0.dp,
            backgroundColor = Color.White
        ) {
            val displayContent = @Composable {
                Card(
                    modifier = Modifier.padding(12.dp),
                    shape = RoundedCornerShape(8.dp), elevation = 3.dp, backgroundColor = Color.White
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            modifier = Modifier.padding(24.dp, 8.dp).noRippleClickable {
                                screenNavigator.toast(
                                    arguments = ScreenArgs.putValue(
                                        KEY_TOAST_MSG, "Recommend Click and cancel this PopupWindow"
                                    )
                                )
                                screenNavigator.cancelPopupWindow()
                            }, text = "Recommend", fontSize = 16.sp, color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.padding(24.dp, 8.dp).noRippleClickable {
                                screenNavigator.toast(
                                    arguments = ScreenArgs.putValue(
                                        KEY_TOAST_MSG, "Hot Click"
                                    )
                                )
                            }, text = "Hot", fontSize = 16.sp, color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.padding(24.dp, 8.dp).noRippleClickable {
                                screenNavigator.toast(
                                    arguments = ScreenArgs.putValue(
                                        KEY_TOAST_MSG, "Viewed Click"
                                    )
                                )
                            }, text = "Viewed", fontSize = 16.sp, color = Color.Black
                        )
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize().padding(18.dp)) {
                PopupWindowLayout(
                    modifier = Modifier.align(Alignment.TopStart),
                    displayContent = displayContent,
                    clickableContent = {
                        Card(
                            shape = RoundedCornerShape(8.dp), elevation = 3.dp, backgroundColor = Color.White
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    modifier = Modifier.padding(24.dp, 8.dp),
                                    text = "TopStart",
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    })
                PopupWindowLayout(
                    modifier = Modifier.align(Alignment.TopEnd),
                    displayContent = displayContent,
                    clickableContent = {
                        Card(
                            shape = RoundedCornerShape(8.dp), elevation = 3.dp, backgroundColor = Color.White
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    modifier = Modifier.padding(24.dp, 8.dp),
                                    text = "TopEnd",
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    })
                PopupWindowLayout(
                    modifier = Modifier.align(Alignment.Center),
                    displayContent = displayContent, clickableContent = {
                        Card(
                            shape = RoundedCornerShape(8.dp), elevation = 3.dp, backgroundColor = Color.White
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    modifier = Modifier.padding(24.dp, 8.dp),
                                    text = "Center",
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    })
                PopupWindowLayout(
                    modifier = Modifier.align(Alignment.BottomStart),
                    displayContent = displayContent, clickableContent = {
                        Card(
                            shape = RoundedCornerShape(8.dp), elevation = 3.dp, backgroundColor = Color.White
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    modifier = Modifier.padding(24.dp, 8.dp),
                                    text = "BottomStart",
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    })
                PopupWindowLayout(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    displayContent = displayContent, clickableContent = {
                        Card(
                            shape = RoundedCornerShape(8.dp), elevation = 3.dp, backgroundColor = Color.White
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    modifier = Modifier.padding(24.dp, 8.dp),
                                    text = "BottomEnd",
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    })
            }
        }
    }
}

