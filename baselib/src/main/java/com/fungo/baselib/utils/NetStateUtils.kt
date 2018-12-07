package com.fungo.baselib.utils

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException


/**
 * 网络状态工具类
 */
object NetStateUtils {

    // 网络类型 - 无连接
    private const val NETWORK_TYPE_NO_CONNECTION = -1231545315

    private const val NETWORK_TYPE_WIFI = "wifi"
    private const val NETWORK_TYPE_3G = "eg"
    private const val NETWORK_TYPE_2G = "2g"
    private const val NETWORK_TYPE_WAP = "wap"
    private const val NETWORK_TYPE_UNKNOWN = "unknown"
    private const val NETWORK_TYPE_DISCONNECT = "disconnect"

    /**
     * 获取本机IP地址
     *
     * @return null：没有网络连接
     */
    val ipAddress: String?
        get() {
            try {
                var nerworkInterface: NetworkInterface
                var inetAddress: InetAddress
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    nerworkInterface = en.nextElement()
                    val enumIpAddr = nerworkInterface.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress) {
                            return inetAddress.hostAddress
                        }
                    }
                }
                return null
            } catch (ex: SocketException) {
                ex.printStackTrace()
                return null
            }

        }


    /**
     * Get network type
     *
     * @param context mContext
     * @return 网络状态
     */
    fun getNetworkType(context: Context): Int {
        val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.type ?: -1
    }


    /**
     * Get network type name
     *
     * @param context mContext
     * @return NetworkTypeName
     */
    fun getNetworkTypeName(context: Context): String {
        val manager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = manager.activeNetworkInfo
        var type = NETWORK_TYPE_DISCONNECT
        if (networkInfo == null) {
            return type
        }
        if (networkInfo.isConnected) {
            val typeName = networkInfo.typeName
            type = when (typeName) {
                "WIFI" -> NETWORK_TYPE_WIFI
                "MOBILE" -> {
                    val proxyHost = android.net.Proxy.getDefaultHost()
                    if (TextUtils.isEmpty(proxyHost))
                        if (isFastMobileNetwork(context))
                            NETWORK_TYPE_3G
                        else
                            NETWORK_TYPE_2G
                    else
                        NETWORK_TYPE_WAP
                }
                else -> NETWORK_TYPE_UNKNOWN
            }
        }
        return type
    }


    /**
     * Whether is fast mobile network
     *
     * @param context mContext
     * @return FastMobileNetwork
     */
    private fun isFastMobileNetwork(context: Context): Boolean {
        val telephonyManager = context.getSystemService(
                Context.TELEPHONY_SERVICE) as TelephonyManager
        when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_1xRTT -> return false
            TelephonyManager.NETWORK_TYPE_CDMA -> return false
            TelephonyManager.NETWORK_TYPE_EDGE -> return false
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> return true
            TelephonyManager.NETWORK_TYPE_EVDO_A -> return true
            TelephonyManager.NETWORK_TYPE_GPRS -> return false
            TelephonyManager.NETWORK_TYPE_HSDPA -> return true
            TelephonyManager.NETWORK_TYPE_HSPA -> return true
            TelephonyManager.NETWORK_TYPE_HSUPA -> return true
            TelephonyManager.NETWORK_TYPE_UMTS -> return true
            TelephonyManager.NETWORK_TYPE_EHRPD -> return true
            TelephonyManager.NETWORK_TYPE_EVDO_B -> return true
            TelephonyManager.NETWORK_TYPE_HSPAP -> return true
            TelephonyManager.NETWORK_TYPE_IDEN -> return false
            TelephonyManager.NETWORK_TYPE_LTE -> return true
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> return false
            else -> return false
        }
    }


    /**
     * 获取当前网络的状态
     *
     * @param context 上下文
     * @return 当前网络的状态。
     * 具体类型可参照NetworkInfo.State.CONNECTED、NetworkInfo.State.CONNECTED.DISCONNECTED等字段。当前没有网络连接时返回null
     */
    fun getCurrentNetworkState(context: Context): NetworkInfo.State? {
        val networkInfo = (context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return networkInfo?.state
    }


    /**
     * 获取当前网络的类型
     *
     * @param context 上下文
     * @return 当前网络的类型。具体类型可参照ConnectivityManager中的TYPE_BLUETOOTH、TYPE_MOBILE、TYPE_WIFI等字段。当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    fun getCurrentNetworkType(context: Context): Int {
        val networkInfo = (context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return networkInfo?.type ?: NETWORK_TYPE_NO_CONNECTION
    }


    /**
     * 获取当前网络的具体类型
     *
     * @param context 上下文
     * @return 当前网络的具体类型。具体类型可参照TelephonyManager中的NETWORK_TYPE_1xRTT、NETWORK_TYPE_CDMA等字段。当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    fun getCurrentNetworkSubtype(context: Context): Int {
        val networkInfo = (context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return networkInfo?.subtype ?: NETWORK_TYPE_NO_CONNECTION
    }


    /**
     * 判断当前网络是否已经连接
     *
     * @param context 上下文
     * @return 当前网络是否已经连接。false：尚未连接
     */
    fun isConnected(context: Context): Boolean {
        return getCurrentNetworkState(context) == NetworkInfo.State.CONNECTED
    }


    /**
     * 判断当前网络是否正在连接
     *
     * @param context 上下文
     * @return 当前网络是否正在连接
     */
    fun isConnecting(context: Context): Boolean {
        return getCurrentNetworkState(context) == NetworkInfo.State.CONNECTING
    }


    /**
     * 判断当前网络是否已经断开
     *
     * @param context 上下文
     * @return 当前网络是否已经断开
     */
    fun isDisconnected(context: Context): Boolean {
        return getCurrentNetworkState(context) == NetworkInfo.State.DISCONNECTED
    }


    /**
     * 判断当前网络是否正在断开
     *
     * @param context 上下文
     * @return 当前网络是否正在断开
     */
    fun isDisconnecting(context: Context): Boolean {
        return getCurrentNetworkState(context) == NetworkInfo.State.DISCONNECTING
    }


    /**
     * 判断当前网络是否已经暂停
     *
     * @param context 上下文
     * @return 当前网络是否已经暂停
     */
    fun isSuspended(context: Context): Boolean {
        return getCurrentNetworkState(context) == NetworkInfo.State.SUSPENDED
    }


    /**
     * 判断当前网络是否处于未知状态中
     *
     * @param context 上下文
     * @return 当前网络是否处于未知状态中
     */
    fun isUnknown(context: Context): Boolean {
        return getCurrentNetworkState(context) == NetworkInfo.State.UNKNOWN
    }


    /**
     * 判断当前网络的类型是否是蓝牙
     *
     * @param context 上下文
     * @return 当前网络的类型是否是蓝牙。false：当前没有网络连接或者网络类型不是蓝牙
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun isBluetooth(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            false
        } else {
            getCurrentNetworkType(context) == ConnectivityManager.TYPE_BLUETOOTH
        }
    }


    /**
     * 判断当前网络的类型是否是移动网络
     *
     * @param context 上下文
     * @return 当前网络的类型是否是移动网络。false：当前没有网络连接或者网络类型不是移动网络
     */
    fun isMobile(context: Context): Boolean {
        return getCurrentNetworkType(context) == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * 判断当前网络的类型是否是Wifi
     *
     * @param context 上下文
     * @return 当前网络的类型是否是Wifi。false：当前没有网络连接或者网络类型不是wifi
     */
    fun isWifi(context: Context): Boolean {
        return getCurrentNetworkType(context) == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 判断当前网络的具体类型是否是UNKNOWN
     *
     * @param context 上下文
     * @return false：当前网络的具体类型是否是UNKNOWN。false：当前没有网络连接或者具体类型不是UNKNOWN
     */
    fun isUNKNOWN(context: Context): Boolean {
        return getCurrentNetworkSubtype(context) == TelephonyManager.NETWORK_TYPE_UNKNOWN
    }

    /**
     * 获取Wifi的状态，需要ACCESS_WIFI_STATE权限
     *
     * @param context 上下文
     * @return 取值为WifiManager中的WIFI_STATE_ENABLED、WIFI_STATE_ENABLING、WIFI_STATE_DISABLED、WIFI_STATE_DISABLING、WIFI_STATE_UNKNOWN之一
     * @throws Exception 没有找到wifi设备
     */
    @Throws(Exception::class)
    fun getWifiState(context: Context): Int {
        val wifiManager = context.applicationContext.getSystemService(
                Context.WIFI_SERVICE) as WifiManager
        return wifiManager.wifiState
    }


    /**
     * 判断Wifi是否打开，需要ACCESS_WIFI_STATE权限
     *
     * @param context 上下文
     * @return true：打开；false：关闭
     */
    @Throws(Exception::class)
    fun isWifiOpen(context: Context): Boolean {
        val wifiState = getWifiState(context)
        return wifiState == WifiManager.WIFI_STATE_ENABLED || wifiState == WifiManager.WIFI_STATE_ENABLING
    }


    /**
     * 判断移动网络是否打开，需要ACCESS_NETWORK_STATE权限
     *
     * @param context 上下文
     * @return true：打开；false：关闭
     */
    fun isMobileOpen(context: Context): Boolean {
        return (context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager).getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnected
    }
}