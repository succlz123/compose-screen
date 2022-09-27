package org.succlz123.app.screen.ui.popup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.ScreenLogger

@Composable
fun DialogRootPopupScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator,
        Manifest.DialogRootPopupScreen,
        "Show the popup screen (dialog)"
    ) {
        screenNavigator.push(Manifest.DialogPopupScreen)
    }
}

@Composable
fun DialogPopupScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.width(maxWidth / 5 * 4).height(IntrinsicSize.Min).align(Alignment.Center),
            shape = RoundedCornerShape(6.dp),
            elevation = 6.dp,
            backgroundColor = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = "Notification",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = "This is a message. This is a message. This is a message. This is a message. This is a message. This is a message.",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(28.dp))
                Row {
                    Spacer(
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    Box(Modifier.clickable {
                        ScreenLogger.debugLog("Dialog Test: CANCEL clicked")
                        screenNavigator.pop()
                    }) {
                        Text(
                            modifier = Modifier.width(IntrinsicSize.Min).padding(16.dp, 8.dp),
                            text = "CANCEL",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Box(Modifier.clickable {
                        ScreenLogger.debugLog("Dialog Test: OK Clicked")
                        screenNavigator.pop()
                    }) {
                        Text(
                            modifier = Modifier.width(IntrinsicSize.Min).padding(16.dp, 8.dp),
                            text = "OK",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
