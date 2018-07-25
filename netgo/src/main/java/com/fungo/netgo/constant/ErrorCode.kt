package com.fungo.netgo.constant

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     错误代码
 */
object ErrorCode {
    const val SERVER_SUCCESS = 0//请求成功
    const val SERVER_ERROR = 500//服务器异常

    //本地自定义错误
    const val JSON_DATA_ERROR = 901  //receive a jsonException when try to analysis a string to bean
    const val HTTP_SERIES_ERROR = 904  //ConnectException ||SocketTimeoutException||TimeoutException|| HttpException
    const val NETWORK_UN_CONNECTED = 907//无网络

    const val NETWORK_DATA_CONVERT_ERROR = 1008 // 数据转换异常


}