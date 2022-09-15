package org.succlz123.lib.screen.ext.toast

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.succlz123.lib.screen.*
import org.succlz123.lib.screen.core.ItemScreen

internal const val ScreenToastPopupScreen = "~~~Screen-Toast-PopupScreen~~~"
internal const val ScreenToastSaveId = -116222633

const val KEY_TOAST_MSG = "KEY_TOAST_MSG"

const val KEY_TOAST_TIME = "KEY_TOAST_TIME"
const val ARGS_TOAST_TIME_SHORT = 1500L
const val ARGS_TOAST_TIME_LONG = 3000L

const val KEY_TOAST_TIME_LOCATION = "KEY_TOAST_TIME_LOCATION"
val ARGS_TOAST_TIME_LOCATION_TOP_START = Alignment.TopStart
val ARGS_TOAST_TIME_LOCATION_TOP_CENTER = Alignment.TopCenter
val ARGS_TOAST_TIME_LOCATION_TOP_END = Alignment.TopEnd
val ARGS_TOAST_TIME_LOCATION_CENTER_START = Alignment.CenterStart
val ARGS_TOAST_TIME_LOCATION_CENTER = Alignment.Center
val ARGS_TOAST_TIME_LOCATION_CENTER_END = Alignment.CenterEnd
val ARGS_TOAST_TIME_LOCATION_BOTTOM_START = Alignment.BottomStart
val ARGS_TOAST_TIME_LOCATION_BOTTOM_CENTER = Alignment.BottomCenter
val ARGS_TOAST_TIME_LOCATION_BOTTOM_END = Alignment.BottomEnd

class ScreenAnonymityToastFragmentScreen(
    name: String = "ScreenAnonymityToastPopupScreen",
    replaceable: Boolean = false,
    overlayIndex: Int = 0,
    pushTransition: ScreenPushTransition = ScreenTransitionPushNone(),
    popTransition: ScreenPopTransition = ScreenTransitionPopNone(),
    content: @Composable (ScreenRecord) -> Unit
) : ItemScreen(name, replaceable, overlayIndex, pushTransition, popTransition, content)

@Composable
fun ScreenToastPopupScreen() {
    val screenRecord = LocalScreenRecord.current
    val msg = screenRecord.arguments.value(KEY_TOAST_MSG, "")
    if (msg.isEmpty()) {
        return
    }
    val location = screenRecord.arguments.value(KEY_TOAST_TIME_LOCATION, ARGS_TOAST_TIME_LOCATION_BOTTOM_CENTER)
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.align(location).padding(64.dp, 92.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 3.dp,
            backgroundColor = Color(0xCC000000)
        ) {
            Text(
                modifier = Modifier.padding(24.dp, 8.dp), text = msg, fontSize = 14.sp, color = Color.White
            )
        }
    }
}
