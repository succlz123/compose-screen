package org.succlz123.app.screen.ui.push

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
import org.succlz123.lib.screen.screenKey

@Composable
fun PushRemoveAnyRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenKey = 1
    BaseScreenUI(
        screenNavigator,
        Manifest.PushRemoveAnyRootScreen,
        "Click to ${Manifest.PushRemoveAnyScreen}-${screenKey}"
    ) {
        screenNavigator.push(
            screenName = Manifest.PushRemoveAnyScreen,
            screenKey = screenKey.toString()
        )
    }
}

@Composable
fun PushRemoveAnyScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val arguments = LocalScreenRecord.current.arguments
    val key = (arguments.screenKey()?.toIntOrNull() ?: 0)
    BaseScreenUI(screenNavigator, Manifest.PushRemoveAnyScreen, "", hookButtonView = {
        if (key > 3) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(24.dp).clickable {
                    screenNavigator.remove(screenName = Manifest.PushRemoveAnyScreen, screenKey = "1")
                }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(36.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    text = "Click to Remove ${Manifest.PushRemoveAnyScreen}-1",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(24.dp).clickable {
                    screenNavigator.remove(screenName = Manifest.PushRemoveAnyScreen, screenKey = "2")
                }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(36.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    text = "Click to Remove ${Manifest.PushRemoveAnyScreen}-2",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(24.dp).clickable {
                    screenNavigator.pop()
                }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(36.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    text = "Click to go back",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth().padding(24.dp).clickable {
                    screenNavigator.push(
                        screenName = Manifest.PushRemoveAnyScreen,
                        screenKey = (key + 1).toString()
                    )
                }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(36.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    text = "Click to ${Manifest.PushRemoveAnyScreen}-${key + 1}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }) {}
}
