import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.succlz123.lib.screen.ScreenContainer

fun main() = application {
    val size = DpSize(480.dp, 720.dp)
    val windowState = rememberWindowState(size = size)
    ScreenContainer(
        title = "Compose Screen",
        state = windowState,
        enableEscBack = true,
        onCloseRequest = {
            exitApplication()
        },
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Please switch to the /feat/app-demo branch")
        }
    }
}
