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

package apijson.demo;

import apijson.Log;
import apijson.framework.APIJSONSQLExecutor;
import apijson.orm.SQLConfig;

import javax.sql.DataSource;
import java.sql.Connection;


/**
 * SQL 执行
 *
 * @author Lemon
 */
public class DemoSQLExecutor extends APIJSONSQLExecutor {
    public static final String TAG = "DemoSQLExecutor";

    /**
     * 适配连接池，如果这里能拿到连接池的有效 Connection，则 SQLConfig 不需要配置 dbVersion, dbUri, dbAccount, dbPassword
     *
     * @param config
     * @return
     * @throws Exception
     */
    @Override
    public Connection getConnection(SQLConfig config) throws Exception {
        String key = config.getDatasource() + "-" + config.getDatabase();
        Connection c = connectionMap.get(key);
        if (c == null || c.isClosed()) {
            try {
                DataSource ds = DemoApplication.getApplicationContext().getBean(DataSource.class);
                connectionMap.put(key, ds.getConnection());
            } catch (Exception e) {
                Log.e(TAG, "getConnection   try { "
                        + "DataSource ds = DemoApplication.getApplicationContext().getBean(DataSource.class); .."
                        + "} catch (Exception e) = " + e.getMessage());
            }
        }

        // 必须最后执行 super 方法，因为里面还有事务相关处理。
        // 如果这里是 return c，则会导致 增删改 多个对象时只有第一个会 commit，即只有第一个对象成功插入数据库表
        return super.getConnection(config);
    }

}
