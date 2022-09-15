import org.succlz123.lib.screen.viewmodel.ScreenViewModel

abstract class ScreenPageViewModel : ScreenViewModel() {
    var page = 0
    var pageSize = 20

    var currentSize = 0
    var totalSize = 0

    var hasMore = true

    override fun clear() {
        page = 0
        hasMore = true
        onCleared()
    }
}
