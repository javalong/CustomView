package com.javalong.customview.lib.layout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.javalong.customview.lib.R
import com.javalong.customview.lib.exception.CustomViewParamException

class HorizontalCouponLayout : ViewGroup {

    companion object {
        //默认的中间的圈圈数
        const val DEFAULT_MEDIUM_CIRCLE_COUNT = 13
        //默认中间圈圈的宽度
        const val DEFAULT_MEDIUM_CIRCLE_WIDTH = 10.0f
        //默认中间圈圈的高度
        const val DEFAULT_MEDIUM_CIRCLE_HEIGHT = 20.0f
        //默认阴影大小
        const val DEFAULT_SHADOW_RADIUS = 20.0f
        //默认边角裁剪的圆的半径
        const val DEFAULT_INTENT_RADIUS = 20.0f
    }

    var indentRadius = DEFAULT_INTENT_RADIUS
    var mediumCircleWidth = DEFAULT_MEDIUM_CIRCLE_WIDTH
    var mediumCircleHeight = DEFAULT_MEDIUM_CIRCLE_HEIGHT
    var mediumCircleCount = DEFAULT_MEDIUM_CIRCLE_COUNT
    var shadowRadius = DEFAULT_SHADOW_RADIUS

    var leftWidth = 0f
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var blurPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        var typedValue = context.obtainStyledAttributes(attrs, R.styleable.jcl_HorizontalCouponLayout)
        indentRadius = typedValue.getDimension(R.styleable.jcl_HorizontalCouponLayout_jcl_HorizontalCouponLayout_indentRadius, DEFAULT_INTENT_RADIUS)
        shadowRadius = typedValue.getDimension(R.styleable.jcl_HorizontalCouponLayout_jcl_HorizontalCouponLayout_shadowRadius, DEFAULT_SHADOW_RADIUS)
        mediumCircleHeight = typedValue.getDimension(R.styleable.jcl_HorizontalCouponLayout_jcl_HorizontalCouponLayout_mediumCircleHeight, DEFAULT_MEDIUM_CIRCLE_HEIGHT)
        mediumCircleWidth = typedValue.getDimension(R.styleable.jcl_HorizontalCouponLayout_jcl_HorizontalCouponLayout_mediumCircleHeight, DEFAULT_MEDIUM_CIRCLE_WIDTH)
        mediumCircleCount = typedValue.getInteger(R.styleable.jcl_HorizontalCouponLayout_jcl_HorizontalCouponLayout_mediumCircleHeight, DEFAULT_MEDIUM_CIRCLE_COUNT)
        typedValue.recycle()
        init()
    }

    class Builder() {

        private var indentRadius = DEFAULT_INTENT_RADIUS
        private var mediumCircleWidth = DEFAULT_MEDIUM_CIRCLE_WIDTH
        private var mediumCircleHeight = DEFAULT_MEDIUM_CIRCLE_HEIGHT
        private var mediumCircleCount = DEFAULT_MEDIUM_CIRCLE_COUNT
        private var shadowRadius = DEFAULT_SHADOW_RADIUS
        private var viewsMap: HashMap<View, ViewGroup.LayoutParams?> = HashMap()

//        fun addView(view: View, lp: ViewGroup.LayoutParams?): Builder {
//            viewsMap[view] = lp
//            return this
//        }
//
//        fun addView(view: View): Builder {
//            viewsMap[view] = null
//            return this
//        }

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

        fun build(context: Context): HorizontalCouponLayout {
            var couponLayout = HorizontalCouponLayout(context)
            couponLayout.mediumCircleCount = mediumCircleCount
            couponLayout.mediumCircleWidth = mediumCircleWidth
            couponLayout.mediumCircleHeight = mediumCircleHeight
            couponLayout.shadowRadius = shadowRadius
            couponLayout.indentRadius = indentRadius

//            viewsMap.forEach { view, lp ->
//                if (lp == null) {
//                    couponLayout.addView(view)
//                } else {
//                    couponLayout.addView(view, lp)
//                }
//            }

            return couponLayout
        }
    }

    private fun init() {
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
            var usedWidth = 0
            var arr = arrayOf(leftWidth, measuredWidth - leftWidth - 2 * shadowRadius)
            for (i in 0 until 2) {
                var view = getChildAt(i)
                var lp = view.layoutParams as MarginLayoutParams
                view.layout(usedWidth + lp.leftMargin + shadowRadius.toInt(), lp.topMargin + shadowRadius.toInt(), shadowRadius.toInt() + usedWidth + arr[i].toInt() + lp.leftMargin, measuredHeight + lp.topMargin - shadowRadius.toInt())
                usedWidth += (arr[i].toInt() + lp.leftMargin + lp.rightMargin)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0..1) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
        leftWidth = getChildAt(0).measuredWidth.toFloat()
        var totalWidth = 0
        for (i in 0..1) {
            var view = getChildAt(i)
            var lp = view.layoutParams as MarginLayoutParams
            totalWidth += (lp.leftMargin + lp.rightMargin + view.measuredWidth)
        }
        totalWidth = View.resolveSize(totalWidth, widthMeasureSpec)

        var maxHeight = 0
        for (i in 0..1) {
            var view = getChildAt(i)
            var lp = view.layoutParams as MarginLayoutParams
            var total = lp.topMargin + lp.bottomMargin + view.measuredHeight
            if (maxHeight < total) {
                maxHeight = total
            }
        }
        maxHeight = View.resolveSize(maxHeight, heightMeasureSpec)
        var newHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight - 2 * shadowRadius.toInt(), MeasureSpec.EXACTLY)
        var widthSpecs = arrayOf(MeasureSpec.makeMeasureSpec(leftWidth.toInt(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(totalWidth - 2 * shadowRadius.toInt() - leftWidth.toInt(), MeasureSpec.EXACTLY))
        for (i in 0..1) {
            getChildAt(i).measure(widthSpecs[i], newHeightSpec)
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
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
        indentPath.addCircle(leftWidth + shadowRadius.toFloat(), shadowRadius.toFloat(), indentRadius, Path.Direction.CW)
        indentPath.addCircle(leftWidth + shadowRadius.toFloat(), measuredHeight.toFloat() - shadowRadius.toFloat(), indentRadius, Path.Direction.CW)

        var space = (measuredHeight - mediumCircleCount * mediumCircleHeight - 2 * indentRadius - 2 * shadowRadius) / (mediumCircleCount + 1)
        for (i in 0 until mediumCircleCount) {
            var left = leftWidth - mediumCircleWidth / 2 + shadowRadius.toFloat()
            var top = (space + mediumCircleHeight) * i + indentRadius + space + shadowRadius.toFloat()
            indentPath.addOval(RectF(left, top, left + mediumCircleWidth, top + mediumCircleHeight), Path.Direction.CW)
        }
        canvas.clipPath(indentPath, Region.Op.DIFFERENCE)
        var filter = BlurMaskFilter(shadowRadius.toFloat(), BlurMaskFilter.Blur.SOLID)
        blurPaint.maskFilter = filter
        canvas.drawRect(Rect(shadowRadius.toInt(), shadowRadius.toInt(), measuredWidth - shadowRadius.toInt(), measuredHeight - shadowRadius.toInt()), blurPaint)
        var leftShader = LinearGradient(leftWidth / 3, 0.0f, leftWidth * 3 / 4, measuredHeight * 4 / 5.0f, Color.parseColor("#FAE19F")
                , Color.parseColor("#DDBC6F"), Shader.TileMode.CLAMP)
        paint.shader = leftShader
        canvas.drawRect(Rect(shadowRadius.toInt(), shadowRadius.toInt(), leftWidth.toInt() + shadowRadius.toInt(), measuredHeight - shadowRadius.toInt()), paint)
        paint.shader = null
        paint.color = Color.WHITE
        canvas.drawRect(Rect(leftWidth.toInt() + shadowRadius.toInt(), shadowRadius.toInt(), measuredWidth - shadowRadius.toInt(), measuredHeight - shadowRadius.toInt()), paint)
    }

    fun attachTo(parent: ViewGroup, layoutParam: ViewGroup.LayoutParams?): HorizontalCouponLayout {
        parent.addView(this, layoutParam)
        return this
    }

    fun attachTo(parent: ViewGroup): HorizontalCouponLayout {
        parent.addView(this)
        return this
    }
}