package com.fungo.netgo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.fungo.netgo.NetGo;
import com.fungo.netgo.cache.CacheEntity;
import com.fungo.netgo.cache.CacheMode;
import com.fungo.netgo.model.HttpHeaders;
import com.fungo.netgo.model.HttpParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Pinger
 * @since 18-10-17 上午11:11
 */
public class HttpUtils {

    /**
     * 将传递进来的参数拼接成 url
     */
    public static String createUrlFromParams(String url, Map<String, Object> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                //对参数进行 utf-8 编码,防止头信息传中文
                String urlValue = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                sb.append(entry.getKey()).append("=").append(urlValue).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            NetLogger.printStackTrace(e);
        }
        return url;
    }

    /**
     * 通用的拼接请求头
     */
    public static Request.Builder appendHeaders(Request.Builder builder, HttpHeaders headers) {
        if (headers.headersMap.isEmpty()) return builder;
        Headers.Builder headerBuilder = new Headers.Builder();
        try {
            for (Map.Entry<String, Object> entry : headers.headersMap.entrySet()) {
                //对头信息进行 utf-8 编码,防止头信息传中文,这里暂时不编码,可能出现未知问题,如有需要自行编码
//                String headerValue = URLEncoder.encode(entry.getValue(), "UTF-8");
                headerBuilder.add(entry.getKey(), String.valueOf(entry.getKey()));
            }
        } catch (Exception e) {
            NetLogger.printStackTrace(e);
        }
        builder.headers(headerBuilder.build());
        return builder;
    }


    /**
     * 根据响应头或者url获取文件名
     */
    public static String getNetFileName(Response response, String url) {
        String fileName = getHeaderFileName(response);
        if (TextUtils.isEmpty(fileName)) fileName = getUrlFileName(url);
        if (TextUtils.isEmpty(fileName)) fileName = "unknownfile_" + System.currentTimeMillis();
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            NetLogger.printStackTrace(e);
        }
        return fileName;
    }

    /**
     * 解析文件头
     * Content-Disposition:attachment;filename=FileName.txt
     * Content-Disposition: attachment; filename*="UTF-8''%E6%9B%BF%E6%8D%A2%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A.pdf"
     */
    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response.header(HttpHeaders.HEAD_KEY_CONTENT_DISPOSITION);
        if (dispositionHeader != null) {
            //文件名可能包含双引号，需要去除
            dispositionHeader = dispositionHeader.replaceAll("\"", "");
            String split = "filename=";
            int indexOf = dispositionHeader.indexOf(split);
            if (indexOf != -1) {
                return dispositionHeader.substring(indexOf + split.length(), dispositionHeader.length());
            }
            split = "filename*=";
            indexOf = dispositionHeader.indexOf(split);
            if (indexOf != -1) {
                String fileName = dispositionHeader.substring(indexOf + split.length(), dispositionHeader.length());
                String encode = "UTF-8''";
                if (fileName.startsWith(encode)) {
                    fileName = fileName.substring(encode.length(), fileName.length());
                }
                return fileName;
            }
        }
        return null;
    }

    /**
     * 通过 ‘？’ 和 ‘/’ 判断文件名
     * http://mavin-manzhan.oss-cn-hangzhou.aliyuncs.com/1486631099150286149.jpg?x-oss-process=image/watermark,image_d2F0ZXJtYXJrXzIwMF81MC5wbmc
     */
    private static String getUrlFileName(String url) {
        String filename = null;
        String[] strings = url.split("/");
        for (String string : strings) {
            if (string.contains("?")) {
                int endIndex = string.indexOf("?");
                if (endIndex != -1) {
                    filename = string.substring(0, endIndex);
                    return filename;
                }
            }
        }
        if (strings.length > 0) {
            filename = strings[strings.length - 1];
        }
        return filename;
    }

    /**
     * 根据路径删除文件
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) return true;
        File file = new File(path);
        if (!file.exists()) return true;
        if (file.isFile()) {
            boolean delete = file.delete();
            NetLogger.e("deleteFile:" + delete + " path:" + path);
            return delete;
        }
        return false;
    }

    /**
     * 根据文件名获取MIME类型
     */
    public static MediaType guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        fileName = fileName.replace("#", "");   //解决文件名中含有#号异常的问题
        String contentType = fileNameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            return HttpParams.MEDIA_TYPE_STREAM;
        }
        return MediaType.parse(contentType);
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static void runOnUiThread(Runnable runnable) {
        NetGo.getInstance().getDelivery().post(runnable);
    }

    public static byte[] toByteArray(Object input) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(input);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            NetLogger.printStackTrace(e);
        } finally {
            closeQuietly(oos);
            closeQuietly(baos);
        }
        return null;
    }


    public static Object toObject(byte[] input) {
        if (input == null) return null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(input);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            NetLogger.printStackTrace(e);
        } finally {
            closeQuietly(ois);
            closeQuietly(bais);
        }
        return null;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Exception e) {
            NetLogger.printStackTrace(e);
        }
    }

    /**
     * 网络是否连接
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED;
    }


    /**
     * 网络是否是在WIFI环境
     */
    public static boolean isWifiNet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 拼接请求参数
     */
    public static String appendUrlParams(String url, Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            Uri.Builder builder = Uri.parse(url).buildUpon();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    builder.appendQueryParameter(entry.getKey(), String.valueOf(entry.getKey()));
                }
            }
            url = builder.build().toString();
        }
        return url;
    }


    /**
     * 生成类似表单的请求体
     */
    public static RequestBody generateMultipartRequestBody(HttpParams params, boolean isMultipart) {
        if (params.getFileParams().isEmpty() && !isMultipart) {
            //表单提交，没有文件
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (String key : params.getUrlParams().keySet()) {
                bodyBuilder.addEncoded(key, String.valueOf(params.getUrlParams().get(key)));
            }
            return bodyBuilder.build();
        } else {
            //表单提交，有文件
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            //拼接键值对
            if (!params.getUrlParams().isEmpty()) {
                for (Map.Entry<String, Object> entry : params.getUrlParams().entrySet()) {
                    multipartBodybuilder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            //拼接文件
            for (Map.Entry<String, List<HttpParams.FileWrapper>> entry : params.fileParamsMap.entrySet()) {
                List<HttpParams.FileWrapper> fileValues = entry.getValue();
                for (HttpParams.FileWrapper fileWrapper : fileValues) {
                    RequestBody fileBody = RequestBody.create(fileWrapper.contentType, fileWrapper.file);
                    multipartBodybuilder.addFormDataPart(entry.getKey(), fileWrapper.fileName, fileBody);
                }
            }
            return multipartBodybuilder.build();
        }
    }


    /**
     * 根据请求结果生成对应的缓存实体类，以下为缓存相关的响应头
     * Cache-Control: public                             响应被缓存，并且在多用户间共享
     * Cache-Control: private                            响应只能作为私有缓存，不能在用户之间共享
     * Cache-Control: no-cache                           提醒浏览器要从服务器提取文档进行验证
     * Cache-Control: no-store                           绝对禁止缓存（用于机密，敏感文件）
     * Cache-Control: max-age=60                         60秒之后缓存过期（相对时间）,优先级比Expires高
     * Date: Mon, 19 Nov 2012 08:39:00 GMT               当前response发送的时间
     * Expires: Mon, 19 Nov 2012 08:40:01 GMT            缓存过期的时间（绝对时间）
     * Last-Modified: Mon, 19 Nov 2012 08:38:01 GMT      服务器端文件的最后修改时间
     * ETag: "20b1add7ec1cd1:0"                          服务器端文件的ETag值
     * 如果同时存在cache-control和Expires，浏览器总是优先使用cache-control
     *
     * @param responseHeaders 返回数据中的响应头
     * @param data            解析出来的数据
     * @param cacheMode       缓存的模式
     * @param cacheKey        缓存的key
     * @return 缓存的实体类
     */
    public static <T> CacheEntity<T> createCacheEntity(Headers responseHeaders, T data, CacheMode cacheMode, String cacheKey) {

        long localExpire = 0;   // 缓存相对于本地的到期时间

        if (cacheMode == CacheMode.DEFAULT) {
            long date = HttpHeaders.getDate(responseHeaders.get(HttpHeaders.HEAD_KEY_DATE));
            long expires = HttpHeaders.getExpiration(responseHeaders.get(HttpHeaders.HEAD_KEY_EXPIRES));
            String cacheControl = HttpHeaders.getCacheControl(responseHeaders.get(HttpHeaders.HEAD_KEY_CACHE_CONTROL), responseHeaders.get(HttpHeaders.HEAD_KEY_PRAGMA));

            //没有缓存头控制，不需要缓存
            if (TextUtils.isEmpty(cacheControl) && expires <= 0) return null;

            long maxAge = 0;
            if (!TextUtils.isEmpty(cacheControl)) {
                StringTokenizer tokens = new StringTokenizer(cacheControl, ",");
                while (tokens.hasMoreTokens()) {
                    String token = tokens.nextToken().trim().toLowerCase(Locale.getDefault());
                    if (token.equals("no-cache") || token.equals("no-store")) {
                        //服务器指定不缓存
                        return null;
                    } else if (token.startsWith("max-age=")) {
                        try {
                            //获取最大缓存时间
                            maxAge = Long.parseLong(token.substring(8));
                            //服务器缓存设置立马过期，不缓存
                            if (maxAge <= 0) return null;
                        } catch (Exception e) {
                            NetLogger.printStackTrace(e);
                        }
                    }
                }
            }

            //获取基准缓存时间，优先使用response中的date头，如果没有就使用本地时间
            long now = System.currentTimeMillis();
            if (date > 0) now = date;

            if (maxAge > 0) {
                // Http1.1 优先验证 Cache-Control 头
                localExpire = now + maxAge * 1000;
            } else if (expires >= 0) {
                // Http1.0 验证 Expires 头
                localExpire = expires;
            }
        } else {
            localExpire = System.currentTimeMillis();
        }

        //将response中所有的头存入 HttpHeaders，原因是写入数据库的对象需要实现序列化，而ok默认的Header没有序列化
        HttpHeaders headers = new HttpHeaders();
        for (String headerName : responseHeaders.names()) {
            headers.put(headerName, responseHeaders.get(headerName));
        }

        //构建缓存实体对象
        CacheEntity<T> cacheEntity = new CacheEntity<>();
        cacheEntity.setKey(cacheKey);
        cacheEntity.setData(data);
        cacheEntity.setLocalExpire(localExpire);
        cacheEntity.setResponseHeaders(headers);
        return cacheEntity;
    }

    /**
     * 对每个请求添加默认的请求头，如果有缓存，并返回缓存实体对象
     * Cache-Control: max-age=0                            以秒为单位
     * If-Modified-Since: Mon, 19 Nov 2012 08:38:01 GMT    缓存文件的最后修改时间。
     * If-None-Match: "0693f67a67cc1:0"                    缓存文件的ETag值
     * Cache-Control: no-cache                             不使用缓存
     * Pragma: no-cache                                    不使用缓存
     * Accept-Language: zh-CN,zh;q=0.8                     支持的语言
     * User-Agent:                                         用户代理，它的信息包括硬件平台、系统软件、应用软件和用户个人偏好
     * Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36
     *
     * @param request     请求类
     * @param cacheEntity 缓存实体类
     * @param cacheMode   缓存模式
     */
    public static <T> void addCacheHeaders(com.fungo.netgo.request.base.Request request, CacheEntity<T> cacheEntity, CacheMode cacheMode) {
        //1. 按照标准的 http 协议，添加304相关请求头
        if (cacheEntity != null && cacheMode == CacheMode.DEFAULT) {
            HttpHeaders responseHeaders = cacheEntity.getResponseHeaders();
            if (responseHeaders != null) {
                Object eTag = responseHeaders.get(HttpHeaders.HEAD_KEY_E_TAG);
                if (eTag != null)
                    request.headers(HttpHeaders.HEAD_KEY_IF_NONE_MATCH, String.valueOf(eTag));
                long lastModified = HttpHeaders.getLastModified(String.valueOf(eTag));
                if (lastModified > 0)
                    request.headers(HttpHeaders.HEAD_KEY_IF_MODIFIED_SINCE, HttpHeaders.formatMillisToGMT(lastModified));
            }
        }
    }


}
