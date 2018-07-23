package com.javalong.customview

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS
import kotlinx.android.synthetic.main.activity_selectcoupon.*

class SelectCouponActivity : AppCompatActivity() {
    var str = arrayOf("0.1折", "立减100元")
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectcoupon)
        selectCouponView.couponComment = str[i % 2]

        btToggle.setOnClickListener({
            selectCouponView.couponComment = str[i % 2]
            i++

            selectCouponView.requestLayout()
        })
    }
}