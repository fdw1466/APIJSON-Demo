package apijson.controller;

import apijson.JSONResponse;
import apijson.StringUtil;
import apijson.creator.MyParser;
import apijson.orm.JSONRequest;
import apijson.orm.exception.NotExistException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import static apijson.RequestMethod.DELETE;
import static apijson.RequestMethod.GET;

/**
 * API Controller
 *
 * @author DWER
 */
@RestController
@RequestMapping("api")
public class ApiController {

    /**
     * 估计IMEI或手机号获取白名单
     *
     * @param request {
     *                imei,
     *                phone
     *                }
     * @return
     */
    @PostMapping("get/{tag}")
    public JSONObject get(@RequestBody String request, @PathVariable String tag) {
        //校验参数
        JSONObject requestObject = null;
        String imei, phone;
        try {
            requestObject = MyParser.parseRequest(request);
            imei = requestObject.getString("imei");
            phone = requestObject.getString("phone");
        } catch (Exception e) {
            return MyParser.extendErrorResult(requestObject, e);
        }

        //根据IMEI获取终端ID
        Integer terminalId;
        if (StringUtil.isNotEmpty(imei)) {
            JSONObject jo = new JSONObject();
            jo.put("imei", imei);
            JSONObject jo1 = new JSONRequest();
            jo1.put("Terminal", jo);
            JSONResponse resp = new JSONResponse(new MyParser(GET, false).parseResponse(jo1)).getJSONResponse("Terminal");
            if (resp == null) {
                return MyParser.newErrorResult(new NotExistException("终端不存在"));
            }
            terminalId = resp.getInteger(apijson.JSONObject.KEY_ID);
        }
        //根据手机号获取终端ID
        else if (StringUtil.isNotEmpty(phone)) {
            JSONObject jo = new JSONObject();
            jo.put("phone", phone);
            JSONObject jo1 = new JSONRequest();
            jo1.put("Terminal", jo);
            JSONResponse resp = new JSONResponse(new MyParser(GET, false).parseResponse(jo1)).getJSONResponse("Terminal");
            if (resp == null) {
                return MyParser.newErrorResult(new NotExistException("终端不存在"));
            }
            terminalId = resp.getInteger(apijson.JSONObject.KEY_ID);
        } else {
            return MyParser.newErrorResult(new IllegalArgumentException("参数错误"));
        }

        JSONResponse response;
        JSONObject jo = new JSONObject();
        JSONObject jo1 = new JSONRequest();
        jo1.put("terminal_id", terminalId);
        switch (tag) {
            //根据终端ID获取白名单
            case "Whitelist":
                JSONObject jo2 = new JSONRequest();
                jo2.put("Whitelist", jo1);
                jo.put("Whitelist[]", jo2);
                response = new JSONResponse(new MyParser(GET, false).parseResponse(jo));
                break;
            //根据终端ID获取授权码
            case "AuthCode":
                jo.put("AuthCode", jo1);
                response = new JSONResponse(new MyParser(GET, false).parseResponse(jo));
                break;
            //根据终端ID获取解绑码
            case "UnbindCode":
                jo.put("UnbindCode", jo1);
                response = new JSONResponse(new MyParser(GET, false).parseResponse(jo));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tag);
        }

        return response;
    }
}
