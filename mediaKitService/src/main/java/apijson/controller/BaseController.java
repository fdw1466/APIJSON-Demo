package apijson.controller;

import apijson.JSONResponse;
import apijson.RequestMethod;
import apijson.common.constant.CommonConstant;
import apijson.common.utils.LogUtil;
import apijson.framework.APIJSONConstant;
import apijson.framework.APIJSONController;
import apijson.model.User;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;

import static apijson.RequestMethod.*;
import static apijson.framework.APIJSONConstant.USER_;

public class BaseController extends APIJSONController {
    /**
     * 重写解析方法，根据解析结果记录操作日志
     *
     * @param request
     * @param session
     * @param method
     * @return
     */
    @Override
    public String parse(String request, HttpSession session, RequestMethod method) {
        String parse = this.newParser(session, method).parse(request);

        //操作人是否已经登录
        User user = (User) session.getAttribute(USER_);
        if (user == null || user.getRoleId() == 0) {
            return parse;
        }

        //是否增删改
        if (POST.name().equals(method.name()) || DELETE.name().equals(method.name()) || PUT.name().equals(method.name())) {
            JSONResponse jsonResponse = new JSONResponse(parse);
            if (jsonResponse.isSuccess()) {
                JSONObject jsonRequest = JSONObject.parseObject(request);
                if (POST.name().equals(method.name())) {
                    String tag = jsonRequest.getString(APIJSONConstant.TAG);
                    if (jsonRequest.getJSONObject(tag) != null) {
                        jsonRequest.getJSONObject(tag).put("id", jsonResponse.getJSONResponse(tag).getId());
                    }
                }

                //保存日志
                Integer operateType = POST.name().equals(method.name()) ? CommonConstant.OPERATE_TYPE_ADD
                        : DELETE.name().equals(method.name()) ? CommonConstant.OPERATE_TYPE_DELETE
                        : CommonConstant.OPERATE_TYPE_UPDATE;
                LogUtil.saveLog(user, operateType, jsonRequest.toString());
            }
        }

        return parse;
    }
}
