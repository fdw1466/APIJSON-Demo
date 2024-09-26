package apijson.common.utils;

import apijson.JSONResponse;
import apijson.creator.MyParser;
import apijson.model.Log;
import apijson.orm.JSONRequest;

import javax.servlet.http.HttpSession;

import static apijson.RequestMethod.POST;
import static apijson.framework.APIJSONConstant.ID;
import static apijson.framework.APIJSONConstant.USER_ID;

/**
 * 日志工具类
 *
 * @author DWER
 */
public class LogUtil {

    /**
     * 保存日志
     *
     * @param session
     * @param operateType
     * @param remark
     */
    public static void saveLog(HttpSession session, Integer operateType, String remark) {
        Integer customerId = (Integer) session.getAttribute(USER_ID);
        Integer userId = (Integer) session.getAttribute(ID);
        Log log = new Log(customerId, userId, operateType, remark);
        new JSONResponse(new MyParser(POST, false).parseResponse(new JSONRequest(log)));
    }
}
