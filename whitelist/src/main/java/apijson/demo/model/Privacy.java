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

package apijson.demo.model;

import apijson.MethodAccess;
import apijson.framework.BaseModel;
import com.alibaba.fastjson.annotation.JSONField;

import static apijson.orm.AbstractVerifier.*;

/**
 * 用户隐私信息
 *
 * @author DWER
 */
@MethodAccess(
        GET = {},
        GETS = {OWNER, ADMIN},
        POST = {UNKNOWN, ADMIN},
        DELETE = {ADMIN}
)
public class Privacy extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;
    /**
     * 登录密码，隐藏字段
     */
    private String password;

    public Privacy() {
        super();
    }

    public Privacy(long id) {
        this();
        setId(id);
    }

    public Privacy(String username, String password) {
        this();
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public Privacy setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * fastjson >= 1.2.70 的版本时，JSON.toJSONString 后，
     * get__password, get_password 会分别转为 __password, password，
     * 不像之前(例如 1.2.61 及以下)分别转为       _password, password，
     * 如果 @JSONField(name="_password") 未生效，请勿使用 1.2.70-1.2.73，或调整数据库字段命名为 __password
     *
     * @return
     */
    @JSONField(name = "_password")
    public String get__password() {
        return password;
    }

    public Privacy setPassword(String password) {
        this.password = password;
        return this;
    }

}
