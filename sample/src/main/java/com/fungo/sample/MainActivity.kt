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
import com.fungo.sample.data.api.Api
import com.fungo.sample.data.bean.NovelDetailBean
import com.fungo.sample.subscribe.NovelSubscriber
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
//        Api.getGankData(object : JsonSubscriber<GankBean>() {
//            override fun onNext(data: GankBean) {
//                if (swipeRefreshLayout.isRefreshing) {
//                    swipeRefreshLayout.isRefreshing = false
//                }
//                mAdapter.setData(data.results)
//            }
//        })
//


//        Api.getNovelRankData(object : JsonSubscriber<NovelRankBean>() {
//            override fun onNext(data: NovelRankBean) {
//                if (swipeRefreshLayout.isRefreshing) {
//                    swipeRefreshLayout.isRefreshing = false
//                }
//                mAdapter.setData(data.male)
//            }
//        })


        Api.getNovelDetailData(object : NovelSubscriber<NovelDetailBean>() {
            override fun onSuccess(data: NovelDetailBean) {
                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }
                mAdapter.setData(data.bookList)

                println("---------> onNext ")
            }

            override fun onError(exception: ApiException) {
                super.onError(exception)
                println("---------> onError = " + exception.getMsg())
            }
        })


    }

    class MainAdapter : RecyclerView.Adapter<MainHolder>() {

        private var datas = listOf<NovelDetailBean.BookListBean>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            return MainHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.activity_list_item, null))
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            holder.title.text = datas[position].name
        }


        fun setData(data: List<NovelDetailBean.BookListBean>) {
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
