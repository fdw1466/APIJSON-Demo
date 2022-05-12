package apijson.common.constant;

public interface CommonConstant {

    ////////////////////////操作类型////////////////////////
    /**
     * 添加
     */
    int OPERATE_TYPE_ADD = 1;
    /**
     * 修改
     */
    int OPERATE_TYPE_UPDATE = 2;
    /**
     * 删除
     */
    int OPERATE_TYPE_DELETE = 3;
    /**
     * 登录
     */
    int OPERATE_TYPE_LOGIN = 4;
    /**
     * 登出
     */
    int OPERATE_TYPE_LOGOUT = 5;

    ////////////////////////角色////////////////////////
    /**
     * 管理员
     */
    int ROLE_ADMIN = 1;
    /**
     * 普通客户
     */
    int ROLE_CUSTOMER = 2;

    ////////////////////////Redis前缀////////////////////////
    /**
     * 用户信息前缀
     */
    String PREFIX_USER = "PREFIX_USER_";
}
