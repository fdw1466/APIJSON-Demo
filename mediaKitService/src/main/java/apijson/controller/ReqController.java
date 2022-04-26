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

package apijson.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 提供入口，转交给 APIJSON 的 Parser 来处理
 *
 * @author DWER
 */
@RestController
@RequestMapping("")
public class ReqController extends BaseController {

    /**
     * 获取
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("get")
    public String get(@RequestBody String request, HttpSession session) {
        return super.get(request, session);
    }

    /**
     * 计数
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("head")
    public String head(@RequestBody String request, HttpSession session) {
        return super.head(request, session);
    }

    /**
     * 限制性获取
     * request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("gets")
    public String gets(@RequestBody String request, HttpSession session) {
        return super.gets(request, session);
    }

    /**
     * 限制性计数
     * request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("heads")
    public String heads(@RequestBody String request, HttpSession session) {
        return super.heads(request, session);
    }

    /**
     * 新增
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("post")
    public String post(@RequestBody String request, HttpSession session) {
        return super.post(request, session);
    }

    /**
     * 修改
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("put")
    public String put(@RequestBody String request, HttpSession session) {
        return super.put(request, session);
    }

    /**
     * 删除
     *
     * @param request
     * @param session
     * @return
     */
    @Override
    @PostMapping("delete")
    public String delete(@RequestBody String request, HttpSession session) {
        return super.delete(request, session);
    }
}