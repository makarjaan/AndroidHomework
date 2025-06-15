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
import android.graphics.RectF


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
        require(total == 100) { "Сумма процентов должна быть равна 100, сейчас $total" }

        dataList = list
        calculatePercentageOfData()
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

        val dx = event.x - circleCenterX
        val dy = event.y - circleCenterY
        val r = hypot(dx.toDouble(), dy.toDouble())

        selectedSectorId = if (r in (circleRadius - circleStrokeWidth / 2)..(circleRadius + circleStrokeWidth / 2)) {
            val angle = ((Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())) + 360) % 360).toFloat()
            percentageCircleList.find { angle in it.percentToStartAt..(it.percentToStartAt + it.percentOfCircle) }?.id
        } else null

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
                val paint = model.paint.apply {
                    color = if (model.id == selectedSectorId)
                        ColorUtils.blendARGB(model.colorOfLine, Color.WHITE, 0.3f)
                    else model.colorOfLine
                }
                canvas.drawArc(circleRect, model.percentToStartAt, sweepToDraw, false, paint)
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
            val color = Color.parseColor(pieChartColors[index % pieChartColors.size])

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


    companion object {
        const val DEFAULT_VIEW_SIZE_HEIGHT = 150
        const val DEFAULT_VIEW_SIZE_WIDTH = 250
    }
}