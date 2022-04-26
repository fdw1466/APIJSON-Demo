package apijson.common.utils.request;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequestUtil {

    /**
     * 发送请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param data          请求参数
     * @return json格式数据
     * @throws Exception
     */
    public static String sendRequest(String requestUrl, String requestMethod, Map<String, Object> data) throws Exception {
        return sendRequest(requestUrl, requestMethod, null, data);
    }

    /**
     * 发送请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param requestHeader 请求头
     * @param data          请求参数
     * @return json格式数据
     * @throws Exception
     */
    public static String sendRequest(String requestUrl, String requestMethod, Map<String, String> requestHeader, Map<String, Object> data) throws Exception {
        if (requestUrl.contains("https")) {
            return httpsRequest(requestUrl, requestMethod, requestHeader, data);
        } else {
            return httpRequest(requestUrl, requestMethod, requestHeader, data);
        }
    }

    /**
     * HTTP请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param requestHeader 请求头
     * @param data          请求参数
     * @return json格式数据
     * @throws Exception
     */
    public static String httpRequest(String requestUrl, String requestMethod, Map<String, String> requestHeader, Map<String, Object> data) throws Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("Content-type", "application/json");
        conn.setDoOutput(true);
        //设置请求头
        if (requestHeader != null) {
            requestHeader.keySet().forEach(key -> conn.setRequestProperty(key, requestHeader.get(key)));
        }
        //往服务器端写内容
        if (data != null) {
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(JSONObject.toJSONString(data));
            out.flush();
            out.close();
        }
        //读取服务器端返回的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder resp = new StringBuilder();
        String read;
        while ((read = reader.readLine()) != null) {
            resp.append(read);
        }
        return resp.toString();
    }

    /**
     * HTTPS请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param requestHeader 请求头
     * @param data          请求参数
     * @return json格式数据
     * @throws Exception
     */
    public static String httpsRequest(String requestUrl, String requestMethod, Map<String, String> requestHeader, Map<String, Object> data) throws Exception {
        //创建SSLContext
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new MyX509TrustManager()};
        //初始化
        sslContext.init(null, tm, new java.security.SecureRandom());
        //获取SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(requestMethod);
        //设置请求头
        if (requestHeader != null) {
            requestHeader.keySet().forEach(key -> conn.setRequestProperty(key, requestHeader.get(key)));
        }
        //设置当前实例使用的SSLSocketFactory
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        //往服务器端写内容
        if (data != null) {
            OutputStream os = conn.getOutputStream();
            os.write(JSONObject.toJSONString(data).getBytes(StandardCharsets.UTF_8));
            os.close();
        }
        //读取服务器端返回的内容
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder resp = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            resp.append(line);
        }
        return resp.toString();
    }

    /**
     * HTTP请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param requestHeader 请求头
     * @param data          请求参数
     * @return json格式数据
     * @throws Exception
     */
    public static InputStream streamHttpRequest(String requestUrl, String requestMethod, Map<String, String> requestHeader, Map<String, Object> data) throws Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("Content-type", "application/json");
        conn.setDoOutput(true);
        //设置请求头
        if (requestHeader != null) {
            requestHeader.keySet().forEach(key -> conn.setRequestProperty(key, requestHeader.get(key)));
        }
        //往服务器端写内容
        if (data != null) {
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(JSONObject.toJSONString(data));
            out.flush();
            out.close();
        }
        return conn.getInputStream();
    }
}

