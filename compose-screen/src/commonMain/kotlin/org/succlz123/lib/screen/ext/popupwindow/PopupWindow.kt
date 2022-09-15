package org.succlz123.lib.screen.ext.popupwindow

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import org.succlz123.lib.screen.*

internal const val ScreenPopupWindowPopupScreen = "~~~Screen-PopupWindow-PopupScreen~~~"

internal const val ScreenPopupWindowSaveId = -116333633

const val KEY_POPUP_WINDOW_VIEW_SIZE = "KEY_POPUP_WINDOW_VIEW_SIZE"
const val KEY_POPUP_WINDOW_VIEW_GLOBAL_OFFSET = "KEY_POPUP_WINDOW_VIEW_GLOBAL_OFFSET"
const val KEY_POPUP_WINDOW_VIEW_CLICK_OFFSET = "KEY_POPUP_WINDOW_VIEW_CLICK_OFFSET"

@Composable
fun PopupWindowLayout(
    modifier: Modifier, displayContent: @Composable () -> Unit, clickableContent: @Composable () -> Unit
) {
    val screenNavigator = LocalScreenNavigator.current
    var clickableSize = remember { IntSize.Zero }
    var clickableGlobal = remember { Offset.Zero }
    Box(modifier = modifier.pointerInput(Unit) {
        detectTapGestures { clickOffset ->
            screenNavigator.popupWindow(
                arguments = ScreenArgs.putValue(KEY_POPUP_WINDOW_VIEW_SIZE, clickableSize)
                    .putValue(KEY_POPUP_WINDOW_VIEW_GLOBAL_OFFSET, clickableGlobal)
                    .putValue(KEY_POPUP_WINDOW_VIEW_CLICK_OFFSET, clickOffset), displayContent
            )
        }
    }.onGloballyPositioned { coordinates ->
        clickableSize = coordinates.size
        clickableGlobal = coordinates.positionInRoot()
    }) {
        clickableContent()
    }
}

@Composable
fun ScreenPopupWindowPopupScreen() {
    val screenRecord = LocalScreenRecord.current
    val innerContent = screenRecord.innerContent
    if (innerContent == null) {
        return
    }
    val windowSizeOwner = LocalScreenWindowSizeOwner.current
    val windowWidth = windowSizeOwner.getWindowHolder().size.width.toInt()
    val windowHeight = windowSizeOwner.getWindowHolder().size.height.toInt()

    val viewSize = screenRecord.arguments.value(KEY_POPUP_WINDOW_VIEW_SIZE, IntSize.Zero)
    val globalOffset = screenRecord.arguments.value(KEY_POPUP_WINDOW_VIEW_GLOBAL_OFFSET, Offset.Zero)
    val clickOffset = screenRecord.arguments.value(KEY_POPUP_WINDOW_VIEW_CLICK_OFFSET, Offset.Zero)
    val clickGlobalOffset = Offset(globalOffset.x + clickOffset.x, globalOffset.y + clickOffset.y)

    ScreenLogger.debugLog(
        "Window Size: windowWidth=$windowWidth windowHeight=$windowHeight"
    )
    ScreenLogger.debugLog("View Size: $viewSize")
    ScreenLogger.debugLog("Global: $globalOffset")
    ScreenLogger.debugLog(
        "Location: left top=" + (globalOffset.x) + ", " + (globalOffset.y) + " right bottom=" + (globalOffset.x + viewSize.width) + ", " + (globalOffset.y + viewSize.height)
    )
    ScreenLogger.debugLog("Input: $clickGlobalOffset")

    Layout(
        content = innerContent, modifier = Modifier.width(IntrinsicSize.Min).height(IntrinsicSize.Min)
    ) { measurables, constraints ->

        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight
        val miniWidth = constraints.minWidth
        val miniHeight = constraints.minHeight

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val width = placeables.maxOf { it.width }
        val height = placeables.sumOf { it.height }

        if (maxWidth != Int.MAX_VALUE && maxHeight != Int.MAX_VALUE) {
            ScreenLogger.debugLog(
                "Constraints Size: maxWidth=$maxWidth maxHeight=$maxHeight miniWidth=$miniWidth miniHeight=$miniHeight"
            )
            ScreenLogger.debugLog("Children: width=$width height=$height")
        }

        layout(width, height) {
            // We prefer to show PopupWindow in the bottom left corner
            val isRightEnough = (clickGlobalOffset.x + width) <= windowWidth
            val isLeftEnough = (clickGlobalOffset.x - width) >= 0
            val isShowLeft = if (isLeftEnough && !isRightEnough) {
                true
            } else if (isRightEnough && !isLeftEnough) {
                false
            } else {
//                (windowWidth - (clickGlobalOffset.x + width)) >= (clickGlobalOffset.x - width)
                true
            }

            val x = if (isShowLeft) {
                ((globalOffset.x + viewSize.width / 2) - width).toInt()
            } else {
                (globalOffset.x + viewSize.width / 2).toInt()
            }

            val isUpEnough = (clickGlobalOffset.y - height) >= 0
            val isBottomEnough = (clickGlobalOffset.y + height) <= windowHeight
            val isShowBottom = if (isBottomEnough && !isUpEnough) {
                true
            } else if (isUpEnough && !isBottomEnough) {
                false
            } else {
                // viewSize.height - the offset,
//                (clickGlobalOffset.y - height) <= (windowHeight - (clickGlobalOffset.y + height) + viewSize.height)
                true
            }

            var y = if (isShowBottom) {
                (globalOffset.y + viewSize.height).toInt()
            } else {
                (globalOffset.y - height).toInt()
            }

            ScreenLogger.debugLog("Detect: isLeft=$isShowLeft isBottom=$isShowBottom")
            ScreenLogger.debugLog("Place: x=$x y=$y")

            placeables.forEach { placeable ->
                placeable.placeRelative(x = x, y = y)
                y += placeable.height
            }
        }
    }
}
