package ru.itis.androidhomework

import android.graphics.Paint

//секция
data class PieModel(
    val id: Int,
    var percentOfCircle: Float = 0F,  //сколько процентов занимает внутри круга
    var percentToStartAt: Float = 0F, //откуда отрисовывается объект
    var colorOfLine: Int = 0, //цвет секции
    var stroke: Float = 0F, //ширина секции
) {
    val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = stroke
        strokeCap = Paint.Cap.BUTT
    }
}