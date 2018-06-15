package com.javalong.customview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.javalong.customview.lib.StepView
import kotlinx.android.synthetic.main.activity_step_view.*

class StepViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_view)
        initView()
    }

    private fun initView() {
        var bottomStepView = StepView.Builder(this)
                .stepCount(4)
                .numTextSize(60.0f)
                .circleSize(80.0f)
                .build().attachTo(flContent)
        bottomBtNext.setOnClickListener({
            bottomStepView.next()
        })
        bottomBtPre.setOnClickListener({
            bottomStepView.pre()
        })

        topBtNext.setOnClickListener({
            topStempView.next()
        })

        topBtPre.setOnClickListener({
            topStempView.pre()
        })

    }
}
