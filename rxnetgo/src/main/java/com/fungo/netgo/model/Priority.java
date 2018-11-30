package com.fungo.netgo.model;

/**
 * @author Pinger
 * @since 18-10-17 下午2:36
 * 网络请求不同接口的优先级处理
 */
public interface Priority {

    int UI_TOP = Integer.MAX_VALUE;
    int UI_NORMAL = 1000;
    int UI_LOW = 100;
    int DEFAULT = 0;
    int BG_TOP = -100;
    int BG_NORMAL = -1000;
    int BG_LOW = Integer.MIN_VALUE;

}
