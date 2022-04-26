package apijson.common.api.zlmk;

import apijson.common.utils.PropertyUtil;
import apijson.common.utils.request.HttpRequestUtil;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static org.springframework.http.HttpMethod.GET;

@Service
public class ZlmkApiImpl implements ZlmkApi {

    /**
     * 获取流列表
     *
     * @return
     */
    @Override
    public String getMediaList() {
        String url = PropertyUtil.getProperty("zlmk_url") + "/index/api/getMediaList";
        String param = "?secret=" + PropertyUtil.getProperty("zlmk_secret");
        try {
            return HttpRequestUtil.sendRequest(url + param, GET.name(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取截图
     *
     * @return
     */
    @Override
    public InputStream getSnap(String snapUrl, int timeoutSec, int expireSec) {
        String url = PropertyUtil.getProperty("zlmk_url") + "/index/api/getSnap";
        String param = "?secret=" + PropertyUtil.getProperty("zlmk_secret")
                + "&url=" + snapUrl
                + "&timeout_sec=" + timeoutSec
                + "&expire_sec=" + expireSec;
        try {
            return HttpRequestUtil.streamHttpRequest(url + param, GET.name(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
