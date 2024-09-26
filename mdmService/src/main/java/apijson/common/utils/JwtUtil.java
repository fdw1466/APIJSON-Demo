package apijson.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @desc JWT工具类
 **/
public class JwtUtil {
    public static final long EXPIRE_TIME = Long.parseLong(PropertyUtil.getProperty("token_expire"));

    /**
     * 生成token
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的用户名
     *
     * @return
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 根据request获取token
     *
     * @param request
     * @return
     * @throws RuntimeException
     */
    public static String getToken(HttpServletRequest request) throws RuntimeException {
        String accessToken = request.getHeader("X-Access-Token");
        if (accessToken == null || "".equals(accessToken)) {
            throw new RuntimeException("Token不存在");
        }
        return accessToken;
    }

    /**
     * 根据request中的token获取用户账号
     *
     * @param request
     * @return
     * @throws RuntimeException
     */
    public static String getUserNameByToken(HttpServletRequest request) throws RuntimeException {
        String username = getUsername(getToken(request));
        if (username == null || "".equals(username)) {
            throw new RuntimeException("未获取到用户");
        }
        return username;
    }
}
