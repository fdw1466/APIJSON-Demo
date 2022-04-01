package apijson.controller;

import apijson.JSONResponse;
import apijson.Log;
import apijson.StringUtil;
import apijson.creator.MyParser;
import apijson.creator.MyVerifier;
import apijson.framework.APIJSONController;
import apijson.model.Privacy;
import apijson.model.User;
import apijson.orm.JSONRequest;
import apijson.utils.PropertyUtil;
import apijson.utils.request.HttpRequestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.LRUMap;
import com.sun.deploy.association.RegisterFailedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static apijson.RequestMethod.GET;
import static apijson.RequestMethod.POST;
import static apijson.framework.APIJSONConstant.*;

/**
 * 系统Controller
 *
 * @author DWER
 */
@RestController
@RequestMapping("sys")
public class SysController extends APIJSONController {
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

    /**
     * 登录
     *
     * @param request {
     *                "username": "admin", //网管帐号
     *                "password": "123456", //网管密码
     *                }
     * @return
     */
    @PostMapping("login")
    public JSONObject login(@RequestBody String request, HttpSession session) {
        //校验参数
        JSONObject requestObject = null;
        String username, password;
        try {
            requestObject = MyParser.parseRequest(request);
            username = requestObject.getString("username");
            password = requestObject.getString("password");
        } catch (Exception e) {
            return MyParser.extendErrorResult(requestObject, e);
        }

        //通过调度接口校验帐号密码
        JSONObject jsonObject;
        try {
            String param = "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
            String url = PropertyUtil.getProperty("ds_url") + "/nms/login?" + param;
            jsonObject = JSON.parseObject(HttpRequestUtil.sendRequest(url, RequestMethod.GET.name(), null));
        } catch (Exception e) {
            return MyParser.newErrorResult(e);
        }
        if (!"0".equals(jsonObject.getString("code"))) {
            return MyParser.newResult(jsonObject.getIntValue("code"), jsonObject.getString("desc"));
        }

        //获取帐号
        JSONResponse resp = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Privacy().setUsername(username)))
        ).getJSONResponse(PRIVACY_);
        //帐号未注册，需自动注册
        if (resp == null) {
            //添加用户
            JSONResponse addResp = new JSONResponse(
                    new MyParser(POST, false).parseResponse(new JSONRequest(new User().setName(username)))
            ).getJSONResponse(USER_);
            if (!addResp.isSuccess()) {
                return MyParser.newErrorResult(new RegisterFailedException());
            }
            //添加帐号
            Privacy privacy = new Privacy(username, password);
            JSONRequest jsonRequest = new JSONRequest(privacy);
            jsonRequest.getJSONObject(PRIVACY_).put(apijson.JSONObject.KEY_USER_ID, addResp.getId());
            addResp = new JSONResponse(new MyParser(POST, false).parseResponse(jsonRequest));
            if (!addResp.isSuccess()) {
                return MyParser.newErrorResult(new RegisterFailedException());
            }
            jsonRequest.getJSONObject(PRIVACY_).put(ID, addResp.getJSONResponse(PRIVACY_).getId());
            resp = new JSONResponse(jsonRequest.getJSONObject(PRIVACY_));
        }

        //查询用户信息
        Privacy privacy = resp.toJavaObject(Privacy.class);
        resp = new JSONResponse(new MyParser(GET, false).parseResponse(new JSONRequest(new User(privacy.getUserId()))));
        User user = resp.getObject(User.class);
        if (user == null) {
            return MyParser.newErrorResult(new NullPointerException("服务器内部错误"));
        }

        //登录状态保存至session
        super.login(session, user, 1, null, null);
        SESSION_MAP.put(session.getId(), session);
        Log.d(TAG, "login userId = " + user.getId() + "; session.getId() = " + session.getId());
        //用户id
        session.setAttribute(USER_ID, user.getId());
        //用户基本信息
        session.setAttribute(USER_, user);
        //用户隐私信息
        session.setAttribute(PRIVACY_, privacy);
        //设置session过期时间
        session.setMaxInactiveInterval(60 * 60 * 24);

        return resp;
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
        long userId;
        try {
            //必须在session.invalidate()前！
            userId = MyVerifier.getVisitorId(session);
            super.logout(session);
            SESSION_MAP.remove(session.getId());
            Log.d(TAG, "logout userId = " + userId + "; session.getId() = " + session.getId());
        } catch (Exception e) {
            return MyParser.newErrorResult(e);
        }

        JSONObject result = MyParser.newSuccessResult();
        JSONObject user = MyParser.newSuccessResult();
        user.put(ID, userId);
        user.put(COUNT, 1);
        result.put(StringUtil.firstCase(USER_), user);

        return result;
    }
}
