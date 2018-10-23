package com.fungo.netgo.model;


/**
 * @author Pinger
 * @since 18-10-14 上午09:12
 * <p>
 * 网络请求返回的数据实体父类，让子类实现请求是否成功和后台异常信息的方法
 */

public interface IModel {

    /**
     * 根据后台定义的code，判断加载是否成功
     */
    boolean isSuccess();

    /**
     * 后台返回的错误信息
     */
    String getErrorMsg();

    /**
     * 获取服务端定义的错误码
     */
    int getCode();
}
