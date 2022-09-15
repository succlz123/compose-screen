package org.succlz123.lib.screen

class ScreenArgs {

    companion object {

        fun putValue(key: String, value: Any?): ScreenArgs {
            return ScreenArgs().apply {
                putValue(key, value)
            }
        }
    }

    val map = HashMap<String, Any?>()

    fun putValue(key: String, value: Any?): ScreenArgs {
        map[key] = value
        return this
    }
}

private const val KEY_NAVIGATION_COMP_SCREEN_KEY = "~~~KEY_NAVIGATION_COMP_SCREEN_KEY~~~"
private const val KEY_NAVIGATION_COMP_DEEPLINK = "~~~KEY_NAVIGATION_COMP_DEEPLINK~~~"

fun ScreenArgs?.putScreenKey(screenKey: String?): ScreenArgs? {
    return putValueAndReturnItself(KEY_NAVIGATION_COMP_SCREEN_KEY, screenKey)
}

fun ScreenArgs?.putDeeplink(deeplink: String?): ScreenArgs? {
    return putValueAndReturnItself(KEY_NAVIGATION_COMP_DEEPLINK, deeplink)
}

fun ScreenArgs?.screenKey(): String? {
    return value(KEY_NAVIGATION_COMP_SCREEN_KEY)
}

fun ScreenArgs?.deeplink(): String? {
    return value(KEY_NAVIGATION_COMP_DEEPLINK)
}

fun ScreenArgs?.putValueAndReturnItself(key: String?, value: Any?): ScreenArgs? {
    if (key.isNullOrEmpty()) {
        return this
    }
    if (this != null) {
        this.map[key] = value
        return this
    }
    val newArgs = ScreenArgs()
    newArgs.map[key] = value
    return newArgs
}

inline fun <reified T> ScreenArgs?.value(key: String?): T? {
    if (this == null) {
        return null
    }
    if (key == null) {
        return null
    }
    val value = this.map[key]

    return if (value is T) {
        value
    } else {
        null
    }
}

inline fun <reified T> ScreenArgs?.value(key: String?, default: T): T {
    if (this == null) {
        return default
    }
    if (key == null) {
        return default
    }
    val value = this.map[key]
    return if (value is T) {
        value
    } else {
        default
    }
}