package com.javalong.customview.lib.layout

import android.content.Context
import android.graphics.*
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.javalong.customview.lib.R
import com.javalong.customview.lib.exception.CustomViewParamException

class VerticalCouponLayout : ViewGroup {

    companion object {
        //默认的中间的圈圈数
        const val DEFAULT_MEDIUM_CIRCLE_COUNT = 40
        //默认中间圈圈的宽度
        const val DEFAULT_MEDIUM_CIRCLE_WIDTH = 20.0f
        //默认中间圈圈的高度
        const val DEFAULT_MEDIUM_CIRCLE_HEIGHT = 10.0f
        //默认阴影大小
        const val DEFAULT_SHADOW_RADIUS = 20.0f
        //默认边角裁剪的圆的半径
        const val DEFAULT_INTENT_RADIUS = 20.0f
    }


    private var indentRadius = DEFAULT_INTENT_RADIUS
    private var mediumCircleWidth = DEFAULT_MEDIUM_CIRCLE_WIDTH
    private var mediumCircleHeight = DEFAULT_MEDIUM_CIRCLE_HEIGHT
    private var mediumCircleCount = DEFAULT_MEDIUM_CIRCLE_COUNT
    private var shadowRadius = DEFAULT_SHADOW_RADIUS

    private var topHeight = 0.0f
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var blurPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        var typedValue = context.obtainStyledAttributes(attrs, R.styleable.jcl_VerticalCouponLayout)
        indentRadius = typedValue.getDimension(R.styleable.jcl_VerticalCouponLayout_jcl_VerticalCouponLayout_indentRadius, DEFAULT_INTENT_RADIUS)
        shadowRadius = typedValue.getDimension(R.styleable.jcl_VerticalCouponLayout_jcl_VerticalCouponLayout_shadowRadius, DEFAULT_SHADOW_RADIUS)
        mediumCircleHeight = typedValue.getDimension(R.styleable.jcl_VerticalCouponLayout_jcl_VerticalCouponLayout_mediumCircleHeight, DEFAULT_MEDIUM_CIRCLE_HEIGHT)
        mediumCircleWidth = typedValue.getDimension(R.styleable.jcl_VerticalCouponLayout_jcl_VerticalCouponLayout_mediumCircleWidth, DEFAULT_MEDIUM_CIRCLE_WIDTH)
        mediumCircleCount = typedValue.getInteger(R.styleable.jcl_VerticalCouponLayout_jcl_VerticalCouponLayout_mediumCircleCount,DEFAULT_MEDIUM_CIRCLE_COUNT)
        typedValue.recycle()
        init()
    }


    class Builder() {

        private var indentRadius = HorizontalCouponLayout.DEFAULT_INTENT_RADIUS
        private var mediumCircleWidth = HorizontalCouponLayout.DEFAULT_MEDIUM_CIRCLE_WIDTH
        private var mediumCircleHeight = HorizontalCouponLayout.DEFAULT_MEDIUM_CIRCLE_HEIGHT
        private var mediumCircleCount = HorizontalCouponLayout.DEFAULT_MEDIUM_CIRCLE_COUNT
        private var shadowRadius = HorizontalCouponLayout.DEFAULT_SHADOW_RADIUS

        fun indentRadius(indentRadius: Float): Builder {
            this.indentRadius = indentRadius
            return this
        }

        fun mediumCircleWidth(mediumCircleWidth: Float): Builder {
            this.mediumCircleWidth = mediumCircleWidth
            return this
        }

        fun mediumCircleHeight(mediumCircleHeight: Float): Builder {
            this.mediumCircleHeight = mediumCircleHeight
            return this
        }

        fun mediumCircleCount(mediumCircleCount: Int): Builder {
            this.mediumCircleCount = mediumCircleCount
            return this
        }

        fun shadowRadius(shadowRadius: Float): Builder {
            this.shadowRadius = shadowRadius
            return this
        }

        fun build(context: Context): VerticalCouponLayout {
            var couponLayout = VerticalCouponLayout(context)
            couponLayout.mediumCircleCount = mediumCircleCount
            couponLayout.mediumCircleWidth = mediumCircleWidth
            couponLayout.mediumCircleHeight = mediumCircleHeight
            couponLayout.shadowRadius = shadowRadius
            couponLayout.indentRadius = indentRadius

            return couponLayout
        }
    }

    fun init() {
        setWillNotDraw(false)
        paint.style = Paint.Style.FILL
        blurPaint.color = Color.parseColor("#15000000")
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (childCount != 2) {
            throw CustomViewParamException("child count should be 2 !!!")
        }
    }

    override fun setLayoutParams(params: LayoutParams?) {
        super.setLayoutParams(params)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            var usedHeight = 0
            var arr = arrayOf(topHeight, measuredHeight - topHeight - 2 * shadowRadius)
            for (i in 0 until 2) {
                var view = getChildAt(i)
                var lp = view.layoutParams as MarginLayoutParams
                view.layout(lp.leftMargin + shadowRadius.toInt(), lp.topMargin + shadowRadius.toInt() + usedHeight, measuredWidth + lp.rightMargin - shadowRadius.toInt(), usedHeight + arr[i].toInt() + lp.topMargin + shadowRadius.toInt())
                usedHeight += (arr[i].toInt() + lp.topMargin + lp.bottomMargin)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0..1) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
        topHeight = getChildAt(0).measuredHeight.toFloat()
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize((heightMeasureSpec))

        var newWidthSize = MeasureSpec.getSize(widthMeasureSpec) - 2 * shadowRadius.toInt()
        var newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidthSize, MeasureSpec.getMode(widthMeasureSpec))
        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                var heightSpecs = arrayOf(MeasureSpec.makeMeasureSpec(topHeight.toInt(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - 2 * shadowRadius.toInt() - topHeight.toInt(), MeasureSpec.EXACTLY))
                for (i in 0..1) {
                    getChildAt(i).measure(newWidthMeasureSpec, heightSpecs[i])
                }
            }
            MeasureSpec.AT_MOST -> {
                var lp = getChildAt(1).layoutParams as MarginLayoutParams
                if (lp.height == LayoutParams.WRAP_CONTENT) {
                    var heightSpecs = arrayOf(MeasureSpec.makeMeasureSpec(topHeight.toInt(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - 2 * shadowRadius.toInt() - topHeight.toInt(), MeasureSpec.AT_MOST))
                    for (i in 0..1) {
                        getChildAt(i).measure(newWidthMeasureSpec, heightSpecs[i])
                    }
                } else if (lp.height == LayoutParams.MATCH_PARENT) {
                    var heightSpecs = arrayOf(MeasureSpec.makeMeasureSpec(topHeight.toInt(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - 2 * shadowRadius.toInt() - topHeight.toInt(), MeasureSpec.EXACTLY))
                    for (i in 0..1) {
                        getChildAt(i).measure(newWidthMeasureSpec, heightSpecs[i])
                    }
                } else {
                    var heightSpecs = arrayOf(MeasureSpec.makeMeasureSpec(topHeight.toInt(), MeasureSpec.EXACTLY), Math.min(MeasureSpec.makeMeasureSpec(heightSize - 2 * shadowRadius.toInt() - topHeight.toInt(), lp.height), MeasureSpec.EXACTLY))
                    for (i in 0..1) {
                        getChildAt(i).measure(newWidthMeasureSpec, heightSpecs[i])
                    }
                }
            }
        }

        var totalHeight = 0
        for (i in 0..1) {
            var lp = getChildAt(1).layoutParams as MarginLayoutParams
            totalHeight += (getChildAt(i).measuredHeight + lp.topMargin + lp.bottomMargin)
        }
        var newHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMeasureSpec, newHeightSpec)

    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onDraw(canvas: Canvas) {
        var indentPath = Path()
        indentPath.addCircle(shadowRadius.toFloat(), shadowRadius.toFloat(), indentRadius, Path.Direction.CW)
        indentPath.addCircle(measuredWidth.toFloat() - shadowRadius.toFloat(), shadowRadius.toFloat(), indentRadius, Path.Direction.CW)
        indentPath.addCircle(shadowRadius.toFloat(), measuredHeight.toFloat() - shadowRadius.toFloat(), indentRadius, Path.Direction.CW)
        indentPath.addCircle(measuredWidth.toFloat() - shadowRadius, measuredHeight.toFloat() - shadowRadius, indentRadius, Path.Direction.CW)
        indentPath.addCircle(shadowRadius.toFloat(), topHeight + shadowRadius.toFloat(), indentRadius, Path.Direction.CW)
        indentPath.addCircle(measuredWidth - shadowRadius.toFloat(), topHeight + shadowRadius.toFloat(), indentRadius, Path.Direction.CW)

        var space = (measuredWidth - mediumCircleCount * mediumCircleWidth - 2 * indentRadius - 2 * shadowRadius) / (mediumCircleCount + 1)
        for (i in 0 until mediumCircleCount) {
            var left = (space + mediumCircleWidth) * i + indentRadius + space + shadowRadius.toFloat()
            var top = topHeight - mediumCircleHeight / 2 + shadowRadius.toFloat()
            indentPath.addOval(RectF(left, top, left + mediumCircleWidth, top + mediumCircleHeight), Path.Direction.CW)
        }
        canvas.clipPath(indentPath, Region.Op.DIFFERENCE)

        var filter = BlurMaskFilter(shadowRadius.toFloat(), BlurMaskFilter.Blur.SOLID)
        blurPaint.maskFilter = filter
        canvas.drawRect(Rect(shadowRadius.toInt(), shadowRadius.toInt(), measuredWidth - shadowRadius.toInt(), measuredHeight - shadowRadius.toInt()), blurPaint)

        var topShader = LinearGradient(mediumCircleWidth / 3.0f, 0.0f, mediumCircleWidth * 3 / 4.0f, topHeight, Color.parseColor("#FAE19F")
                , Color.parseColor("#DDBC6F"), Shader.TileMode.CLAMP)
        paint.shader = topShader
        canvas.drawRect(Rect(shadowRadius.toInt(), shadowRadius.toInt(), measuredWidth - shadowRadius.toInt(), topHeight.toInt() + shadowRadius.toInt()), paint)
        paint.shader = null
        paint.color = Color.WHITE
        canvas.drawRect(Rect(shadowRadius.toInt(), shadowRadius.toInt() + topHeight.toInt(), measuredWidth - shadowRadius.toInt(), measuredHeight - shadowRadius.toInt()), paint)
    }

    fun attachTo(parent: ViewGroup, layoutParam: ViewGroup.LayoutParams?): VerticalCouponLayout {
        parent.addView(this, layoutParam)
        return this
    }

    fun attachTo(parent: ViewGroup): VerticalCouponLayout {
        parent.addView(this)
        return this
    }

}