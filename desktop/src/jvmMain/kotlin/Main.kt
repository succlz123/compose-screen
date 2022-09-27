import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.succlz123.app.screen.ui.main.DemoMainContent
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
        DemoMainContent()
    }
}
