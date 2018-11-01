package com.fungo.netgo.request.base;

import com.fungo.netgo.model.HttpParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Pinger
 * @since 18-10-23 下午6:37
 * <p>
 * 请求体的相关行为定义，给实现类实现
 */
public interface IBody<R> {

    R isMultipart(boolean isMultipart);

    R upRequestBody(RequestBody requestBody);

    R params(String key, File file);

    R addFileParams(String key, List<File> files);

    R addFileWrapperParams(String key, List<HttpParams.FileWrapper> fileWrappers);

    R params(String key, File file, String fileName);

    R params(String key, File file, String fileName, MediaType contentType);

    R upString(String string);

    R upString(String string, MediaType mediaType);

    R upJson(String json);

    R upJson(JSONObject jsonObject);

    R upJson(JSONArray jsonArray);

    R upJson(Object object);

    R upBytes(byte[] bs);

    R upBytes(byte[] bs, MediaType mediaType);

    R upFile(File file);

    R upFile(File file, MediaType mediaType);
}
