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
 * 指定升级群组
 *
 * @author DWER
 */
public class OtaGroup extends BaseModel {
    private static final long serialVersionUID = 1L;

    private Integer customerId;
    private Integer otaId;
    private Integer groupId;

    public OtaGroup() {
    }

    @JSONField(name = "customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @JSONField(name = "ota_id")
    public Integer getOtaId() {
        return otaId;
    }

    public OtaGroup setOtaId(Integer otaId) {
        this.otaId = otaId;
        return this;
    }

    @JSONField(name = "group_id")
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
