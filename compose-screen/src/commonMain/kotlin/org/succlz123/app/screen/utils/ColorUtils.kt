package org.succlz123.app.screen.utils

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object ColorUtils {
    val red = Color(0xFFF44336)
    val pink = Color(0xFFE91E63)
    val purple = Color(0xFF9C27B0)
    val deepPurple = Color(0xFF673AB7)
    val indigo = Color(0xFF3F51B5)
    val blue = Color(0xFF2196F3)
    val lightBlue = Color(0xFF03A9F4)
    val cany = Color(0xFF00BCD4)
    val teal = Color(0xFF009688)
    val green = Color(0xFF4CAF50)
    val lightGreen = Color(0xFF8BC34A)
    val lime = Color(0xFFCDDC39)
    val yellow = Color(0xFFFFEB3B)
    val amber = Color(0xFFFFC107)
    val orange = Color(0xFFFF9800)
    val deepOrange = Color(0xFFFF5722)
    val brown = Color(0xFF795548)
    val grey = Color(0xFF9E9E9E)
    val blueGrey = Color(0xFF607D8B)

    val colorList = arrayListOf(
        lightBlue,
        lightGreen,
        amber,
        orange,
        deepOrange,
        blueGrey,
        grey,
        cany,
        red,
        pink,
        purple,
        deepPurple,
        teal,
        brown,
        purple,
        lime,
        green,
        indigo,
        yellow,
        blue,
        Color.LightGray
    )

    fun getRandomColor(): Color {
        return colorList[Random.nextInt(0, colorList.size - 1)]
    }
}
