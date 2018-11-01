package com.fungo.netgo.callback;

import com.fungo.netgo.utils.GsonUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Pinger
 * @since 18-10-23 下午6:11
 * <p>
 * 将相应数据转换成具体的实体对象
 */
public abstract class JsonCallBack<T> extends BaseCallBack<T> {


    @Override
    public T convertResponse(ResponseBody response) throws IOException {
        Type genType = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];

        return GsonUtils.INSTANCE.fromJson(response.string(), type);
    }
}
