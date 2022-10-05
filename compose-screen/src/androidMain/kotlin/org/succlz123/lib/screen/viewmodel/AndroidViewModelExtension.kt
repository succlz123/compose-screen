package org.succlz123.lib.screen.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.succlz123.lib.screen.LocalAndroidScreenViewModelStoreOwner
import kotlin.reflect.KClass

@Composable
inline fun <reified T : ViewModel> androidViewModel(
    key: String = T::class.java.name,
): T = androidViewModel(T::class, key)

@Composable
fun <T : ViewModel> androidViewModel(
    viewModelClass: KClass<T>, key: String = viewModelClass.java.name
): T {
    return LocalAndroidScreenViewModelStoreOwner.current.androidViewModel(viewModelClass, key)
}

@Composable
inline fun <reified T : ViewModel> ViewModelStoreOwner.androidViewModel(
    key: String = T::class.java.name,
): T = androidViewModel(T::class, key)


@Composable
fun <T : ViewModel> ViewModelStoreOwner.androidViewModel(
    viewModelClass: KClass<T>, key: String = viewModelClass.java.name
): T {
    return remember {
        ViewModelProvider(this)[key, viewModelClass.java]
    }
}
