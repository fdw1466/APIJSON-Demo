package apijson.controller;

import apijson.JSONResponse;
import apijson.Log;
import apijson.common.api.zlmk.ZlmkApi;
import apijson.common.constant.CommonConstant;
import apijson.common.utils.RedisUtil;
import apijson.creator.MyParser;
import apijson.model.User;
import apijson.orm.JSONRequest;
import apijson.orm.exception.ConditionErrorException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static apijson.RequestMethod.GETS;
import static apijson.framework.APIJSONConstant.*;

/**
 * API Controller
 *
 * @author DWER
 */
@RestController
@RequestMapping("api")
public class ApiController extends BaseController {
    private final String TAG = ApiController.class.getSimpleName();

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ZlmkApi zlmkApi;

    /**
     * 登录
     *
     * @param request {
     *                "sn": "11111111111", //SN
     *                "pwd": "123456", //密码
     *                }
     * @return
     */
    @PostMapping("login")
    public JSONObject login(@RequestBody String request, HttpSession session) {
        //校验参数
        JSONObject requestObject = null;
        String sn, pwd;
        try {
            requestObject = MyParser.parseRequest(request);
            sn = requestObject.getString("sn");
            pwd = requestObject.getString("pwd");
        } catch (Exception e) {
            return MyParser.extendErrorResult(requestObject, e);
        }
        if (sn == null || pwd == null) {
            return MyParser.newErrorResult(new ConditionErrorException("SN或密码不能为空"));
        }

        //获取设备
        JSONObject device = new JSONObject();
        device.put("sn", sn);
        device.put("pwd", pwd);
        JSONResponse resp = new JSONResponse(new MyParser(GETS, false).parseResponse(new JSONRequest("Device", device)));
        JSONResponse deviceResp = resp.getJSONResponse("Device");
        if (deviceResp == null) {
            return MyParser.newErrorResult(new ConditionErrorException("SN或密码错误"));
        }

        //校验使用状态
        if (deviceResp.getInteger("available") != CommonConstant.USE_STATE_ENABLE) {
            return MyParser.newErrorResult(new ConditionErrorException("设备未启用"));
        }

        //登录状态保存至session
        User user = new User();
        user.setId(deviceResp.getId());
        //标识为设备
        user.setRoleId(0);
        user.setName(deviceResp.getString("name"));
        user.setCustomerId(deviceResp.getInteger("customer_id"));
        super.login(session, user, 1, null, null);
        //用户id
        session.setAttribute(USER_ID, user.getCustomerId());
        //用户基本信息
        session.setAttribute(USER_, user);
        //设置session过期时间
        session.setMaxInactiveInterval(60 * 60 * 24);

        //将用户信息保存到redis
        redisUtil.set(CommonConstant.PREFIX_USER + sn, deviceResp.toJSONString());

        return resp;
    }

    /**
     * 录制
     *
     * @param request {
     *                deviceId: 设备ID
     *                type: 类型（1开始，2结束，3查询状态）
     *                }
     * @return
     */
    @PostMapping("record")
    public JSONObject startRecord(@RequestBody String request) {
        //校验参数
        JSONObject jsonObject = null;
        String deviceId, type;
        try {
            jsonObject = MyParser.parseRequest(request);
            deviceId = jsonObject.getString("deviceId");
            type = jsonObject.getString("type");
        } catch (Exception e) {
            return MyParser.extendErrorResult(jsonObject, e);
        }
        if (deviceId == null || type == null) {
            return MyParser.newErrorResult(new ConditionErrorException("参数错误"));
        }

        //查询设备
        JSONObject device = new JSONObject();
        device.put(ID, deviceId);
        JSONResponse resp = new JSONResponse(new MyParser(GETS, false).parseResponse(new JSONRequest("Device", device)));
        JSONResponse deviceResp = resp.getJSONResponse("Device");
        if (deviceResp == null) {
            return MyParser.newErrorResult(new ConditionErrorException("设备不存在"));
        }

        switch (type) {
            case "1":
                //调用开始录制Api
                String result = zlmkApi.startRecord(deviceResp.getString("sn"));
                Log.i(TAG, "调用开始录制Api结果：" + result);
                if (result != null && JSONObject.parseObject(result).getInteger("code") == 0) {
                    jsonObject = MyParser.newSuccessResult();
                } else {
                    jsonObject = MyParser.newErrorResult(new RuntimeException("开始录制失败"));
                }
                break;
            case "2":
                //调用停止录制Api
                result = zlmkApi.stopRecord(deviceResp.getString("sn"));
                Log.i(TAG, "调用结束录制Api结果：" + result);
                if (result != null && JSONObject.parseObject(result).getInteger("code") == 0) {
                    jsonObject = MyParser.newSuccessResult();
                } else {
                    jsonObject = MyParser.newErrorResult(new RuntimeException("结束录制失败"));
                }
                break;
            case "3":
                //调用查询录制状态Api
                result = zlmkApi.isRecording(deviceResp.getString("sn"));
                Log.i(TAG, "调用查询录制状态Api结果：" + result);
                if (result != null && JSONObject.parseObject(result).getInteger("code") == 0) {
                    jsonObject = MyParser.newSuccessResult();
                } else {
                    jsonObject = MyParser.newErrorResult(new RuntimeException("查询录制状态失败"));
                }
                break;
            default:
                jsonObject = MyParser.newErrorResult(new RuntimeException("没有匹配的类型"));
        }

        return jsonObject;
    }
}
