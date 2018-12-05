package com.fungo.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fungo.netgo.RxNetGo
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.api.Api
import com.fungo.sample.data.bean.GankBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var mAdapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxNetGo.getInstance().init(application)


        initView()
        initData()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = MainAdapter()
        recyclerView.adapter = mAdapter

        swipeRefreshLayout.setOnRefreshListener {
            initData()
        }

    }

    private fun initData() {
        Api.test("1", Subscriber())
        Api.test("2", Subscriber())
        Api.test("3", Subscriber())
        Api.test("4", Subscriber())
        Api.test("5", Subscriber())
        Api.test("6", Subscriber())
        Api.test("7", Subscriber())
        Api.test("8", Subscriber())
        Api.test("9", Subscriber())
        Api.test("10", Subscriber())
        Api.test("11", Subscriber())
        Api.test("12", Subscriber())
        Api.test("13", Subscriber())
        Api.test("14", Subscriber())
        Api.test("15", Subscriber())
        Api.test("16", Subscriber())
        Api.test("17", Subscriber())
        Api.test("18", Subscriber())
        Api.test("19", Subscriber())
        Api.test("20", Subscriber())
        Api.test("21", Subscriber())
        Api.test("22", Subscriber())
        Api.test("23", Subscriber())
        Api.test("24", Subscriber())
        Api.test("25", Subscriber())
        Api.test("26", Subscriber())
        Api.test("27", Subscriber())
        Api.test("28", Subscriber())
        Api.test("29", Subscriber())
        Api.test("30", Subscriber())
        Api.test("31", Subscriber())
        Api.test("32", Subscriber())
        Api.test("33", Subscriber())
        Api.test("34", Subscriber())
        Api.test("35", Subscriber())
        Api.test("36", Subscriber())
        Api.test("37", Subscriber())
        Api.test("38", Subscriber())
        Api.test("39", Subscriber())
        Api.test("40", Subscriber())
    }

    inner class Subscriber : JsonSubscriber<GankBean>() {

        override fun onNext(data: GankBean) {
//            if (swipeRefreshLayout.isRefreshing) {
//                swipeRefreshLayout.isRefreshing = false
//            }
//            mAdapter.setData(data.results)

            println("------请求成功")


        }

        override fun onError(exception: ApiException) {
            println("------请求异常: ${exception.getMsg()}")
        }
    }


    class MainAdapter : RecyclerView.Adapter<MainHolder>() {

        private var datas = listOf<GankBean.ResultsBean>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            return MainHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.activity_list_item, null))
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            holder.title.text = datas[position].desc
        }


        fun setData(data: List<GankBean.ResultsBean>) {
            this.datas = data
            this.notifyDataSetChanged()
        }

    }


    class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(android.R.id.text1)!!
    }

    override fun onDestroy() {
        super.onDestroy()
        RxNetGo.getInstance().dispose()
    }
}
