package apijson.controller;

import apijson.JSONResponse;
import apijson.StringUtil;
import apijson.common.constant.CommonConstant;
import apijson.common.utils.LogUtil;
import apijson.config.MqttProviderConfig;
import apijson.creator.MyParser;
import apijson.creator.MyVerifier;
import apijson.dto.PublishDto;
import apijson.framework.APIJSONConstant;
import apijson.framework.APIJSONController;
import apijson.model.Privacy;
import apijson.model.User;
import apijson.orm.JSONRequest;
import apijson.orm.exception.NotExistException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static apijson.RequestMethod.*;
import static apijson.framework.APIJSONConstant.*;

/**
 * 系统 Controller
 *
 * @author DWER
 */
@Slf4j
@RestController
@RequestMapping("sys")
public class SysController extends APIJSONController {

    @Autowired
    private MqttProviderConfig providerClient;

    /**
     * 登录系统
     *
     * @param request {
     *                "username": "", //帐号
     *                "password": "", //密码（MD5加密）
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
            return MyParser.newErrorResult(new IllegalArgumentException("帐号或密码不能为空"));
        }

        //获取帐号
        JSONResponse resp = new JSONResponse(
                new MyParser(GETS, false).parseResponse(new JSONRequest(new Privacy().setUsername(username)))
        ).getJSONResponse(PRIVACY_);
        //帐号不存在
        if (resp == null) {
            return MyParser.newErrorResult(new IllegalArgumentException("帐号或密码错误"));
        }
        Privacy privacy = resp.toJavaObject(Privacy.class);
        if (privacy == null) {
            return MyParser.newErrorResult(new IllegalArgumentException("帐号或密码错误"));
        }

        //校验密码
        resp = new JSONResponse(
                new MyParser(HEADS, false).parseResponse(
                        new JSONRequest(new Privacy(privacy.getId()).setPassword(password))
                )
        ).getJSONResponse(PRIVACY_);
        if (!JSONResponse.isExist(resp)) {
            return MyParser.newErrorResult(new IllegalArgumentException("账号或密码错误"));
        }

        //查询用户信息
        resp = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new User(privacy.getUserId())))
        );
        User user = resp.getObject(User.class);
        if (user == null) {
            return MyParser.newErrorResult(new NotExistException("用户不存在"));
        }

        //校验使用状态
        if (user.getEnable() != CommonConstant.USE_STATE_ENABLE) {
            return MyParser.newErrorResult(new UnsupportedOperationException("用户已禁用"));
        }

        //登录状态保存至session
        super.login(session, user, 1, null, null);
        //设置用户id
        session.setAttribute(ID, user.getId().intValue());
        //设置用户所属客户id
        session.setAttribute(USER_ID, user.getCustomerId());
        //设置用户登录信息
        session.setAttribute(APIJSONConstant.VISITOR_, user.setId(user.getCustomerId().longValue()));
        //设置用户基本信息
        session.setAttribute(USER_, user);
        //设置用户隐私信息
        session.setAttribute(PRIVACY_, privacy);
        //设置session过期时间
        session.setMaxInactiveInterval(60 * 60 * 24);

        //保存日志
        LogUtil.saveLog(session, CommonConstant.OPERATE_TYPE_LOGIN, "Sign in");

        return resp;
    }

    /**
     * 登出系统
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

            //保存日志
            LogUtil.saveLog(session, CommonConstant.OPERATE_TYPE_LOGIN, "Sign out");
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

    /**
     * 发布消息
     *
     * @param dto
     * @return
     */
    @PostMapping("/publish")
    public JSONObject publish(PublishDto dto, HttpSession session) {
        //校验参数
        if (StringUtil.isEmpty(dto.getTopic()) || StringUtil.isEmpty(dto.getMessage())) {
            return MyParser.newErrorResult(new IllegalArgumentException("参数错误"));
        }

        //校验登录
        try {
            MyVerifier.verifyLogin(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //发布消息
        try {
            providerClient.publish(dto.getQos(), dto.isRetained(), dto.getTopic(), dto.getMessage());
            log.info("发布消息成功：{}", dto);
            return MyParser.newSuccessResult();
        } catch (Exception e) {
            log.error("发布消息失败：{}", dto);
            return MyParser.newErrorResult(e);
        }
    }
}
