![banner][file:banner]

[file:banner]: screenshot/Banner.png

<img src="https://img.shields.io/static/v1?label=compose-screen&message=0.0.1&color=success"/>

<img src="https://img.shields.io/static/v1?label=platform&message=Android&color=green"/>   <img src="https://img.shields.io/static/v1?label=platform&message=Desktop&color=blue"/>

# 介绍

一个简单并提供高度扩展功能的 Compose 导航组件，同时支持 Android 和 Desktop 平台。

# 常用功能

<img src="https://github.com/succlz123/compose-screen/blob/master/screenshot/Navigation.png?raw=true" width="460" height="725"/><br/>

# 使用

## 下载

```
// Android
implementation("io.github.succlz123:compose-screen-android:0.0.1")
// Desktop
implementation("io.github.succlz123:compose-screen-desktop:0.0.1")
```

## 开始

### Android
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenContainer(this, this, this) {
                // Screen Host
            }
        }
    }
}
```

### Desktop

```kotlin
fun main() = application {
    val size = DpSize(480.dp, 720.dp)
    val windowState = rememberWindowState(size = size)
    ScreenContainer(
        title = "Compose Screen",
        state = windowState,
        onCloseRequest = {
            exitApplication()
        },
    ) {
        // Screen Host
    }
}
```

### 通用 

#### Screen Host

```kotlin
// 创建 screen navigator
val screenNavigator = rememberScreenNavigator()

// 初始化应用页面，确定根页面
ScreenHost(screenNavigator = screenNavigator, rootScreenName = "your root screen") {
    
    // 注册添加你所需要展示的页面
    groupScreen(screenName = ("your root screen")) {
        XXXScreen()
    }
    groupScreen(screenName = "your YYY screen") {
        YYYScreen()
    }
    itemScreen(screenName = "your popup screen") {
        ZZZPopupScreen()
    }
    ...
    
 }
```

#### 导航

```kotlin
// 获取 screenNavigator
val screenNavigator = LocalScreenNavigator.current

// 普通导航
screenNavigator.push(screenName = "your screen")

// 导航携带参数
val count = 1
screenNavigator.push(
    screenName = "your screen",
    arguments = ScreenArgs.putValue("KEY_COUNT", count)
)

// singleTop 栈顶复用
screenNavigator.push(
    screenName = "your screen",
    pushOptions = PushOptions().apply {
            removePredicate = PushOptions.SingleTopPredicate("your screen")
        }
)

// singleTask 栈内复用
screenNavigator.push(
    screenName = "your screen",
    pushOptions = PushOptions().apply {
            removePredicate = PushOptions.SingleTaskPredicate("your screen")
        }
)

// 导航同时移除当前页面
screenNavigator.push(
    screenName = "your screen",
    pushOptions = PushOptions().apply {
            removePredicate = PushOptions.PopItselfPredicate()
        }
)

// 移除任意页面
screenNavigator.remove(screenName = "your screen")
```

#### 返回

```kotlin
screenNavigator.pop()
```

##### 返回到某一页面

```kotlin
screenNavigator.popTo(screenName = "ZZZScreen")
```

##### 返回并获得结果

```kotlin
val onResult = LocalScreenRecord.current.result
```

##### 返回拦截

```kotlin
screenNavigator.pop(popOptions = PopOptions(popStackFinalInterceptor = { backstackList, _, _ ->
    backstackList.size > 10
}))
```

#### 浮窗

##### 弹窗

<img src="https://github.com/succlz123/compose-screen/blob/master/screenshot/Dialog.png?raw=true" width="517" height="583"/><br/>

PopupScreen 层级高于 Screen，附属于当前绑定的 Group Screen。退出 Group Screen 时，PopupScreen 同时消失。

```kotlin
ScreenHost(screenNavigator = screenNavigator, rootScreenName = Manifest.MainScreen) {
    // register your popup screen
    popup(screenName = Manifest.DialogPopupScreen) {
            DialogPopupScreen(screenNavigator)
        }
    }

// show th dialog
screenNavigator.push(Manifest.DialogPopupScreen)
```

##### 吐司

<img src="https://github.com/succlz123/compose-screen/blob/master/screenshot/Toast.png?raw=true" width="517" height="583"/><br/>


全局只有一个吐司，吐司永远在所有页面层级之上，切换页面不会导致吐司消失。

```kotlin
// 默认的吐司
screenNavigator.toast("Your toast.")

// 更多配置的默认的吐司
screenNavigator.toast(
    arguments = ScreenArgs.putValue(KEY_TOAST_TIME, ARGS_TOAST_TIME_SHORT)
            .putValue(KEY_TOAST_TIME_LOCATION, ARGS_TOAST_TIME_LOCATION_BOTTOM_CENTER)
            .putValue(KEY_TOAST_MSG, "Your toast."))

// 显示自定义的吐司
screenNavigator.toast {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        ...
    }
}
```

##### 浮动窗口

<img src="https://github.com/succlz123/compose-screen/blob/master/screenshot/PopupWindow.png?raw=true" width="517" height="583"/><br/>

同一个 Group Screen 中只能存在一个 PopupWindow。退出 Screen PopupWindow 同时消失。

```kotlin
Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        ...
        PopupWindowLayout(displayContent = {
                        // the content which you want to display
                        ...
                    }, clickableContent = {
                        // such as a button layout
                        ...
                    })
        ...
}
```

#### 生命周期

针对 Compose 为了更好的处理在某些特殊场景下的需要，ScreenLifecycleCallback 提供了 screenLifecycleState 和 hostLifecycleState 生命周期回调。

##### hostLifecycleState

宿主 Android/Desktop 生命周期回调

> Android 是收不到 onCreate onDestroy 相应回调

| Compose Screen - Host Lifecyle | Android   | Desktop                |
|--------------------------------|-----------|------------------------|
|                                | onCreate  |                        |
|                                | onStart   |                        |
| RESUMED                        | onResume  | active                 |
| PAUSED                         | onPasuse  | inActive - isMinimized |
|                                | onStop    |                        |
|                                | onDestroy |                        |
| onDestroy                      |           | onCloseRequest         |

##### screenLifecycleState

Screen 抽象后的生命周期，通常来讲业务逻辑不应该依赖与此，切换页面会导致在此触发的 OnCreate 和 OnResume。

| Compose Screen - Screen Lifecycle | Screen                                    |
|-----------------------------------|-------------------------------------------|
| CREATED                          | Push the new screen record into the stack | 
|                                   |                                           | 
| RESUMED                          | Check the host lifecycle - onResume       |  
| PAUSED                           | Check the host lifecycle - onPause        |  
|                                   |                                           |  
| PAUSED                         | A new screen was dispalyed                | 
| DESTROYED                         | The screen record was out of the stack              | 

```kotlin
ScreenLifecycleCallback(screenRecord, screenLifecycleState = {
    println("Screen lifecycle - $countValue: ${it.name}")
    if (it == ComposeLifecycle.State.DESTROYED) {
        screenNavigator.toast("Screen lifecycle - $countValue: is destroyed")
    }
}, hostLifecycleState = {
    println(">>> Host lifecycle: ${getPlatformName()} - ${it.name} <<<")
})
```

#### 视图模块

针对不同场景下的 ViewModel 使用, Compose Screen 提供了 viewModel，sharedViewModel，globalViewModel，androidViewModel 的扩展方法来方便使用。

##### Compose ViewModel

- viewModel

```kotlin
// 绑定到当前 group screen 的 ViewModel，当 group screen 出栈的时候，该 ViewModel 被销毁
val viewModel = viewModel { YourViewModel() }
```

- sharedViewModel

```kotlin
// XXX -> YYY，当 YYY 出栈的时候，sharedViewModel 将继续存在，当 XXX 也出栈的时候， sharedViewModel 才被销毁
@Composable
fun XXXScreen(){
    val sharedViewModel = sharedViewModel { YourViewModel() }
}

@Composable
fun YYYScreen(){
    val sharedViewModel = sharedViewModel { YourViewModel() }
}
```

- globalViewModel

```kotlin
// 被绑定到 Screen Mananger 上的 ViewModel，全局唯一，只有 Screen Mananger 不在后才被销毁
val globalViewModel = globalViewModel { YourViewModel() }
```

##### Android ViewModel

```kotlin
// Android 平台独有，通过初始化时传入的 ViewModelStoreOwner，来获取或者生成 AndroidX 的 ViewModel.
val androidViewModel = androidViewModel { YourAndroidViewModel() }
```

#### 动画

##### 转场动画

框架内已经封装了几个常见转场动画，如下是右边进左边出的转场动画

```kotlin
screenNavigator.push(
    "your screen",
    pushOptions = PushOptions(
        pushTransition = ScreenTransitionRightInLeftOutPush(),
        popTransition = ScreenTransitionRightInLeftOutPop()
    )
)
```

#### 响应系统返回

##### Android

在 Compose Screen 初始化时，传入 OnBackPressedDispatcherOwner, 就可以响应系统返回按键。

##### Desktop

可以通过 enableEscBack 来开启关闭 Compose Screen 响应 ESC 按键返回


# 感谢

- https://developer.android.com/jetpack/compose/navigation
- https://github.com/bytedance/scene
- https://github.com/olshevski/compose-navigation-reimagined
