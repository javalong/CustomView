package com.javalong.customview.lib.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SelectCouponView : View {
    var couponComment = ""
    private var couponCommentTextSize = 24.0f
    private var couponCommentTextColor = 0
    private var circleCount = 3
    private var circleRadius = 4f
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circlePath = Path()
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var dp20 = 20

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        couponCommentTextColor = Color.WHITE
        paint.color = Color.parseColor("#CCAD74")
        textPaint.color = couponCommentTextColor
        textPaint.textSize = couponCommentTextSize
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        circlePath.reset()
        var space = (measuredHeight - circleCount * 2 * circleRadius) / (circleCount + 1)
        for (i in 0 until circleCount) {
            var p = PointF(0.0f, (space + 2 * circleRadius) * i + circleRadius + space)
            circlePath.addCircle(p.x, p.y, circleRadius, Path.Direction.CW)
        }
        for (i in 0 until circleCount) {
            var p = PointF(measuredWidth.toFloat(), (space + 2 * circleRadius) * i + circleRadius + space)

            circlePath.addCircle(p.x, p.y, circleRadius, Path.Direction.CW)
        }
        canvas.clipPath(circlePath, Region.Op.DIFFERENCE)

        var rect = Rect(0, 0, measuredWidth, measuredHeight)
        canvas.drawRect(rect, paint)
        canvas.drawText(couponComment, paddingLeft.toFloat()
                , measuredHeight.toFloat() - paddingRight, textPaint)
        canvas.restore()
    }

    override fun isLayoutRequested(): Boolean {
        return true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = textPaint.measureText(couponComment)
        setMeasuredDimension(width.toInt() + paddingLeft + paddingRight, dp20)
    }
}