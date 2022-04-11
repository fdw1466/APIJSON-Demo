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
import apijson.orm.exception.ConditionErrorException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.LRUMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static apijson.RequestMethod.*;
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
     *                "username": "admin", //帐号
     *                "password": "123456", //密码（MD5加密）
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

        //获取帐号
        JSONResponse resp = new JSONResponse(
                new MyParser(GETS, false).parseResponse(new JSONRequest(new Privacy().setUsername(username)))
        ).getJSONResponse(PRIVACY_);
        //帐号不存在
        if (resp == null) {
            return MyParser.newErrorResult(new ConditionErrorException("帐号或密码错误"));
        }
        Privacy privacy = resp.toJavaObject(Privacy.class);
        if (privacy == null) {
            return MyParser.newErrorResult(new ConditionErrorException("帐号或密码错误"));
        }

        //校验密码
        resp = new JSONResponse(
                new MyParser(HEADS, false).parseResponse(
                        new JSONRequest(new Privacy(privacy.getId()).setPassword(password))
                )
        ).getJSONResponse(PRIVACY_);
        if (!JSONResponse.isExist(resp)) {
            return MyParser.newErrorResult(new ConditionErrorException("账号或密码错误"));
        }

        //查询用户信息
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
        session.setAttribute(USER_ID, user.getCustomerId());
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
