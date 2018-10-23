package com.fungo.netgo.request;

import java.util.Map;

/**
 * @author Pinger
 * @since 18-10-18 下午3:26
 *
 * 请求体对象,由子类去重写
 *
 */
public class BaseBodyRequest {


    public Map<String,Object> data;

    public BaseBodyRequest(Map<String,Object> data) {
        this.data = data;
    }

}
