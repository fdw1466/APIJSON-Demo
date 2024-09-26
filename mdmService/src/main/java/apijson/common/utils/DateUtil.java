package apijson.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String y_M_d_H_m_s_S = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String y_M_d_H_m_s = "yyyy-MM-dd HH:mm:ss";
    public static String y_M_d = "yyyy-MM-dd";
    public static String H_m_s = "HH:mm:ss";

    /**
     * 时间转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String date2Str(Date date, String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(date);
    }

    /**
     * 时间转字符串
     *
     * @param date
     * @return
     */
    public static String date2Str(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(y_M_d_H_m_s);
        return sf.format(date);
    }

    /**
     * 字符串转时间
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String str, String pattern) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.parse(str);
    }

    /**
     * 字符串转时间
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String str) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(y_M_d_H_m_s);
        return sf.parse(str);
    }

    /**
     * 获取UTC时间
     *
     * @param localDate
     * @return
     */
    public static Date getUtc(Date localDate) {
        /** long时间转换成Calendar */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localDate == null ? System.currentTimeMillis() : localDate.getTime());
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取UTC时间
     *
     * @return
     */
    public static Date getUtc() {
        return getUtc(null);
    }

    /**
     * 获取UTC时间字符串
     *
     * @return
     */
    public static String getUtcStr() {
        return date2Str(getUtc());
    }
}
