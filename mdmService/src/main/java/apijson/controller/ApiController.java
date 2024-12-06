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

import apijson.common.utils.SubjectUtil;
import apijson.creator.MyParser;
import apijson.dto.DeviceDto;
import apijson.dto.OtaRecordDto;
import apijson.dto.SignInDto;
import apijson.dto.SignOutDto;
import apijson.framework.APIJSONController;
import apijson.service.ApiService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * API Controller
 *
 * @author DWER
 */
@RestController
@RequestMapping("api")
public class ApiController extends APIJSONController {

    @Autowired
    private ApiService apiService;
    @Autowired
    private SubjectUtil subjectUtil;

    /**
     * 设备登入
     *
     * @param dto
     * @return
     */
    @PostMapping("signIn")
    public JSONObject signIn(@RequestBody SignInDto dto) {
        return apiService.signIn(dto);
    }

    /**
     * 设备登出
     *
     * @param dto
     * @return
     */
    @PostMapping("signOut")
    public JSONObject signOut(@RequestBody SignOutDto dto) {
        return apiService.signOut(dto);
    }

    /**
     * 设备心跳
     *
     * @param dto
     * @param req
     * @return
     */
    @PostMapping("heartbeat")
    public JSONObject heartbeat(@RequestBody DeviceDto dto, HttpServletRequest req) {
        try {
            subjectUtil.checkToken(req.getHeader("AssetToken"), dto.getImei());
        } catch (IllegalAccessException e) {
            return MyParser.newErrorResult(e);
        }
        return apiService.heartbeat(dto);
    }

    /**
     * 检查更新
     *
     * @param imei
     * @param version
     * @param req
     * @return
     */
    @GetMapping("checkUpdate")
    public JSONObject checkUpdate(String imei, String version, HttpServletRequest req) {
        try {
            subjectUtil.checkToken(req.getHeader("AssetToken"), imei);
        } catch (IllegalAccessException e) {
            return MyParser.newErrorResult(e);
        }
        return apiService.checkUpdate(imei, version);
    }

    /**
     * 保存升级记录
     *
     * @param dto
     * @return
     */
    @PostMapping("saveOtaRecord")
    public JSONObject saveOtaRecord(@RequestBody OtaRecordDto dto, HttpServletRequest req) {
        try {
            subjectUtil.checkToken(req.getHeader("AssetToken"), dto.getImei());
        } catch (IllegalAccessException e) {
            return MyParser.newErrorResult(e);
        }
        return apiService.saveOtaRecord(dto);
    }
}