package org.succlz123.app.screen.ui.pop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenRecord

@Composable
fun PopOnResultRootScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val onResult = LocalScreenRecord.current.result
    BaseScreenUI(
        screenNavigator,
        Manifest.PopOnResultRootScreen,
        "Click to ${Manifest.PopOnResultScreen}\nMessage from onResult: $onResult"
    ) {
        screenNavigator.push(Manifest.PopOnResultScreen)
    }
}

@Composable
fun PopOnResultScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val inputText = remember { mutableStateOf("1234") }
    BaseScreenUI(
        screenNavigator,
        Manifest.PopOnResultScreen,
        "", hookButtonView = {
            OutlinedTextField(
                value = inputText.value,
                onValueChange = { inputText.value = it },
                label = { Text(text = "On Result Value") },
            )
            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier.clickable {
                    screenNavigator.pop(result = inputText.value)
                }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
            ) {
                Text(
                    modifier = Modifier.padding(36.dp, 12.dp),
                    text = "Click to go back and set result",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    ) {
    }
}
