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

import apijson.RequestMethod;
import apijson.framework.APIJSONSQLConfig;
import apijson.orm.AbstractSQLConfig;
import apijson.orm.model.Access;
import apijson.orm.model.Function;
import apijson.orm.model.Request;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static apijson.framework.APIJSONConstant.*;


/**
 * SQL配置，这里不配置数据库账号密码等信息，改为使用 DemoDataSourceConfig 来配置
 * TiDB 用法和 MySQL 一致
 *
 * @author DWER
 */
public class MySQLConfig extends APIJSONSQLConfig {

    static {
        DEFAULT_DATABASE = DATABASE_MYSQL;
        DEFAULT_SCHEMA = "mdm";

        //表名和数据库不一致的，需要配置映射关系。只使用 APIJSONORM 时才需要；
        //这个 Demo 用了 apijson-framework 且调用了 APIJSONApplication.init
        //(间接调用 DemoVerifier.init 方法读取数据库 Access 表来替代手动输入配置)，所以不需要。
        //但如果 Access 这张表的对外表名与数据库实际表名不一致，仍然需要这里注册。
        TABLE_KEY_MAP.put(Access.class.getSimpleName(), "sys_access");
        TABLE_KEY_MAP.put(Request.class.getSimpleName(), "sys_request");
        TABLE_KEY_MAP.put(Function.class.getSimpleName(), "sys_function");

        //主键名映射
        SIMPLE_CALLBACK = new SimpleCallback() {

            @Override
            public AbstractSQLConfig getSQLConfig(RequestMethod method, String database, String schema, String table) {
                return new MySQLConfig(method, table);
            }

            //取消注释来实现自定义各个表的主键名
            /*@Override
            public String getIdKey(String database, String schema, String datasource, String table) {
                return StringUtil.firstCase(table + "Id");  // userId, comemntId ...
                //		return StringUtil.toLowerCase(t) + "_id";  // user_id, comemnt_id ...
                //		return StringUtil.toUpperCase(t) + "_ID";  // USER_ID, COMMENT_ID ...
            }*/

            @Override
            public String getUserIdKey(String database, String schema, String datasource, String table) {
                // id / userId
                return USER_.equals(table) || PRIVACY_.equals(table) ? ID : USER_ID;
            }

            //取消注释来实现数据库自增 id
            @Override
            public Object newId(RequestMethod method, String database, String schema, String table) {
                // return null 则不生成 id，一般用于数据库自增 id
                return null;
            }

            @Override
            public void onMissingKey4Combine(String name, JSONObject request, String combine, String item, String key) throws Exception {
                super.onMissingKey4Combine(name, request, combine, item, key);
            }
        };
    }

    public MySQLConfig() {
        super();
    }

    public MySQLConfig(RequestMethod method, String table) {
        super(method, table);
    }

    @Override
    public String getDBVersion() {
        return "5.7.22";
    }

}
