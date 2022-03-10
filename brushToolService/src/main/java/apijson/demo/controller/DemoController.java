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

package apijson.demo.controller;

import apijson.JSONResponse;
import apijson.Log;
import apijson.StringUtil;
import apijson.demo.creator.DemoParser;
import apijson.demo.creator.DemoVerifier;
import apijson.demo.model.Privacy;
import apijson.demo.model.User;
import apijson.framework.APIJSONController;
import apijson.framework.BaseModel;
import apijson.orm.JSONRequest;
import apijson.orm.exception.ConditionErrorException;
import apijson.orm.exception.NotExistException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.LRUMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static apijson.RequestMethod.GETS;
import static apijson.RequestMethod.HEADS;
import static apijson.framework.APIJSONConstant.*;


/**
 * 提供入口，转交给 APIJSON 的 Parser 来处理
 *
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class DemoController extends APIJSONController {
    public static final SessionMap SESSION_MAP;
    public static final List<String> EXCEPT_HEADER_LIST;

    static {
        SESSION_MAP = new SessionMap();
        //accept-encoding 在某些情况下导致乱码，origin 和 sec-fetch-mode 等 CORS 信息导致服务器代理失败
        EXCEPT_HEADER_LIST = Arrays.asList(
                "accept-encoding",
                "accept-language",
                "host",
                "origin",
                "referer",
                "user-agent",
                "sec-fetch-mode",
                "sec-fetch-site",
                "sec-fetch-dest",
                "sec-fetch-user"
        );
    }

    public static class SessionMap extends LRUMap<String, HttpSession> {
        public SessionMap() {
            super(16, 1000000);
        }

        public void remove(String key) {
            _map.remove(key);
        }
    }

//    @Override
//    public Parser<Long> newParser(HttpSession session, RequestMethod method) {
//        //TODO 这里关闭校验，方便新手快速测试，实际线上项目建议开启
//        return super.newParser(session, method).setNeedVerify(false);
//    }

    /**
     * 登录
     *
     * @param request {
     *                "username": "admin",
     *                "password": "123456",
     *                }
     * @return
     */
    @PostMapping("login")
    public JSONObject login(@RequestBody String request, HttpSession session) {
        JSONObject requestObject = null;
        String username, password;
        try {
            requestObject = DemoParser.parseRequest(request);
            username = requestObject.getString("username");
            password = requestObject.getString("password");
        } catch (Exception e) {
            return DemoParser.extendErrorResult(requestObject, e);
        }

        //帐号是否注册
        JSONObject phoneResponse = new DemoParser(HEADS, false).parseResponse(
                new JSONRequest(new Privacy().setUsername(username))
        );
        if (!JSONResponse.isSuccess(phoneResponse)) {
            return DemoParser.newResult(
                    phoneResponse.getIntValue(JSONResponse.KEY_CODE), phoneResponse.getString(JSONResponse.KEY_MSG)
            );
        }
        JSONResponse response = new JSONResponse(phoneResponse).getJSONResponse(PRIVACY_);
        if (!JSONResponse.isExist(response)) {
            return DemoParser.newErrorResult(new NotExistException("帐号未注册"));
        }

        //根据帐号获取用户
        JSONObject privacyResponse = new DemoParser(GETS, false).parseResponse(
                new JSONRequest(new Privacy().setUsername(username)).setFormat(true)
        );
        response = new JSONResponse(privacyResponse);

        Privacy privacy = response.getObject(Privacy.class);
        long userId = privacy == null ? 0 : BaseModel.value(privacy.getId());
        if (userId <= 0) {
            return privacyResponse;
        }

        //校验密码
        response = new JSONResponse(
                new DemoParser(HEADS, false).parseResponse(new JSONRequest(new Privacy(userId).setPassword(password)))
        );
        if (!JSONResponse.isSuccess(response)) {
            return response;
        }
        response = response.getJSONResponse(PRIVACY_);
        if (!JSONResponse.isExist(response)) {
            return DemoParser.newErrorResult(new ConditionErrorException("帐号或密码错误"));
        }

        response = new JSONResponse(
                new DemoParser(GETS, false).parseResponse(
                        // 兼容 MySQL 5.6 及以下等不支持 json 类型的数据库
                        new JSONRequest(
                                // User 里在 setContactIdList(List<Long>) 后加 setContactIdList(String) 没用
                                USER_,
                                // fastjson 查到一个就不继续了，所以只能加到前面或者只有这一个，但这样反过来不兼容 5.7+
                                new apijson.JSONObject(
                                        // 所以就用 @json 来强制转为 JSONArray，保证有效
                                        new User(userId)
                                ).setJson("contactIdList,pictureList")
                        ).setFormat(true)
                )
        );
        User user = response.getObject(User.class);
        if (user == null || BaseModel.value(user.getId()) != userId) {
            return DemoParser.newErrorResult(new NullPointerException("服务器内部错误"));
        }

        //登录状态保存至session
        super.login(session, user, 1, null, null);
        //用户id
        session.setAttribute(USER_ID, userId);
        //用户基本信息
        session.setAttribute(USER_, user);
        //用户隐私信息
        session.setAttribute(PRIVACY_, privacy);
        //设置session过期时间
        session.setMaxInactiveInterval(60 * 60 * 24);

        return response;
    }

    /**
     * 登出
     *
     * @param session
     * @return
     */
    @PostMapping("logout")
    @Override
    public JSONObject logout(HttpSession session) {
        SESSION_MAP.remove(session.getId());

        long userId;
        try {
            //必须在session.invalidate()前！
            userId = DemoVerifier.getVisitorId(session);
            Log.d(TAG, "logout userId = " + userId + "; session.getId() = " + session.getId());
            super.logout(session);
        } catch (Exception e) {
            return DemoParser.newErrorResult(e);
        }

        JSONObject result = DemoParser.newSuccessResult();
        JSONObject user = DemoParser.newSuccessResult();
        user.put(ID, userId);
        user.put(COUNT, 1);
        result.put(StringUtil.firstCase(USER_), user);

        return result;
    }

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

}