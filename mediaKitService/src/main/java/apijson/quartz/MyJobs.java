package apijson.quartz;

import apijson.JSONRequest;
import apijson.JSONResponse;
import apijson.Log;
import apijson.common.api.zlmk.ZlmkApi;
import apijson.common.utils.PropertyUtil;
import apijson.common.utils.UUIDUtil;
import apijson.common.utils.UploadUtil;
import apijson.creator.MyParser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static apijson.RequestMethod.GETS;
import static apijson.RequestMethod.PUT;

@Component
@EnableAsync
@EnableScheduling
public class MyJobs {
    public static final String TAG = MyJobs.class.getSimpleName();

    @Autowired
    private ZlmkApi zlmkApi;

    /**
     * 截图任务
     * 每10分钟执行一次
     */
    @Async
    @Scheduled(cron = "0 0/10 * * * ?")
    public void execute() {
        //是否启用定时任务
        if (!"1".equals(PropertyUtil.getProperty("job_snap"))) {
            Log.logInfo(TAG, "=============== 截图任务未启用 ===============", LogLevel.INFO.name());
            return;
        }
        Log.logInfo(TAG, "=============== 截图任务开始 ===============", LogLevel.INFO.name());

        //获取流列表
        String resp = zlmkApi.getMediaList();
        if (resp == null) {
            Log.logInfo(TAG, "获取流列表异常", LogLevel.ERROR.name());
        }
        JSONObject jsonObject = JSONObject.parseObject(resp);
        if (jsonObject.getInteger("code") != 0) {
            Log.logInfo(TAG, "获取流列表异常", LogLevel.ERROR.name());
        }

        //解析流列表
        Map<String, String> streamMap = new HashMap<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        jsonArray.forEach(data -> {
            JSONObject object = (JSONObject) data;
            String stream = object.getString("stream"), originUrl = object.getString("originUrl");
            streamMap.putIfAbsent(stream, originUrl);
        });
        //遍历流列表
        for (String key : streamMap.keySet()) {
            //获取设备
            JSONObject device = new JSONObject();
            device.put("sn", key);
            JSONResponse deviceRes = new JSONResponse(new MyParser(GETS, false).parseResponse(
                    new JSONRequest("Device", device))
            ).getJSONResponse("Device");
            if (deviceRes == null) {
                Log.logInfo(TAG, "设备【" + key + "】不存在", LogLevel.INFO.name());
                continue;
            }

            //获取并截图
            String stream = streamMap.get(key);
            InputStream snap = zlmkApi.getSnap(stream, 10, 30);
            if (snap != null) {
                //上传截图到文件服务器
                JSONObject uploadRes = null;
                String url = PropertyUtil.getProperty("fs_url");
                String name = UUIDUtil.getUUID() + ".jpeg";
                try {
                    uploadRes = JSONObject.parseObject(UploadUtil.upload(url, name, snap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (uploadRes == null || uploadRes.getInteger("code") != 0) {
                    Log.logInfo(TAG, "上传文件【" + name + "】失败", LogLevel.INFO.name());
                    continue;
                }

                //更新设备截图
                device = new JSONObject();
                device.put("id", deviceRes.getId());
                device.put("snap_url", uploadRes.getString("desc"));
                deviceRes = new JSONResponse(new MyParser(PUT, false).parseResponse(
                        new JSONRequest("Device", device))
                );
                if (deviceRes.isSuccess()) {
                    Log.logInfo(TAG, "更新设备【" + key + "】截图成功", LogLevel.INFO.name());
                }
            }
        }

        Log.logInfo(TAG, "=============== 截图任务结束 ===============", LogLevel.INFO.name());
    }
}
