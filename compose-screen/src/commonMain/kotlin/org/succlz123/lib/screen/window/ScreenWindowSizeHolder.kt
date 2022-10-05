package org.succlz123.lib.screen.window

import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.flow.MutableStateFlow

class ScreenWindowSizeHolder {
    var size: MutableStateFlow<Size> = MutableStateFlow(Size.Zero)
    var sizeClass: MutableStateFlow<ScreenWindowSizeClass> = MutableStateFlow(ScreenWindowSizeClass.Expanded)
}