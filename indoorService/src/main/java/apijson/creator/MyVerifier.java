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

package apijson.creator;

import apijson.JSONObject;
import apijson.common.constant.CommonConstant;
import apijson.framework.APIJSONVerifier;
import apijson.model.User;
import apijson.orm.SQLConfig;


/**
 * 权限验证器
 *
 * @author DWER
 */
public class MyVerifier extends APIJSONVerifier {
    public static final String TAG = "MyVerifier";

    /**
     * 自定义字段名等
     *
     * @param config
     * @return
     */
    @Override
    public String getVisitorIdKey(SQLConfig config) {
        return JSONObject.KEY_USER_ID;
    }

    /**
     * 自定义校验管理员
     */
    @Override
    public void verifyAdmin() {
        Integer roleId = ((User) this.visitor).getRoleId();
        if (roleId == null || roleId != CommonConstant.ROLE_ADMIN) {
            throw new IllegalArgumentException("请勿伪造[" + ADMIN + "]角色");
        }
    }
}
