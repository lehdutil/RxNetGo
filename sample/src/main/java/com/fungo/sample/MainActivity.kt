package com.fungo.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fungo.sample.api.Api
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
    }

    @SuppressLint("CheckResult")
    private fun initData() {

        Api.getGankString().subscribe {
            textView?.text = it
        }


    }
}
