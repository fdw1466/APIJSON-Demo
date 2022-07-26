package apijson.controller;

import apijson.JSONResponse;
import apijson.StringUtil;
import apijson.common.constant.CommonConstant;
import apijson.common.utils.LogUtil;
import apijson.creator.MyParser;
import apijson.creator.MyVerifier;
import apijson.framework.APIJSONConstant;
import apijson.model.Privacy;
import apijson.model.User;
import apijson.orm.JSONRequest;
import apijson.orm.exception.ConditionErrorException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static apijson.RequestMethod.*;
import static apijson.framework.APIJSONConstant.*;

/**
 * 系统Controller
 *
 * @author DWER
 */
@RestController
@RequestMapping("sys")
public class SysController extends BaseController {

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
        if (username == null || password == null) {
            return MyParser.newErrorResult(new ConditionErrorException("帐号或密码不能为空"));
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

        //校验使用状态
        if (user.getEnable() != CommonConstant.USE_STATE_ENABLE) {
            return MyParser.newErrorResult(new ConditionErrorException("账号未启用"));
        }

        //登录状态保存至session
        super.login(session, user, 1, null, null);
        //用户登录信息
        session.setAttribute(APIJSONConstant.VISITOR_, user.setId(user.getCustomerId().longValue()));
        //用户id
        session.setAttribute(USER_ID, user.getId());
        //用户基本信息
        session.setAttribute(USER_, user);
        //用户隐私信息
        session.setAttribute(PRIVACY_, privacy);

        //设置session过期时间
        session.setMaxInactiveInterval(60 * 60 * 24);

        //保存日志
        LogUtil.saveLog(user, CommonConstant.OPERATE_TYPE_LOGIN, "Sign in");

        return resp;
    }


    /**
     * 登出
     *
     * @param session
     * @return
     */
    @Override
    @PostMapping("logout")
    public JSONObject logout(HttpSession session) {
        long userId;
        try {
            //必须在session.invalidate()前！
            userId = MyVerifier.getVisitorId(session);
            User user = (User) session.getAttribute(USER_);
            super.logout(session);

            //保存日志
            LogUtil.saveLog(user, CommonConstant.OPERATE_TYPE_LOGIN, "Sign out");
        } catch (Exception e) {
            return MyParser.newErrorResult(e);
        }

        //封装返回值
        JSONObject result = MyParser.newSuccessResult();
        JSONObject user = MyParser.newSuccessResult();
        user.put(ID, userId);
        user.put(COUNT, 1);
        result.put(StringUtil.firstCase(USER_), user);

        return result;
    }
}
