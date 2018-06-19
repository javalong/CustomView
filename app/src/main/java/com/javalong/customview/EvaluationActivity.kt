package com.javalong.customview

import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.javalong.customview.lib.EvaluationView
import kotlinx.android.synthetic.main.activity_evaluation_view.*

class EvaluationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation_view)
        initView()
    }

    private fun initView() {
        evaluationView.shaderHandler = PointShaderHandler()
        var resId = arrayOf(R.mipmap.xiaofei, R.mipmap.caifuzhishu, R.mipmap.lvyue, R.mipmap.xingwei)
        var titles = arrayOf("TEST1", "TEST2", "TEST3", "TEST4")
        var scores = arrayOf(3, 3, 3, 3)
        var iconArr = SparseArray<Drawable>()
        var titleArr = SparseArray<String>()
        var scoreArr = SparseArray<Int>()
        for (i in 0 until titles.size) {
            Glide.with(this)
                    .load(resId[i])
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(drawable: Drawable, p1: Transition<in Drawable>?) {
                            iconArr.put(i, drawable)
                            titleArr.put(i, titles[i])
                            scoreArr.put(i, scores[i])
                            if (iconArr != null && iconArr!!.size() == titles.size) {
                                evaluationView.initData(iconArr, titleArr, scoreArr)
                            }
                        }
                    })
        }


        var resId2 = arrayOf(R.mipmap.xiaofei, R.mipmap.caifuzhishu, R.mipmap.lvyue, R.mipmap.xingwei, R.mipmap.xinyong)
        var titles2 = arrayOf("TEST1", "TEST2", "TEST3", "TEST4", "TEST5")
        var scores2 = arrayOf(3, 2, 1, 3, 2)
        var iconArr2 = SparseArray<Drawable>()
        var titleArr2 = SparseArray<String>()
        var scoreArr2 = SparseArray<Int>()

        for (i in 0 until titles2.size) {
            Glide.with(this)
                    .load(resId2[i])
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(drawable: Drawable, p1: Transition<in Drawable>?) {
                            iconArr2.put(i, drawable)
                            titleArr2.put(i, titles2[i])
                            scoreArr2.put(i, scores2[i])
                            if (iconArr2 != null && iconArr2!!.size() == titles2.size) {
                                EvaluationView.Builder()
                                        .bitmapToShapeSpace(arrayOf(70, 50, 50, 50, 50))
                                        .polygonNum(5)
                                        .maxScore(4)
                                        .iconSize(60.0f)
                                        .textToBitmapSpace(40.0f)
                                        .lineColor(Color.parseColor("#00FF00"))
                                        .lineWidth(4.0f)
                                        .shaderHandler(PointShaderHandler())
                                        .build(this@EvaluationActivity).attachTo(flContent)
                                        .initData(iconArr2, titleArr2, scoreArr2)
                            }
                        }
                    })
        }

    }

    class PointShaderHandler : EvaluationView.ShaderHandler {
        //获取渐变的启始颜色和结束颜色
        override fun getShaderColor(index: Int): Array<Int> {
            var startColor = Color.parseColor("#FFF9EA")
            var endColor = Color.parseColor("#E5C67B")
            return arrayOf(startColor, endColor)
        }

        //渐变的起始点和结束点
        override fun getShaderPoints(index: Int, radian: Double, sideLength: Float): Array<PointF> {
            var startPointF = PointF(0.0f, 0.0f)
            var p1 = PointF(sideLength * Math.cos(radian * (index - 1) + Math.PI / 2).toFloat(), -sideLength * Math.sin(radian * ((index - 1)) + Math.PI / 2).toFloat())
            var p2 = PointF(sideLength * Math.cos(radian * index + Math.PI / 2).toFloat(), -sideLength * Math.sin(radian * index + Math.PI / 2).toFloat())
            var p3 = PointF((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
            return arrayOf(startPointF, p3)
        }
    }
}