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
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.api.Api
import com.fungo.sample.data.bean.GankBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var mAdapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxNetGo.instance.init(application)


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

        Api.getGankData(object : JsonSubscriber<GankBean>() {

            override fun onNext(data: GankBean) {
                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }
                mAdapter.setData(data.results)
            }
        })
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
}
