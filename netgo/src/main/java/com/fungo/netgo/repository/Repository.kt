package com.fungo.netgo.repository

import com.fungo.netgo.api.FungoRequest
import com.fungo.netgo.entity.BaseEntity
import com.fungo.netgo.retrofit.RetrofitManager

/**
 * @author Pinger
 * @since 2018/4/12 17:07
 */
class Repository {

    @Volatile
    private var mRepository: Repository? = null

    companion object {
        private var mInstance: Repository? = null
        val instance: Repository
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = Repository()
                }
                return mInstance!!
            }
    }

    private var mFungoRequest: FungoRequest<BaseEntity>? = null
    fun getFungoRequest(): FungoRequest<BaseEntity> {
        if (mFungoRequest == null) {
            mFungoRequest = FungoRequest(RetrofitManager.instance.getFungoApi())
        }
        return mFungoRequest!!
    }



    fun getNetDateStore(){
        val netDateStore = NetDataStore(getFungoRequest() )
    }








}