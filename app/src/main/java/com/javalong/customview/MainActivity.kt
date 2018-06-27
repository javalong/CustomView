package com.javalong.customview

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btStepView.setOnClickListener({
            startActivity(Intent(this, StepViewActivity::class.java))
        })
        btEvaluationView.setOnClickListener({
            startActivity(Intent(this, EvaluationActivity::class.java))
        })
        btCouponLayout.setOnClickListener({
            startActivity(Intent(this, CouponLayoutActivity::class.java))
        })
        btCouponDetailLayout.setOnClickListener({
            startActivity(Intent(this, CouponDetailLayoutActivity::class.java))
        })
    }

}
