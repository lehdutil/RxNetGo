package com.fungo.netgo.callback;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * @author Pinger
 * @since 18-10-23 下午4:31
 */
public abstract class StringCallBack extends BaseCallBack<String> {


    @Override
    public String convertResponse(ResponseBody response) throws IOException {
        String result = response.string();
        response.close();
        return result;
    }
}
