package org.succlz123.lib.screen.result

/**
 *
 * copy from https://github.com/airbnb/mavericks/blob/main/docs/async.md
 *
 */
sealed class ScreenResult<out T>(private val value: T?) {

    open operator fun invoke(): T? = value

    object Uninitialized : ScreenResult<Nothing>(value = null)

    data class Loading<out T>(private val value: T? = null) : ScreenResult<T>(value = value)

    data class Success<out T>(private val value: T) : ScreenResult<T>(value = value) {
        override operator fun invoke(): T = value
    }

    data class Fail<out T>(val error: Throwable, private val value: T? = null) : ScreenResult<T>(value = value)
}
