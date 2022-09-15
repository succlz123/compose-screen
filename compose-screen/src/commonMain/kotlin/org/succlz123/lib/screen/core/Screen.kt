package org.succlz123.lib.screen.core

import androidx.compose.runtime.Composable
import org.succlz123.lib.screen.ScreenRecord

abstract class Screen(
    val name: String,
    val content: @Composable (ScreenRecord) -> Unit
)
