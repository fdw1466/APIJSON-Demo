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
import apijson.framework.APIJSONFunctionParser;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;


/**
 * 可远程调用的函数类
 *
 * @author DWER
 */
public class MyFunctionParser extends APIJSONFunctionParser {
    public static final String TAG = "MyFunctionParser";

    public MyFunctionParser() {
        this(null, null, 0, null, null);
    }

    public MyFunctionParser(RequestMethod method, String tag, int version, JSONObject request, HttpSession session) {
        super(method, tag, version, request, session);
    }
}