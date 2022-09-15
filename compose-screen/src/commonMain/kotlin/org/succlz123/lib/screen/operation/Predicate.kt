package org.succlz123.lib.screen.operation

interface Predicate<T> {
    fun apply(screen: T): Boolean
}