import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.succlz123.lib.screen.ScreenNavigator

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun AppToolbar(
    screenNavigator: ScreenNavigator,
    title: String,
    showBack: Boolean = true,
    onclick: (() -> Unit)? = null
) {
    Row(modifier = Modifier.padding(24.dp, 32.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (showBack) {
            Box(Modifier.noRippleClickable {
                if (onclick == null) {
                    screenNavigator.pop()
                } else {
                    onclick.invoke()
                }
            }) {
                Icon(
                    Icons.Sharp.ArrowBack,
                    modifier = Modifier.size(26.dp),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
        }
        Text(
            modifier = Modifier, text = title, fontSize = if (showBack) {
                18.sp
            } else {
                24.sp
            }, color = Color.Black, fontWeight = FontWeight.Bold
        )
    }
}