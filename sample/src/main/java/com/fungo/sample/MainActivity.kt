package com.fungo.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fungo.netgo.NetGo
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.netgo.subscribe.StringSubscriber
import com.fungo.sample.data.api.Api
import com.fungo.sample.data.bean.GankBean
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Subscription

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetGo.getInstance().init(application)


        initData()
    }

    private fun initData() {

        Api.getGankString(object : JsonSubscriber<GankBean>() {

            override fun onStart() {
                println("------------> onStart")

                super.onStart()
            }

            override fun onNext(json: GankBean) {
                println("------------> onNext")

                textView.text = json.toString()
            }


            override fun onComplete() {
                println("------------> onComplete")

                super.onComplete()
            }

            override fun onError(exception: ApiException) {
                println("------------> onError")
                super.onError(exception)
            }



        })

    }
}
