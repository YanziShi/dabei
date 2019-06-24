package com.qckj.dabei.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class DateUtils {

    /**
     * 获取毫秒数对应的自定义格式的时间字符串
     *
     * @param milliseconds 毫秒数
     * @param formatString 自定义格式，例如 yyyy年MM月dd日
     */
    public static String getTimeStringByMillisecondsWithFormatString(long milliseconds, String formatString) {
        Date date = new Date(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
        return format.format(date);
    }

}
