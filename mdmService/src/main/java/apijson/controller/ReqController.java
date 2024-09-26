/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.controller;

import apijson.JSONResponse;
import apijson.RequestMethod;
import apijson.common.constant.CommonConstant;
import apijson.common.utils.LogUtil;
import apijson.framework.APIJSONConstant;
import apijson.framework.APIJSONController;
import apijson.model.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static apijson.RequestMethod.*;
import static apijson.framework.APIJSONConstant.USER_;

/**
 * 提供入口，转交给 APIJSON 的 Parser 来处理
 *
 * @author DWER
 */
@RestController
@RequestMapping("")
public class ReqController extends APIJSONController {

    /**
     * 获取
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("get")
    public String get(@RequestBody String request, HttpSession session) {
        return super.get(request, session);
    }

    /**
     * 计数
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("head")
    public String head(@RequestBody String request, HttpSession session) {
        return super.head(request, session);
    }

    /**
     * 限制性获取
     * request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("gets")
    public String gets(@RequestBody String request, HttpSession session) {
        return super.gets(request, session);
    }

    /**
     * 限制性计数
     * request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("heads")
    public String heads(@RequestBody String request, HttpSession session) {
        return super.heads(request, session);
    }

    /**
     * 新增
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("post")
    public String post(@RequestBody String request, HttpSession session) {
        return super.post(request, session);
    }

    /**
     * 修改
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("put")
    public String put(@RequestBody String request, HttpSession session) {
        return super.put(request, session);
    }

    /**
     * 删除
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("delete")
    public String delete(@RequestBody String request, HttpSession session) {
        return super.delete(request, session);
    }

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
        if (user == null) {
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
                LogUtil.saveLog(session, operateType, jsonRequest.toString());
            }
        }

        return parse;
    }
}