package apijson.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class UploadUtil {

    /**
     * 上传文件
     *
     * @param url
     * @param fileName
     * @param inputStream
     */
    public static String upload(String url, String fileName, InputStream inputStream) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
        String boundary = "--------------4585696313564699";
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        //HttpEntity builder
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //字符编码
        builder.setCharset(StandardCharsets.UTF_8);
        //模拟浏览器
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //boundary
        builder.setBoundary(boundary);
        //binary
        builder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, fileName);
        //HttpEntity
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        //执行提交
        HttpResponse response = httpClient.execute(httpPost);
        //响应
        HttpEntity responseEntity = response.getEntity();
        String result = "";
        if (responseEntity != null) {
            //将响应内容转换为字符串
            result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        }
        return result;
    }
}
