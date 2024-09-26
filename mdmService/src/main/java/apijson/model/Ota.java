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
 * 升级信息
 *
 * @author DWER
 */
public class Ota extends BaseModel {
    private static final long serialVersionUID = 1L;

    private Integer customerId;
    private Integer firmwareId;
    private Integer isSpecify;
    private Integer isForce;
    private String createTime;
    private String remark;

    public Ota() {
    }

    @JSONField(name = "customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public Ota setCustomerId(Integer customerId) {
        this.customerId = customerId;
        return this;
    }

    @JSONField(name = "firmware_id")
    public Integer getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(Integer firmwareId) {
        this.firmwareId = firmwareId;
    }

    @JSONField(name = "is_specify")
    public Integer getIsSpecify() {
        return isSpecify;
    }

    public void setIsSpecify(Integer isSpecify) {
        this.isSpecify = isSpecify;
    }

    @JSONField(name = "is_force")
    public Integer getIsForce() {
        return isForce;
    }

    public void setIsForce(Integer isForce) {
        this.isForce = isForce;
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
