package com.fungo.sample.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Pinger
 * @since 18-12-10 上午10:48
 */
object DateUtils {


    /**
     * 输入时间格式：2018-11-28T00:00:00.0Z
     * 输出格式：2018-11-28
     */
    fun parseDate(time: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA)
        val date = formatter.parse(time)
        return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date)
    }

}