package com.javalong.customview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.javalong.customview.lib.view.StepView
import kotlinx.android.synthetic.main.activity_step_view.*

class VerticalStepViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_step_view)
        initView()
    }

    private fun initView() {
        var bottomStepView = StepView.Builder(this)
                .stepCount(5)
                .numTextSize(60.0f)
                .circleSize(80.0f)
                .orientation(StepView.VERTICAL)
                .build().attachTo(flContent,FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT))
        bottomBtNext.setOnClickListener({
            bottomStepView.next()
        })
        bottomBtPre.setOnClickListener({
            bottomStepView.pre()
        })

    }
}
