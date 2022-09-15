package org.succlz123.lib.screen.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    val androidViewModelStoreOwner = LocalAndroidScreenViewModelStoreOwner.current
    return remember {
        ViewModelProvider(androidViewModelStoreOwner)[key, viewModelClass.java]
    }
}
