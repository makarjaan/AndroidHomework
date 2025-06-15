package ru.itis.androidhomework

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.math.atan2
import kotlin.math.hypot
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt


class PieChartView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
): View(ctx, attrs, defStyleAttrs){

    private val circleRect = RectF()  //отрисовка диаграммы
    private var circleStrokeWidth: Float = context.dpToPx(6)  //толщина диаграммы
    private var circleRadius: Float = 0F  // радиус диаграммы
    private var circlePadding: Float = context.dpToPx(8)  // padding lkz диаграммы
    private var circleSectionSpace: Float = 3F  // расстояние между линиями круга
    private var circleCenterX: Float = 0F // центр круга x
    private var circleCenterY: Float = 0F // центра круга y
    private var pieChartColors: List<String> = listOf()  // список цветов
    private var percentageCircleList: List<PieModel> = listOf()  //модель отрисовки
    private var dataList: List<Pair<Int, Int>> = listOf()  //исходный список данных
    private var animationSweepAngle: Int = 0 //для анимациия
    private var selectedSectorId: Int? = null // выделение секторов

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = context.spToPx(14)
    }


    init {
        if (attrs != null) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.PieCustomView)

            val colorResId = typeArray.getResourceId(R.styleable.PieCustomView_pieChartColors, 0)
            pieChartColors = typeArray.resources.getStringArray(colorResId).toList()

            circleStrokeWidth = typeArray.getDimension(
                R.styleable.PieCustomView_pieChartCircleStrokeWidth,
                circleStrokeWidth
            )
            circlePadding = typeArray.getDimension(
                R.styleable.PieCustomView_pieChartCirclePadding,
                circlePadding
            )
            circleSectionSpace = typeArray.getFloat(
                R.styleable.PieCustomView_pieChartCircleSectionSpace,
                circleSectionSpace
            )
            typeArray.recycle()
        }
        circlePadding += circleStrokeWidth
    }

    fun setDataChart(list: List<Pair<Int, Int>>) {
        val total = list.sumOf { it.second }
        if (total != 100) {
            Toast.makeText(context, "Сумма процентов должна быть равна 100, сейчас $total", Toast.LENGTH_SHORT).show()
            return
        }
        dataList = list
        calculatePercentageOfData()
        startAnimation()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircle(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(context.dpToPx(DEFAULT_VIEW_SIZE_WIDTH).toInt(), widthMeasureSpec)
        val height = getDefaultSize(context.dpToPx(DEFAULT_VIEW_SIZE_HEIGHT).toInt(), heightMeasureSpec)
        calculateCircleRadius(width, height)
        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return super.onTouchEvent(event)

        val x = event.x - circleCenterX
        val y = event.y - circleCenterY
        val distance = hypot(x.toDouble(), y.toDouble())

        if (distance < circleRadius - circleStrokeWidth / 2 || distance > circleRadius + circleStrokeWidth / 2) {
            selectedSectorId = null
            invalidate()
            return true
        }

        val angle = (Math.toDegrees(atan2(y, x).toDouble()) + 360) % 360

        selectedSectorId = percentageCircleList.firstOrNull {
            angle >= it.percentToStartAt && angle <= it.percentToStartAt + it.percentOfCircle
        }?.id

        invalidate()
        return true
    }



    fun startAnimation() {
        val animator = ValueAnimator.ofInt(0, 360).apply {
            duration = 1500
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener { valueAnimator ->
                animationSweepAngle = valueAnimator.animatedValue as Int
                invalidate()
            }
        }
        animator.start()
    }


    private fun drawCircle(canvas: Canvas) {
        var sweepDrawn = 0f
        for (model in percentageCircleList) {
            val sweepToDraw = when {
                sweepDrawn + model.percentOfCircle <= animationSweepAngle -> model.percentOfCircle
                sweepDrawn < animationSweepAngle -> animationSweepAngle - sweepDrawn
                else -> 0f
            }
            if (sweepToDraw > 0) {
                val isSelected = model.id == selectedSectorId

                val paint = model.paint.apply {
                    color = if (isSelected)
                        ColorUtils.blendARGB(model.colorOfLine, Color.WHITE, 0.3f)
                    else model.colorOfLine
                    strokeWidth = if (isSelected) circleStrokeWidth * 1.3f else circleStrokeWidth
                }

                val radius = if (isSelected) circleRadius + circleStrokeWidth * 0.15f else circleRadius

                val rect = RectF(
                    circleCenterX - radius,
                    circleCenterY - radius,
                    circleCenterX + radius,
                    circleCenterY + radius
                )

                canvas.drawArc(rect, model.percentToStartAt, sweepToDraw, false, paint)

                val innerRadius = radius - paint.strokeWidth / 2f
                val outerRadius = radius + paint.strokeWidth / 2f
                val textRadius = (innerRadius + outerRadius) / 2f

                val centerAngle = model.percentToStartAt + model.percentOfCircle / 2
                val angleRad = Math.toRadians(centerAngle.toDouble())

                val x = circleCenterX + (textRadius * kotlin.math.cos(angleRad)).toFloat()
                val y = circleCenterY + (textRadius * kotlin.math.sin(angleRad)).toFloat()

                val percentValue = ((model.percentOfCircle + circleSectionSpace) / 3.6f).toInt()
                canvas.drawText("$percentValue%", x, y + paintText.textSize / 3, paintText)
            }
            sweepDrawn += model.percentOfCircle
            if (sweepDrawn >= animationSweepAngle) break
        }
    }



    private fun calculateCircleRadius(width: Int, height: Int) {
        val availableWidth = width - circleStrokeWidth - circlePadding
        val availableHeight = height - circleStrokeWidth - circlePadding

        circleRadius = minOf(availableWidth, availableHeight) / 2f
        circleCenterX = width / 2f
        circleCenterY = height / 2f
        circleRect.set(
            circleCenterX - circleRadius,
            circleCenterY - circleRadius,
            circleCenterX + circleRadius,
            circleCenterY + circleRadius
        )
    }


    private fun calculatePercentageOfData() {
        var startAngle = -90f

        percentageCircleList = dataList.mapIndexed { index, (id, percent) ->
            val sweep = percent * 3.6f - circleSectionSpace
            val color = pieChartColors[index % pieChartColors.size].toColorInt()

            PieModel(
                id = id,
                percentOfCircle = sweep,
                percentToStartAt = startAngle + circleSectionSpace / 2f,
                colorOfLine = color,
                stroke = circleStrokeWidth
            ).also {
                startAngle += sweep + circleSectionSpace
            }
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("selectedSectorId", selectedSectorId ?: -1)
        return bundle
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            selectedSectorId = state.getInt("selectedSectorId", -1).takeIf { it != -1 }
            val superState = state.getParcelable("superState", Parcelable::class.java)
            super.onRestoreInstanceState(superState)
        } else {
            super.onRestoreInstanceState(state)
        }
        invalidate()
    }


    companion object {
        const val DEFAULT_VIEW_SIZE_HEIGHT = 150
        const val DEFAULT_VIEW_SIZE_WIDTH = 250
    }
}