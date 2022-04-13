package apijson.common.utils;

import apijson.JSONResponse;
import apijson.creator.MyParser;
import apijson.model.Log;
import apijson.model.User;
import apijson.orm.JSONRequest;

import static apijson.RequestMethod.POST;

/**
 * 日志工具类
 *
 * @author DWER
 */
public class LogUtil {

    /**
     * 保存日志
     *
     * @param user
     */
    public static void saveLog(User user, Integer operateType, String remark) {
        Log log = new Log(user.getCustomerId(), user.getCustomerId(), operateType, remark);
        new JSONResponse(new MyParser(POST, false).parseResponse(new JSONRequest(log)));
    }
}
