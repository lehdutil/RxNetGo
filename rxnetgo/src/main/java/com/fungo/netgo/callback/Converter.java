package com.fungo.netgo.callback;

import okhttp3.ResponseBody;

/**
 * @author Pinger
 * @since 18-10-23 上午11:29
 * <p>
 * 转换器，提供给子类转换数据格式
 */
public interface Converter<T> {

    /**
     * 将服务器返回的Response，抓换成想要的数据实体
     */
    T convertResponse(ResponseBody response) throws Exception;
}
