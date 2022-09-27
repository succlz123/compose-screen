package org.succlz123.app.screen.ui.viewmodel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.succlz123.app.screen.ui.main.Manifest
import org.succlz123.app.screen.view.BaseScreenUI
import org.succlz123.lib.screen.LocalScreenNavigator
import org.succlz123.lib.screen.LocalScreenRecord
import org.succlz123.lib.screen.screenKey
import org.succlz123.lib.screen.viewmodel.ScreenViewModel
import org.succlz123.lib.screen.viewmodel.globalViewModel
import org.succlz123.lib.screen.viewmodel.sharedViewModel
import org.succlz123.lib.screen.viewmodel.viewModel

class ViewModelRootViewModel(val msg: String) : ScreenViewModel() {

    init {
        viewModelScope.launch {
//            println("ViewModel Lifecycle: create -> $msg")
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("ViewModel Lifecycle: destroy -> $msg")
    }
}

class ViewModelInnerViewModel(val msg: String) : ScreenViewModel() {

    var inputText: String = ""

    init {
        viewModelScope.launch {
//            println("ViewModel Lifecycle: create -> $msg")
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("ViewModel Lifecycle: destroy -> $msg")
    }
}

@Composable
fun ViewModelRootScreen() {
    val screenRecord = LocalScreenRecord.current
    val screenNavigator = LocalScreenNavigator.current
    // We don't want to import the kotlin reflect lib, so using the invoker to new the instance
    val normalRootViewModel = viewModel { ViewModelRootViewModel("normalVM - RootViewModel - Created by RootScreen") }
    val sharedRootViewModel =
        sharedViewModel { ViewModelRootViewModel("sharedVM - RootViewModel - Created by RootScreen") }
    // Never destroy until the screen manager is changed.
    val globalViewModel =
        globalViewModel { ViewModelInnerViewModel("globalVM - RootViewModel - Created by RootScreen") }

    LaunchedEffect(Unit) {
        println(normalRootViewModel.msg)
        println(sharedRootViewModel.msg)
        println(globalViewModel.msg)
        println("\n")
    }
    DisposableEffect(Unit) {
        onDispose {
            println("Current Screen Record VM size: " + screenRecord.keysVM().size)
            println("Current Screen Host VM size: " + screenNavigator.getHostVMSize() + " <- The Global VM.")
            println("The global view model will not destroy: " + globalViewModel.msg)
        }
    }
    BaseScreenUI(
        screenNavigator, Manifest.ViewModelRootScreen, "Click to ${Manifest.ViewModelScreen}"
    ) {
        screenNavigator.push(Manifest.ViewModelScreen)
    }
}

@Composable
fun ViewModelScreen() {
    val screenNavigator = LocalScreenNavigator.current
    val screenRecord = LocalScreenRecord.current
    val countValue = screenRecord.arguments.screenKey()?.toIntOrNull() ?: 0
    var inputText by remember { mutableStateOf("") }

    // Bound to the host navigation, reuse the root view model, destroyed when there is no screen hold it.
    val normalRootViewModel =
        sharedViewModel { ViewModelRootViewModel("normalVM - RootViewModel - Created by Screen/$countValue") }
    val sharedKeyRootViewModel =
        sharedViewModel(key = countValue.toString()) { ViewModelRootViewModel("sharedVM - RootViewModel/$countValue - Created by Screen/$countValue") }

    val globalViewModel =
        globalViewModel { ViewModelInnerViewModel("globalVM - RootViewModel/$countValue - Created by Screen/$countValue") }

    // Bind to the current screen record, destroyed when the screen is not in the stack.
    val normalViewModel =
        viewModel { ViewModelInnerViewModel("normalVM - InnerViewModel - Created by Screen/$countValue") }
    val normalKeyViewModel =
        viewModel(key = countValue.toString()) { ViewModelInnerViewModel("normalVM - InnerViewModel/$countValue - Created by Screen/$countValue") }


    // The vm does not destroy, so we can show the previous input text.
    if (normalKeyViewModel.inputText != inputText) {
        inputText = normalKeyViewModel.inputText
    }

    LaunchedEffect(Unit) {
        println(normalRootViewModel.msg)
        println(sharedKeyRootViewModel.msg)

        println(normalViewModel.msg)
        println(normalKeyViewModel.msg + " " + normalKeyViewModel.inputText)

        println(globalViewModel.msg)
        println("\n")
    }

    val scope = rememberCoroutineScope()

    BaseScreenUI(screenNavigator, Manifest.ViewModelScreen, "", hookButtonView = {
        OutlinedTextField(
            value = inputText,
            onValueChange = {
                scope.launch {
                    inputText = it
                    normalKeyViewModel.inputText = it
                }
            },
            label = { Text(text = "On Result Value") },
        )
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.clickable {
                val nextValue = countValue + 1
                screenNavigator.push(
                    Manifest.ViewModelScreen,
                    screenKey = nextValue.toString(),
//                    pushOptions = PushOptions().apply {
//                        removePredicate = PushOptions.SingleTopPredicate(Manifest.ViewModelScreen)
//                    }
                )
            }, shape = RoundedCornerShape(6.dp), elevation = 0.dp, backgroundColor = Color.LightGray
        ) {
            Text(
                modifier = Modifier.padding(36.dp, 12.dp),
                text = "Click to more deeper and make it dispose -> " + normalKeyViewModel.inputText,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
        Spacer(Modifier.height(12.dp))
    }) {}
}
