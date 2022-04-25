package apijson.model;

import apijson.framework.BaseModel;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 日志
 *
 * @author DWER
 */
public class Log extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 所属客户
     */
    private Integer customerId;
    /**
     * 操作用户
     */
    private Integer operateBy;
    /**
     * 操作类型
     */
    private Integer operateType;
    /**
     * 备注
     */
    private String remark;

    public Log() {
    }

    public Log(Integer customerId, Integer operateBy, Integer operateType, String remark) {
        this.customerId = customerId;
        this.operateBy = operateBy;
        this.operateType = operateType;
        this.remark = remark;
    }

    @JSONField(name = "customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @JSONField(name = "operate_by")
    public Integer getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(Integer operateBy) {
        this.operateBy = operateBy;
    }

    @JSONField(name = "operate_type")
    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
