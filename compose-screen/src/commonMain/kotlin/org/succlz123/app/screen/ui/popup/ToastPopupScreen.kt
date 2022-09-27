package org.succlz123.app.screen.ui.popup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.ScreenArgs
import org.succlz123.lib.screen.ext.toast.*

@Composable
fun ToastRootPopupScreen() {
    val screenNavigator = LocalScreenNavigator.current
    var count = remember { 0 }
    BaseScreenUI(
        screenNavigator, Manifest.ToastRootPopupScreen, "Show the popup screen (toast)"
    ) {
        count += 1
        if (count % 2 == 0) {
            screenNavigator.toast(
                arguments = ScreenArgs.putValue(KEY_TOAST_TIME, ARGS_TOAST_TIME_SHORT)
                    .putValue(KEY_TOAST_TIME_LOCATION, ARGS_TOAST_TIME_LOCATION_BOTTOM_CENTER).putValue(
                        KEY_TOAST_MSG,
                        "Count: $count - This is a default toast.This is a default toast.This is a default toast.This is a default toast.This is a default toast."
                    )
            )
        } else {
            screenNavigator.toast {
                val msg = "Count: $count - This is a customize toast.This is a customize toast."
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier.align(Alignment.Center).padding(64.dp, 92.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = 3.dp,
                        backgroundColor = Color.Black
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(18.dp, 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Sharp.Place,
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Place",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = msg, fontSize = 14.sp, color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

