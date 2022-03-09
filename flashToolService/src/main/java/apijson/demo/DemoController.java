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

import apijson.RequestMethod;
import apijson.framework.APIJSONController;
import apijson.orm.Parser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


/**
 * 提供入口，转交给 APIJSON 的 Parser 来处理
 *
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class DemoController extends APIJSONController {

//    @Override
//    public Parser<Long> newParser(HttpSession session, RequestMethod method) {
//        //TODO 这里关闭校验，方便新手快速测试，实际线上项目建议开启
//        return super.newParser(session, method).setNeedVerify(false);
//    }

    @PostMapping("get")
    public String getRequest(@RequestBody String request, HttpSession session) {
        return super.get(request, session);
    }

    @PostMapping("post")
    public String postRequest(@RequestBody String request, HttpSession session) {
        return super.post(request, session);
    }

    @PostMapping("put")
    public String putRequest(@RequestBody String request, HttpSession session) {
        return super.put(request, session);
    }

    @PostMapping("delete")
    public String deleteRequest(@RequestBody String request, HttpSession session) {
        return super.delete(request, session);
    }

}