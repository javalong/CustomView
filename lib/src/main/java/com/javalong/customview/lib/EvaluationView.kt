package com.javalong.customview.lib

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.javalong.customview.lib.exception.CustomViewParamException

class EvaluationView : View {


    companion object {
        //默认五边形
        const val DEFAULT_POLYGON_NUM = 5
        //默认的线条的长度
        const val DEFAULT_SIDE_LENGTH = 200.0f
        //默认的线条的宽度(画笔的strokeWidth)
        const val DEFAULT_LINE_WIDTH = 2.0f
        //默认的ICON的大小
        const val DEFAULT_ICON_SIZE = 30.0f
        //最大分值
        const val DEFAULT_MAX_SCORE = 3
        //默认的数字的字体大小
        const val DEFAULT_TEXTSIZE = 20.0f
        //默认的字体与图片的距离
        const val DEFAULT_TEXT_TO_BITMAP_SPACE = 20.0f
        //默认shape到图形的距离
        const val DEFAULT_BITMAP_TO_SHAPE_SPACE = 50
        //默认线条的颜色
        val DEFAULT_LINE_COLOR = Color.parseColor("#CCAD74")
        //默认字体颜色
        val DEFAULT_TEXT_COLOR = Color.parseColor("#909090")
        //默认图形颜色(如果有渐变，优先采用渐变)
        val DEFAULT_SHAPE_COLOR = Color.parseColor("#CCAD74")
    }


    private lateinit var linePaint: Paint
    private lateinit var shapePaint: Paint
    private lateinit var textPaint: Paint
    private var iconArr: SparseArray<Drawable> = SparseArray()
    private var titleArr: SparseArray<String> = SparseArray()
    private var scoreArr: SparseArray<Int> = SparseArray()
    private var radian = Math.PI * 2 / DEFAULT_POLYGON_NUM

    var shaderHandler: ShaderHandler? = null
    private var sideLength = DEFAULT_SIDE_LENGTH
    private var bitmapToShapeSpace = arrayOf(DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE)
    private var polygonNum = DEFAULT_POLYGON_NUM
    private var iconSize = DEFAULT_ICON_SIZE
    private var textToBitmapSpace = DEFAULT_TEXT_TO_BITMAP_SPACE
    private var maxScore = DEFAULT_MAX_SCORE
    private var lineColor = DEFAULT_LINE_COLOR
    private var lineWidth = DEFAULT_LINE_WIDTH
    private var textColor = DEFAULT_TEXT_COLOR
    private var textSize = DEFAULT_TEXTSIZE
    private var shapeColor = DEFAULT_SHAPE_COLOR


    class Builder {
        private var sideLength = DEFAULT_SIDE_LENGTH
        private var bitmapToShapeSpace = arrayOf(DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE, DEFAULT_BITMAP_TO_SHAPE_SPACE)
        private var polygonNum = DEFAULT_POLYGON_NUM
        private var iconSize = DEFAULT_ICON_SIZE
        private var textToBitmapSpace = DEFAULT_TEXT_TO_BITMAP_SPACE
        private var maxScore = DEFAULT_MAX_SCORE
        private var lineColor = DEFAULT_LINE_COLOR
        private var lineWidth = DEFAULT_LINE_WIDTH
        private var textColor = DEFAULT_TEXT_COLOR
        private var textSize = DEFAULT_TEXTSIZE
        private var shapeColor = DEFAULT_SHAPE_COLOR
        private var shaderHandler: ShaderHandler? = null

        //多边形的中心到每个点长度,相当于半径
        fun sideLength(sideLength: Float): Builder {
            this.sideLength = sideLength
            return this
        }

        //几边形
        fun polygonNum(polygonNum: Int): Builder {
            this.polygonNum = polygonNum
            return this
        }

        //icon大小
        fun iconSize(iconSize: Float): Builder {
            this.iconSize = iconSize
            return this
        }

        //文字距离icon的距离
        fun textToBitmapSpace(textToBitmapSpace: Float): Builder {
            this.textToBitmapSpace = textToBitmapSpace
            return this
        }

        //最大分值
        fun maxScore(maxScore: Int): Builder {
            this.maxScore = maxScore
            return this
        }

        //线的颜色
        fun lineColor(lineColor: Int): Builder {
            this.lineColor = lineColor
            return this
        }

        //线的宽度
        fun lineWidth(lineWidth: Float): Builder {
            this.lineWidth = lineWidth
            return this
        }

        //渐变填充器
        fun shaderHandler(shaderHandler: ShaderHandler): Builder {
            this.shaderHandler = shaderHandler
            return this
        }

        //字体颜色
        fun textColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        //字体大小
        fun textSize(textSize: Float): Builder {
            this.textSize = textSize
            return this
        }

        //中心的填充的颜色
        fun shapeColor(shapeColor: Int): Builder {
            this.shapeColor = shapeColor
            return this
        }

        //icon距离多边形的距离，数组，代表每个点距离多边形的距离s
        fun bitmapToShapeSpace(bitmapToShapeSpace: Array<Int>): Builder {
            this.bitmapToShapeSpace = bitmapToShapeSpace
            return this
        }

        fun build(context: Context): EvaluationView {
            var view = EvaluationView(context)
            view.shaderHandler = shaderHandler
            view.sideLength = sideLength
            view.bitmapToShapeSpace = bitmapToShapeSpace
            view.polygonNum = polygonNum
            view.iconSize = iconSize
            view.textToBitmapSpace = textToBitmapSpace
            view.maxScore = maxScore
            view.lineColor = lineColor
            view.lineWidth = lineWidth
            view.textColor = textColor
            view.textSize = textSize
            view.shapeColor = shapeColor
            return view
        }
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        var typeArr = context.obtainStyledAttributes(attrs, R.styleable.jcl_Evalueation)
        sideLength = typeArr.getDimension(R.styleable.jcl_Evalueation_jcl_Evalueation_sideLength, DEFAULT_SIDE_LENGTH)
        lineColor = typeArr.getColor(R.styleable.jcl_Evalueation_jcl_Evalueation_lineColor, DEFAULT_LINE_COLOR)
        lineWidth = typeArr.getDimension(R.styleable.jcl_Evalueation_jcl_Evalueation_lineWidth, DEFAULT_LINE_WIDTH)
        textColor = typeArr.getColor(R.styleable.jcl_Evalueation_jcl_Evalueation_textColor, DEFAULT_TEXT_COLOR)
        shapeColor = typeArr.getColor(R.styleable.jcl_Evalueation_jcl_Evalueation_shapeColor, DEFAULT_SHAPE_COLOR)
        textSize = typeArr.getDimension(R.styleable.jcl_Evalueation_jcl_Evalueation_textSize, DEFAULT_TEXTSIZE)
        iconSize = typeArr.getDimension(R.styleable.jcl_Evalueation_jcl_Evalueation_iconSize, DEFAULT_ICON_SIZE)
        maxScore = typeArr.getInteger(R.styleable.jcl_Evalueation_jcl_Evalueation_maxScore, DEFAULT_MAX_SCORE)
        var resId = typeArr.getResourceId(R.styleable.jcl_Evalueation_jcl_Evalueation_bitmapToShapeSpace, -1)
        if (resId != -1) {
            bitmapToShapeSpace = resources.getIntArray(resId).toTypedArray()
        }
        textToBitmapSpace = typeArr.getDimension(R.styleable.jcl_Evalueation_jcl_Evalueation_textToBitmapSpace, DEFAULT_TEXT_TO_BITMAP_SPACE)
        polygonNum = typeArr.getInteger(R.styleable.jcl_Evalueation_jcl_Evalueation_polygonNum, DEFAULT_POLYGON_NUM)
        radian = Math.PI * 2 / polygonNum
        typeArr.recycle()
        init()
    }

    private fun init() {
        linePaint = Paint()
        linePaint.strokeWidth = lineWidth
        linePaint.isAntiAlias = true
        linePaint.style = Paint.Style.STROKE
        linePaint.color = lineColor


        shapePaint = Paint()
        shapePaint.isAntiAlias = true
        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = textColor
        this.textSize = this.textSize
        textPaint.textSize = this.textSize
        this.iconSize = this.iconSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var centerX = width / 2.0f
        var centerY = height / 2.0f
        canvas.save()
        canvas.translate(centerX, centerY)
        var originPointArr = ArrayList<PointF>()
        //几边形就画几个点，以顶部的第一个点为起始点
        for (i in 0 until polygonNum) {
            originPointArr.add(PointF(sideLength * Math.cos(radian * i + Math.PI / 2).toFloat(), -sideLength * Math.sin(radian * i + Math.PI / 2).toFloat()))
        }

        var path = Path()
        //画每条线
        for (i in maxScore downTo 0) {
            path.moveTo(originPointArr[0].x * i / maxScore, originPointArr[0].y * i / maxScore)
            for (j in 1 until polygonNum) {
                path.lineTo(originPointArr[j].x * i / maxScore, originPointArr[j].y * i / maxScore)
            }
            path.close()
            canvas.drawPath(path, linePaint)
        }

        for (j in 0 until polygonNum) {
            path.moveTo(0f, 0f)
            path.lineTo(originPointArr[j].x, originPointArr[j].y)
        }
        canvas.drawPath(path, linePaint)


        if (iconArr == null || scoreArr.size() < polygonNum) return
        var pointArr = ArrayList<PointF>()
        //根据分数重新生成5个点
        for (i in 0 until polygonNum) {
            pointArr.add(PointF(originPointArr[i].x * scoreArr!![i] / maxScore, originPointArr[i].y * scoreArr!![i] / maxScore))
        }


        for (i in 1..polygonNum) {
            var arc = Path()
            arc.moveTo(0f, 0f)
            arc.lineTo(pointArr[i - 1].x, pointArr[i - 1].y)
            arc.lineTo(pointArr[i % polygonNum].x, pointArr[i % polygonNum].y)
            arc.close()
            if (shaderHandler != null) {
                var points = shaderHandler!!.getShaderPoints(i, radian, sideLength)
                var colors = shaderHandler!!.getShaderColor(i)
                shapePaint.shader = LinearGradient(points[0].x, points[0].y, points[1].x, points[1].y, colors[0], colors[1], Shader.TileMode.CLAMP)
            } else {
                shapePaint.style = Paint.Style.FILL
                shapePaint.color = DEFAULT_SHAPE_COLOR
            }

            canvas.drawPath(arc, shapePaint)
        }


        for (i in 0 until polygonNum) {
            var titlePoint = getTitlePoint(i, originPointArr[i]);
            canvas.drawText(titleArr!![i], titlePoint.x, titlePoint.y, textPaint)
        }

        for (i in 0 until polygonNum) {
            var iconPoint = getIconPoint(i, originPointArr[i])
            iconArr!![i].setBounds(iconPoint.x.toInt(), iconPoint.y.toInt(), iconPoint.x.toInt() + this.iconSize.toInt(), iconPoint.y.toInt() + this.iconSize.toInt())
            iconArr!![i].draw(canvas)
        }

        canvas.restore()
    }


    //获取icon的绘制起点
    private fun getIconPoint(index: Int, p: PointF): PointF {
        return PointF((Math.cos(index * radian + Math.PI / 2) * (bitmapToShapeSpace[index] + sideLength)).toFloat() - iconSize / 2, -(Math.sin(index * radian + Math.PI / 2) * (bitmapToShapeSpace[index] + sideLength)).toFloat() - iconSize / 2)
    }

    //获取title的绘制起点
    private fun getTitlePoint(index: Int, p: PointF): PointF {
        var poi = getIconPoint(index, p)
        var textWidth = textPaint.measureText(titleArr!![index])
        return PointF(poi.x - textWidth / 2 + iconSize / 2, poi.y + textSize + textToBitmapSpace + iconSize / 2)
    }

    fun initData(iconArr: SparseArray<Drawable>, titleArr: SparseArray<String>, scoreArr: SparseArray<Int>) {
        if (iconArr.size() != polygonNum) {
            throw CustomViewParamException("${this.javaClass.simpleName} iconArr.size()!=${polygonNum}")
        }
        if (titleArr.size() != polygonNum) {
            throw CustomViewParamException("${this.javaClass.simpleName} titleArr.size()!=${polygonNum}")
        }
        if (scoreArr.size() != polygonNum) {
            throw CustomViewParamException("${this.javaClass.simpleName} scoreArr.size()!=${polygonNum}")
        }
        this.iconArr = iconArr
        this.titleArr = titleArr
        this.scoreArr = scoreArr
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun attachTo(parent: ViewGroup, layoutParam: ViewGroup.LayoutParams?): EvaluationView {
        parent.addView(this, layoutParams)
        return this
    }

    fun attachTo(parent: ViewGroup): EvaluationView {
        parent.addView(this)
        return this
    }

    interface ShaderHandler {
        fun getShaderColor(index: Int): Array<Int>
        fun getShaderPoints(index: Int, radian: Double, sideLength: Float): Array<PointF>
    }
}