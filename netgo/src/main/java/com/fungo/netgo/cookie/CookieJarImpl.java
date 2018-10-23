package com.fungo.netgo.cookie;

import com.fungo.netgo.cookie.store.CookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 给OkHttp设置Cookie，这里提供了两种实现方法
 * <p>
 * 使用方法：
 * <p>
 * 方法一：使用sp保持cookie，如果cookie不过期，则一直有效
 * builder.cookieJar(new CookieJarImpl(new PersistentCookieStore(this)));
 * <p>
 * 方法二：使用内存保持cookie，app退出后，cookie消失
 * builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
 */

public class CookieJarImpl implements CookieJar {

    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalStateException("cookieStore can not be null.");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
