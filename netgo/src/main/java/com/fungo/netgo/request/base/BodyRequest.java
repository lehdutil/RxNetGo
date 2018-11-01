package com.fungo.netgo.request.base;

import com.fungo.netgo.model.HttpParams;
import com.fungo.netgo.utils.GsonUtils;
import com.fungo.netgo.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Pinger
 * @since 18-10-23 下午3:16
 * <p>
 * 有请求体的请求，一般为post
 */
public abstract class BodyRequest<T, R extends Request> extends Request<T, R> implements IBody<R> {

    protected transient MediaType mediaType;        //上传的MIME类型
    protected String content;                       //上传的文本内容
    protected byte[] bs;                            //上传的字节数据
    protected transient File file;                  //单纯的上传一个文件

    protected boolean isMultipart = false;  //是否强制使用 multipart/form-data 表单上传
    protected RequestBody requestBody;

    public BodyRequest(String url, ApiService service) {
        super(url, service);
    }

    @Override
    public R isMultipart(boolean isMultipart) {
        this.isMultipart = isMultipart;
        return (R) this;
    }


    @Override
    public R params(String key, File file) {
        mParams.put(key, file);
        return (R) this;
    }

    @Override
    public R addFileParams(String key, List<File> files) {
        mParams.putFileParams(key, files);
        return (R) this;
    }

    @Override
    public R addFileWrapperParams(String key, List<HttpParams.FileWrapper> fileWrappers) {
        mParams.putFileWrapperParams(key, fileWrappers);
        return (R) this;
    }

    @Override
    public R params(String key, File file, String fileName) {
        mParams.put(key, file, fileName);
        return (R) this;
    }

    @Override
    public R params(String key, File file, String fileName, MediaType contentType) {
        mParams.put(key, file, fileName, contentType);
        return (R) this;
    }

    @Override
    public R upRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upString(String string) {
        this.content = string;
        this.mediaType = HttpParams.MEDIA_TYPE_PLAIN;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     * 该方法用于定制请求content-type
     */
    @Override
    public R upString(String string, MediaType mediaType) {
        this.content = string;
        this.mediaType = mediaType;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upJson(String json) {
        this.content = json;
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upJson(JSONObject jsonObject) {
        this.content = jsonObject.toString();
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upJson(JSONArray jsonArray) {
        this.content = jsonArray.toString();
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return (R) this;
    }

    @Override
    public R upJson(Object object) {
        this.content = GsonUtils.INSTANCE.toJson(object);
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return (R) this;
    }


    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upBytes(byte[] bs) {
        this.bs = bs;
        this.mediaType = HttpParams.MEDIA_TYPE_STREAM;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upBytes(byte[] bs, MediaType mediaType) {
        this.bs = bs;
        this.mediaType = mediaType;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upFile(File file) {
        this.file = file;
        this.mediaType = HttpUtils.guessMimeType(file.getName());
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @Override
    public R upFile(File file, MediaType mediaType) {
        this.file = file;
        this.mediaType = mediaType;
        return (R) this;
    }

    @Override
    public RequestBody generateRequestBody() {
        if (requestBody != null)
            return requestBody;                                                // 自定义的请求体
        if (content != null && mediaType != null)
            return RequestBody.create(mediaType, content);                     // 上传字符串数据
        if (bs != null && mediaType != null)
            return RequestBody.create(mediaType, bs);                          // 上传字节数组
        if (file != null && mediaType != null)
            return RequestBody.create(mediaType, file);                        // 上传一个文件
        return HttpUtils.generateMultipartRequestBody(mParams, isMultipart);
    }

}
