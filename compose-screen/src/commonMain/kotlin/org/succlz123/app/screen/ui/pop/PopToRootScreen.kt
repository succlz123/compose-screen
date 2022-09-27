package org.succlz123.app.screen.ui.pop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenRecord
import org.succlz123.lib.screen.ScreenArgs
import org.succlz123.lib.screen.value

@Composable
fun PopToRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    BaseScreenUI(
        screenNavigator, Manifest.PopToRootScreen, "Click to ${Manifest.PopToScreen}"
    ) {
        screenNavigator.push(screenName = Manifest.PopToScreen, screenKey = "0")
    }
}

@Composable
fun PopToScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenRecord = LocalScreenRecord.current
    val count = screenRecord.arguments.value("KEY_COUNT", 0) + 1
    BaseScreenUI(screenNavigator, Manifest.PopToScreen, "", hookButtonView = {
        if (count > 3) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(24.dp).clickable {
                    screenNavigator.popTo(
                        screenName = Manifest.PopToScreen, screenKey = "0"
                    )
                }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(36.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    text = "Click to ${Manifest.PopToScreen} 0",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth().padding(24.dp).clickable {
                    screenNavigator.push(
                        screenName = Manifest.PopToScreen,
                        screenKey = count.toString(),
                        arguments = ScreenArgs.putValue("KEY_COUNT", count)
                    )
                }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(36.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    text = "Click to ${Manifest.PopToScreen} $count",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }) {}
}