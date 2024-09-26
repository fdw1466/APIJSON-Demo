package apijson.common.utils;

import apijson.StringUtil;
import apijson.common.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SubjectUtil {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 校验Token
     *
     * @param req
     * @param imei
     */
    public void checkToken(HttpServletRequest req, String imei) throws IllegalAccessException {
        String token = req.getHeader("AssetToken");
        Object o = redisUtil.get(CommonConstant.REDIS_TOKEN_PRE + imei);
        if (StringUtil.isEmpty(token) || !token.equals(o)) {
            throw new IllegalAccessException("Token is error");
        }

        //重置Token有效期
        redisUtil.expire(CommonConstant.REDIS_TOKEN_PRE + imei, JwtUtil.EXPIRE_TIME / 1000);
    }
}
