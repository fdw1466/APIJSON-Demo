package apijson.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5工具类
 *
 * @author DWER
 */
public class Md5Unit {

    /**
     * string转md5
     *
     * @param str
     * @return
     */
    public static String getMd5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            return bytesToHexStr(md.digest());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * byte转hex
     *
     * @param bs
     * @return
     */
    public static String bytesToHexStr(byte[] bs) {
        try {
            StringBuilder buf = new StringBuilder();
            for (byte b : bs) {
                buf.append(String.format("%02X", b));
            }
            return buf.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(getMd5("123456"));
    }
}
