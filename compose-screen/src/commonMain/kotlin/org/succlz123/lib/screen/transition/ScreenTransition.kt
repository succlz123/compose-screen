@file:OptIn(ExperimentalAnimationApi::class)

package org.succlz123.lib.screen.transition

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import org.succlz123.lib.screen.ScreenLogger
import org.succlz123.lib.screen.ScreenRecord

typealias ScreenEnterTransitionProvider = (AnimatedContentScope<ScreenRecord>) -> EnterTransition

typealias ScreenExitTransitionProvider = (AnimatedContentScope<ScreenRecord>) -> ExitTransition

open class ScreenPushTransition(
    // for this
    val enterTransition: ScreenEnterTransitionProvider,
    // for other
    val exitTransition: ScreenExitTransitionProvider
) : ScreenTransition()

open class ScreenPopTransition(
    // for other
    val enterTransition: ScreenEnterTransitionProvider,
    // for this
    val exitTransition: ScreenExitTransitionProvider
) : ScreenTransition()

open class ScreenTransition {

    var contentZIndex: (list: List<ScreenRecord>, isPush: Boolean, init: ScreenRecord, target: ScreenRecord) -> Float =
        { list, isPush, init, target ->
            val index = list.indexOf(target).toFloat()
            ScreenLogger.debugLog("Screen animation: Z index - isPush: $isPush index: $index")
            index
        }
}

class ScreenTransitionPushNone : ScreenPushTransition(
    enterTransition = ScreenTransitionUtils.noneEnter,
    exitTransition = ScreenTransitionUtils.noneExit,
)

class ScreenTransitionPopNone : ScreenPopTransition(
    enterTransition = ScreenTransitionUtils.noneEnter, exitTransition = ScreenTransitionUtils.noneExit
)

class ScreenTransitionRightInLeftOutPush : ScreenPushTransition(
    enterTransition = ScreenTransitionUtils.right2LeftInPushEnterTransition(),
    exitTransition = ScreenTransitionUtils.right2LeftOutPushExitTransition(),
)

class ScreenTransitionRightInLeftOutPop : ScreenPopTransition(
    enterTransition = ScreenTransitionUtils.left2RightPopEnterTransition(),
    exitTransition = ScreenTransitionUtils.left2RightPopExitTransition()
)

class ScreenTransitionFadeInFadeOutPush : ScreenPushTransition(
    enterTransition = ScreenTransitionUtils.pushEnterFade(),
    exitTransition = ScreenTransitionUtils.pushExitFade(),
)

class ScreenTransitionFadeInFadeOutPop : ScreenPopTransition(
    enterTransition = ScreenTransitionUtils.popEnterFade(), exitTransition = ScreenTransitionUtils.popExitFade()
)

class ScreenTransitionTestPush : ScreenPushTransition(
    enterTransition = { scaleIn(animationSpec = tween(durationMillis = 300), initialScale = 0f) },
    exitTransition = ScreenTransitionUtils.pushExitFade(0.90f)
)

class ScreenTransitionTestPop : ScreenPopTransition(enterTransition = ScreenTransitionUtils.noneEnter,
    exitTransition = { scaleOut(animationSpec = tween(durationMillis = 300), targetScale = 0f) })

object ScreenTransitionUtils {

    val noneEnter: ScreenEnterTransitionProvider = {
        EnterTransition.None
    }

    val noneExit: ScreenExitTransitionProvider = {
        ExitTransition.None
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun right2LeftInPushEnterTransition(duration: Int = 350): ScreenEnterTransitionProvider {
        // initialState(退出) -> targetState(进入)
        return { contentScope ->
            contentScope.slideIntoContainer( // targetState(进入) - push enter - for this (target)
                towards = AnimatedContentScope.SlideDirection.Left, animationSpec = tween(durationMillis = duration)
            ) { initialOffset ->
                initialOffset
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun right2LeftOutPushExitTransition(duration: Int = 350): ScreenExitTransitionProvider {
        return { contentScope ->
            contentScope.slideOutOfContainer( // initialState(退出) - push exit - for other (target)
                towards = AnimatedContentScope.SlideDirection.Left, animationSpec = tween(durationMillis = duration)
            ) { offsetForFullSlide ->
                offsetForFullSlide / 5
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun left2RightPopEnterTransition(duration: Int = 350): ScreenEnterTransitionProvider {
        // targetState(进入) <- initialState(退出)
        return { contentScope ->
            contentScope.slideIntoContainer( // targetState(进入) - pop enter - for other (target)
                towards = AnimatedContentScope.SlideDirection.Right, animationSpec = tween(durationMillis = duration)
            ) { initialOffset ->
                initialOffset / 5
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun left2RightPopExitTransition(duration: Int = 350): ScreenExitTransitionProvider {
        return { contentScope ->
            contentScope.slideOutOfContainer( // initialState(退出) - pop exit - for this (target)
                towards = AnimatedContentScope.SlideDirection.Right, animationSpec = tween(durationMillis = duration)
            ) { offsetForFullSlide ->
                offsetForFullSlide
            }
//            + fadeOut(targetAlpha = 0.9f, animationSpec = tween(durationMillis = 350))
        }
    }

    fun pushEnterFade(initialAlpha: Float = 0f, duration: Int = 500): ScreenEnterTransitionProvider {
        return { _ ->
            fadeIn(animationSpec = tween(durationMillis = duration), initialAlpha = initialAlpha)
        }
    }

    fun pushExitFade(targetAlpha: Float = 0f, duration: Int = 500): ScreenExitTransitionProvider {
        return { _ ->
            fadeOut(animationSpec = tween(durationMillis = duration), targetAlpha = targetAlpha)
        }
    }

    fun popEnterFade(initialAlpha: Float = 0f, duration: Int = 500): ScreenEnterTransitionProvider {
        return { _ ->
            fadeIn(animationSpec = tween(durationMillis = duration), initialAlpha = initialAlpha)
        }
    }

    fun popExitFade(targetAlpha: Float = 0f, duration: Int = 500): ScreenExitTransitionProvider {
        return { _ ->
            fadeOut(animationSpec = tween(durationMillis = duration), targetAlpha = targetAlpha)
        }
    }
}