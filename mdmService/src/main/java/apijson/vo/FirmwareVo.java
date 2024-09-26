package apijson.vo;

import apijson.model.Firmware;

public class FirmwareVo {
    private Integer otaId;
    private Integer isForce;
    private Firmware firmware;

    public FirmwareVo() {
    }

    public Integer getOtaId() {
        return otaId;
    }

    public void setOtaId(Integer otaId) {
        this.otaId = otaId;
    }

    public Integer getIsForce() {
        return isForce;
    }

    public void setIsForce(Integer isForce) {
        this.isForce = isForce;
    }

    public Firmware getFirmware() {
        return firmware;
    }

    public void setFirmware(Firmware firmware) {
        this.firmware = firmware;
    }

    @Override
    public String toString() {
        return "FirmwareVo{" +
                "otaId=" + otaId +
                ", isForce=" + isForce +
                ", firmware=" + firmware +
                '}';
    }
}
