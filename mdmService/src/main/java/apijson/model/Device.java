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

package apijson.model;

import apijson.framework.BaseModel;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 设备信息
 *
 * @author DWER
 */
public class Device extends BaseModel {
    private static final long serialVersionUID = 1L;

    private Integer customerId;
    private String imei;
    private String imei2;
    private Integer modelId;
    private Integer onlineStatus;
    private String androidVer;
    private String firmwareVer;
    private String basebandVer;
    private String applicationVer;
    private String lastReqTime;
    private String createTime;
    private String remark;

    public Device() {
    }

    @JSONField(name = "customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getImei() {
        return imei;
    }

    public Device setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    @JSONField(name = "model_id")
    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    @JSONField(name = "online_status")
    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @JSONField(name = "android_ver")
    public String getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(String androidVer) {
        this.androidVer = androidVer;
    }

    @JSONField(name = "firmware_ver")
    public String getFirmwareVer() {
        return firmwareVer;
    }

    public void setFirmwareVer(String firmwareVer) {
        this.firmwareVer = firmwareVer;
    }

    @JSONField(name = "baseband_ver")
    public String getBasebandVer() {
        return basebandVer;
    }

    public void setBasebandVer(String basebandVer) {
        this.basebandVer = basebandVer;
    }

    @JSONField(name = "application_ver")
    public String getApplicationVer() {
        return applicationVer;
    }

    public void setApplicationVer(String applicationVer) {
        this.applicationVer = applicationVer;
    }

    @JSONField(name = "last_req_time")
    public String getLastReqTime() {
        return lastReqTime;
    }

    public void setLastReqTime(String lastReqTime) {
        this.lastReqTime = lastReqTime;
    }

    @JSONField(name = "create_time")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
