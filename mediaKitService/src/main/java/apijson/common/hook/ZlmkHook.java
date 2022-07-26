package apijson.common.hook;

import apijson.JSONRequest;
import apijson.JSONResponse;
import apijson.Log;
import apijson.common.utils.DateUtil;
import apijson.common.utils.PropertyUtil;
import apijson.creator.MyParser;
import apijson.orm.exception.ConditionErrorException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static apijson.RequestMethod.GETS;
import static apijson.RequestMethod.POST;

/**
 * ZLMK Hook
 *
 * @author DWER
 */
@RestController
@RequestMapping("zlmk")
public class ZlmkHook {
    private final String TAG = ZlmkHook.class.getSimpleName();

    /**
     * 录制完成回调
     *
     * @param request
     * @return
     */
    @PostMapping("onRecord")
    public JSONObject onRecord(@RequestBody String request) {
        //校验参数
        JSONObject requestObject = null;
        String stream, startTime, fileSize, url;
        try {
            requestObject = MyParser.parseRequest(request);
            stream = requestObject.getString("stream");
            startTime = requestObject.getString("start_time");
            fileSize = requestObject.getString("file_size");
            url = requestObject.getString("url");
        } catch (Exception e) {
            return MyParser.extendErrorResult(requestObject, e);
        }
        if (stream == null || startTime == null || fileSize == null || url == null) {
            return MyParser.newErrorResult(new ConditionErrorException("参数错误"));
        }

        //根据SN查询设备
        JSONObject device = new JSONObject();
        device.put("sn", stream);
        JSONResponse resp = new JSONResponse(new MyParser(GETS, false).parseResponse(new JSONRequest("Device", device)));
        JSONResponse deviceResp = resp.getJSONResponse("Device");
        if (deviceResp == null) {
            return MyParser.newErrorResult(new ConditionErrorException("设备不存在"));
        }

        //保存设备录制记录
        JSONObject record = new JSONObject();
        record.put("device_id", deviceResp.getId());
        String time = DateUtil.date2Str(new Date(Integer.parseInt(startTime) * 1000L), DateUtil.y_M_d_H_m_s);
        record.put("start_time", time);
        record.put("file_size", fileSize);
        String fileUrl = PropertyUtil.getProperty("zlmk_url") + "/" + url;
        record.put("file_url", fileUrl);
        resp = new JSONResponse(new MyParser(POST, false).parseResponse(new JSONRequest("DeviceRecord", record)));
        if (resp.isSuccess()) {
            Log.i(TAG, "[" + stream + "]录制完成，录制时间：" + time + "，文件地址：" + fileUrl);
            return MyParser.newSuccessResult();
        } else {
            return MyParser.newErrorResult(new RuntimeException("保存设备录制记录失败"));
        }
    }
}
