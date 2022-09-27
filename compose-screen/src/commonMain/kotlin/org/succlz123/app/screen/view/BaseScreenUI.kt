package org.succlz123.app.screen.view

import AppToolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.succlz123.app.screen.ui.main.colorBg
import org.succlz123.lib.screen.ScreenNavigator

@Composable
fun BaseScreenUI(
    screenNavigator: ScreenNavigator,
    titleText: String,
    buttonText: String,
    clickModifier: Modifier = Modifier,
    showBack: Boolean = true,
    hookButtonView: @Composable (() -> Unit)? = null,
    buttonClick: (Offset) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(colorBg)) {
        AppToolbar(screenNavigator, titleText, showBack)
        Card(
            modifier = Modifier.fillMaxSize().padding(24.dp, 0.dp, 24.dp, 24.dp),
            shape = RoundedCornerShape(6.dp),
            elevation = 0.dp,
            backgroundColor = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(18.dp),
                        text = screenNavigator.getStackHistory(),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.weight(1.0f))
                    if (hookButtonView != null) {
                        hookButtonView.invoke()
                    } else {
                        Card(modifier = clickModifier.fillMaxWidth().padding(24.dp)
                            .clickable(indication = null, interactionSource = remember {
                                object : MutableInteractionSource {
                                    override val interactions: MutableSharedFlow<Interaction>
                                        get() = MutableSharedFlow(
                                            extraBufferCapacity = 16,
                                            onBufferOverflow = BufferOverflow.DROP_OLDEST,
                                        )

                                    override suspend fun emit(interaction: Interaction) {
                                        interactions.emit(interaction)
                                        if (interaction is PressInteraction.Press) {
                                            buttonClick(interaction.pressPosition)
                                        }
                                    }

                                    override fun tryEmit(interaction: Interaction): Boolean {
                                        return interactions.tryEmit(interaction)
                                    }
                                }
                            }) {},
                            shape = RoundedCornerShape(6.dp),
                            elevation = 0.dp,
                            backgroundColor = Color.LightGray
                        ) {
                            Text(
                                modifier = Modifier.padding(36.dp, 12.dp),
                                textAlign = TextAlign.Center,
                                text = buttonText,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}