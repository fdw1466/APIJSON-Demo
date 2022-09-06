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
import apijson.framework.APIJSONObjectParser;
import apijson.framework.APIJSONParser;
import apijson.orm.SQLConfig;
import com.alibaba.fastjson.JSONObject;

/**
 * 请求解析器
 *
 * @author DWER
 */
public class MyParser extends APIJSONParser {

    public MyParser() {
        super();
    }

    public MyParser(RequestMethod method) {
        super(method);
    }

    public MyParser(RequestMethod method, boolean needVerify) {
        super(method, needVerify);
    }

    @Override
    public int getDefaultQueryCount() {
        return 10;
    }

    @Override
    public int getMaxQueryPage() {
        return 1000;
    }

    @Override
    public int getMaxQueryCount() {
        return 3600;
    }

    @Override
    public int getMaxUpdateCount() {
        return 200;
    }

    @Override
    public int getMaxSQLCount() {
        return 200;
    }

    @Override
    public int getMaxObjectCount() {
        return 200;
    }

    @Override
    public int getMaxArrayCount() {
        return 200;
    }

    @Override
    public int getMaxQueryDepth() {
        return 5;
    }

    /**
     * 自定义解析器
     *
     * @param request
     * @param parentPath
     * @param arrayConfig
     * @param isSubQuery
     * @param isTable
     * @param isArrayMainTable
     * @return
     * @throws Exception
     */
    @Override
    public APIJSONObjectParser createObjectParser(JSONObject request, String parentPath, SQLConfig arrayConfig
            , boolean isSubQuery, boolean isTable, boolean isArrayMainTable) throws Exception {
        return new MyObjectParser(getSession(), request, parentPath, arrayConfig, isSubQuery, isTable, isArrayMainTable)
                .setMethod(getMethod()).setParser(this);
    }

}
