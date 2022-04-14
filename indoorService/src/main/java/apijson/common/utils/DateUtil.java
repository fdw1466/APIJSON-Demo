package apijson.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}
