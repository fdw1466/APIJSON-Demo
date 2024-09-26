package apijson.dto;

public class OtaRecordDto {
    private String imei;
    private Integer otaId;
    private Integer firmwareId;
    private String download;
    private String downloaded;
    private String upgrade;
    private String upgraded;
    private String oldVersion;
    private String newVersion;

    public OtaRecordDto() {
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getOtaId() {
        return otaId;
    }

    public void setOtaId(Integer otaId) {
        this.otaId = otaId;
    }

    public Integer getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(Integer firmwareId) {
        this.firmwareId = firmwareId;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    public String getUpgraded() {
        return upgraded;
    }

    public void setUpgraded(String upgraded) {
        this.upgraded = upgraded;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    @Override
    public String toString() {
        return "OtaRecordDto{" +
                "imei='" + imei + '\'' +
                ", otaId=" + otaId +
                ", firmwareId=" + firmwareId +
                ", download='" + download + '\'' +
                ", downloaded='" + downloaded + '\'' +
                ", upgrade='" + upgrade + '\'' +
                ", upgraded='" + upgraded + '\'' +
                ", oldVersion='" + oldVersion + '\'' +
                ", newVersion='" + newVersion + '\'' +
                '}';
    }
}
