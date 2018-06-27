package com.javalong.customview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.javalong.customview.lib.layout.VerticalCouponLayout
import kotlinx.android.synthetic.main.activity_couponlayout.*

class CouponDetailLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupondetaillayout)
        initView()
    }

    private fun initView() {
        var lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        var couponLayout = VerticalCouponLayout.Builder()
                .indentRadius(20.0f)
                .mediumCircleCount(11)
                .mediumCircleHeight(20.0f)
                .mediumCircleWidth(10.0f)
                .shadowRadius(20.0f)
                .build(this)
        couponLayout.attachTo(flContent, lp)
        LayoutInflater.from(this).inflate(R.layout.item_verticalcouponlayout_top, couponLayout)
        LayoutInflater.from(this).inflate(R.layout.item_verticalcouponlayout_bottom, couponLayout)
    }
}