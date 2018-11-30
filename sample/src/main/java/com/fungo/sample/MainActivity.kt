package com.fungo.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fungo.netgo.NetGo
import com.fungo.netgo.subscribe.StringSubscriber
import com.fungo.sample.api.Api
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetGo.getInstance().init(application)


        initData()
    }

    private fun initData() {

        Api.getGankString(object : StringSubscriber() {
            override fun onNext(json: String) {
                textView.text = json
            }

        })

    }
}
