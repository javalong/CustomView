package com.javalong.customview.lib

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class StepView : View {

    companion object {
        //默认步数
        const val DEFAULT_STEP_COUNT = 3
        //默认的线条的长度
        const val DEFAULT_LINE_LENGTH = 200.0f
        //默认的线条的宽度(画笔的strokeWidth)
        const val DEFAULT_LINE_WIDTH = 2.0f
        //默认的圆圈的直径
        const val DEFAULT_CIRCLE_SIZE = 30.0f
        //默认的数字的字体大小
        const val DEFAULT_NUM_TEXTSIZE = 20.0f
        //默认主色调的颜色
        val DEFAULT_MAIN_COLOR = Color.parseColor("#CCAD74")
        //默认次一级的颜色(未完成的step的背景色，线条色)
        val DEFAULT_MINOR_COLOR = Color.parseColor("#E7E7E7")
    }

    class Builder(private val context: Context) {
        private var stepCout = DEFAULT_STEP_COUNT
        private var lineLength = DEFAULT_LINE_LENGTH
        private var lineWidth = DEFAULT_LINE_WIDTH
        private var circleSize = DEFAULT_CIRCLE_SIZE
        private var numTextSize = DEFAULT_NUM_TEXTSIZE
        private var mainColor = DEFAULT_MAIN_COLOR
        private var minorColor = DEFAULT_MINOR_COLOR

        //设置当前的步数
        fun stepCount(stepCount: Int): Builder {
            this.stepCout = stepCout
            return this
        }

        //设置连接线的长度
        fun lineLength(lineLength: Float): Builder {
            this.lineLength = lineLength
            return this
        }

        //设置连接线的粗细
        fun lineWidth(lineWidth: Float): Builder {
            this.lineWidth = lineWidth
            return this
        }

        //设置圆的直径
        fun circleSize(circleSize: Float): Builder {
            this.circleSize = circleSize
            return this
        }

        //设置数字字体的大小
        fun numTextSize(numTextSize: Float): Builder {
            this.numTextSize = numTextSize
            return this
        }


        //设置主色调
        fun mainColor(mainColor: Int): Builder {
            this.mainColor = mainColor
            return this
        }

        //设置次色调
        fun minorColor(minorColor: Int): Builder {
            this.minorColor = minorColor
            return this
        }

        fun build(): StepView {
            var stepView = StepView(context)
            stepView.numTextSize = numTextSize
            stepView.stepCout = stepCout
            stepView.minorColor = minorColor
            stepView.mainColor = mainColor
            stepView.lineWidth = lineWidth
            stepView.lineLength = lineLength
            stepView.circleSize = circleSize
            return stepView
        }
    }

    sealed class CircleType {
        object COMPLETE : CircleType()
        object NOW : CircleType()
        object UNCOMPLETE : CircleType()
    }

    private var stepCout = DEFAULT_STEP_COUNT
    private var lineLength = DEFAULT_LINE_LENGTH
    private var lineWidth = DEFAULT_LINE_WIDTH
    private var rightColor = Color.WHITE
    private var circleSize = DEFAULT_CIRCLE_SIZE
    private var numTextSize = DEFAULT_NUM_TEXTSIZE
    private var mainColor = DEFAULT_MAIN_COLOR
    private var minorColor = DEFAULT_MINOR_COLOR
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //当前的步数
    private var currentStep = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
        var typedValue = context.obtainStyledAttributes(attrs, R.styleable.jcl_StepView)
        stepCout = typedValue.getInteger(R.styleable.jcl_StepView_jcl_stepCount, DEFAULT_STEP_COUNT)
        lineLength = typedValue.getDimension(R.styleable.jcl_StepView_jcl_lineLength, DEFAULT_LINE_LENGTH)
        lineWidth = typedValue.getDimension(R.styleable.jcl_StepView_jcl_lineWidth, DEFAULT_LINE_WIDTH)
        mainColor = typedValue.getColor(R.styleable.jcl_StepView_jcl_mainColor, DEFAULT_MAIN_COLOR)
        minorColor = typedValue.getColor(R.styleable.jcl_StepView_jcl_minorColor, DEFAULT_MINOR_COLOR)
        circleSize = typedValue.getDimension(R.styleable.jcl_StepView_jcl_circleSize, DEFAULT_CIRCLE_SIZE)
        numTextSize = typedValue.getDimension(R.styleable.jcl_StepView_jcl_numberTextSize, DEFAULT_NUM_TEXTSIZE)
        typedValue.recycle()
    }

    private fun init() {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var centerX = width / 2.0f
        var centerY = height / 2.0f
        canvas.translate(centerX, centerY)
        //画圈圈
        for (i in 0 until stepCout) {
            var circleType = getCircleType(i)
            var p = getCirclePoint(i)
            when (circleType) {
                CircleType.COMPLETE -> {
                    drawRightCircle(canvas, p)
                }
                CircleType.NOW -> {
                    drawNumCircle(canvas, p, i)
                }
                CircleType.UNCOMPLETE -> {
                    drawUnCircle(canvas, p)
                }
            }
        }

        //画线
        for (i in 0 until stepCout - 1) {
            var points = getLinePoints(i)
            if (i < currentStep) {
                paint.color = mainColor
            } else {
                paint.color = minorColor
            }
            canvas.drawLine(points[0].x, points[0].y, points[1].x, points[1].y, paint)
        }
    }

    //获取线的起点和终点
    private fun getLinePoints(index: Int): Array<PointF> {
        var margin = getCirclePoint(index).x + circleSize
        return arrayOf(PointF(margin * 1.0f, 0.0f), PointF(margin * 1.0f + lineLength, 0.0f))
    }

    //画未选择的圆
    private fun drawUnCircle(canvas: Canvas, p: PointF) {
        paint.color = minorColor
        paint.style = Paint.Style.FILL
        canvas.drawOval(RectF(p.x, p.y, p.x + circleSize, p.y + circleSize), paint)
    }

    //画数字圆
    private fun drawNumCircle(canvas: Canvas, p: PointF, index: Int) {
        paint.strokeWidth = lineWidth
        paint.color = mainColor
        paint.style = Paint.Style.STROKE
        paint.textSize = numTextSize
        canvas.drawOval(RectF(p.x, p.y, p.x + circleSize, p.y + circleSize), paint)
        var num = "${index + 1}"
        var rect = Rect()
        paint.getTextBounds(num, 0, num.length, rect)
        var textWidth = paint.measureText(num)
        paint.style = Paint.Style.FILL
        canvas.drawText(num, p.x + (circleSize - textWidth) / 2, p.y + (circleSize + rect.height()) / 2, paint)
    }

    //画对勾圆
    private fun drawRightCircle(canvas: Canvas, p: PointF) {
        paint.style = Paint.Style.FILL
        paint.color = mainColor
        canvas.drawOval(RectF(p.x, p.y, p.x + circleSize, p.y + circleSize), paint)
        paint.color = rightColor
        var path = Path()
        path.moveTo(circleSize * 3 / 10 + p.x, circleSize / 2 + p.y)
        path.lineTo((circleSize * 9 / 20 + p.x), (circleSize * 13 / 20 + p.y))
        path.lineTo((circleSize * 4 / 5 + p.x), (circleSize * 7 / 20 + p.y))
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
    }

    private fun getCirclePoint(index: Int): PointF {
        var margin = 0
        if (stepCout % 2 == 0) {
            margin = ((lineLength + circleSize) * Math.abs(index - stepCout / 2) + lineLength / 2).toInt()
        } else {
            margin = ((lineLength + circleSize) * Math.abs(index - stepCout / 2) + circleSize / 2).toInt()
        }
        if (index - stepCout / 2 <= 0) {
            margin = -margin
        } else {
            margin -= circleSize.toInt()
        }
        return PointF(margin * 1.0f, -circleSize / 2 * 1.0f)
    }

    private fun getCircleType(index: Int): CircleType {
        if (index == currentStep) {
            return CircleType.NOW
        } else if (index < currentStep) {
            return CircleType.COMPLETE
        } else {
            return CircleType.UNCOMPLETE
        }
    }

    fun next() {
        if (currentStep == stepCout - 1)
            return
        currentStep++
        invalidate()
    }

    fun pre() {
        if (currentStep == 0)
            return
        currentStep--
        invalidate()
    }

    fun attachTo(parent: ViewGroup, layoutParam: ViewGroup.LayoutParams?): StepView {
        parent.addView(this, layoutParams)
        return this
    }

    fun attachTo(parent: ViewGroup): StepView {
        parent.addView(this)
        return this
    }
}