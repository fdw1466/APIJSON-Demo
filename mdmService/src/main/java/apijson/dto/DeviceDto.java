package apijson.dto;

public class DeviceDto {
    /*基础信息*/
    private String imei;
    private String imei2;
    private String model;
    private String androidVer;
    private String firmwareVer;
    private String basebandVer;
    private String applicationVer;

    /*配置信息*/
    private String lng;
    private String lat;
    private Integer battery;
    private String signal;
    private String btName;
    private Integer btState;
    private String btMac;
    private Integer netState;
    private Integer netType;
    private Integer wifiState;
    private String wifiMac;
    private String wifiRssi;
    private String apn;
    private String language;
    private String cpu;
    private String ram;
    private String rom;
    private String appList;

    public DeviceDto() {
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(String androidVer) {
        this.androidVer = androidVer;
    }

    public String getFirmwareVer() {
        return firmwareVer;
    }

    public void setFirmwareVer(String firmwareVer) {
        this.firmwareVer = firmwareVer;
    }

    public String getBasebandVer() {
        return basebandVer;
    }

    public void setBasebandVer(String basebandVer) {
        this.basebandVer = basebandVer;
    }

    public String getApplicationVer() {
        return applicationVer;
    }

    public void setApplicationVer(String applicationVer) {
        this.applicationVer = applicationVer;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getBtName() {
        return btName;
    }

    public void setBtName(String btName) {
        this.btName = btName;
    }

    public Integer getBtState() {
        return btState;
    }

    public void setBtState(Integer btState) {
        this.btState = btState;
    }

    public String getBtMac() {
        return btMac;
    }

    public void setBtMac(String btMac) {
        this.btMac = btMac;
    }

    public Integer getNetState() {
        return netState;
    }

    public void setNetState(Integer netState) {
        this.netState = netState;
    }

    public Integer getNetType() {
        return netType;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public Integer getWifiState() {
        return wifiState;
    }

    public void setWifiState(Integer wifiState) {
        this.wifiState = wifiState;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public String getWifiRssi() {
        return wifiRssi;
    }

    public void setWifiRssi(String wifiRssi) {
        this.wifiRssi = wifiRssi;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getAppList() {
        return appList;
    }

    public void setAppList(String appList) {
        this.appList = appList;
    }

    @Override
    public String toString() {
        return "DeviceDto{" +
                "imei='" + imei + '\'' +
                ", imei2='" + imei2 + '\'' +
                ", model='" + model + '\'' +
                ", androidVer='" + androidVer + '\'' +
                ", firmwareVer='" + firmwareVer + '\'' +
                ", basebandVer='" + basebandVer + '\'' +
                ", applicationVer='" + applicationVer + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", battery=" + battery +
                ", signal='" + signal + '\'' +
                ", btName='" + btName + '\'' +
                ", btState=" + btState +
                ", btMac='" + btMac + '\'' +
                ", netState=" + netState +
                ", netType=" + netType +
                ", wifiState=" + wifiState +
                ", wifiMac='" + wifiMac + '\'' +
                ", wifiRssi='" + wifiRssi + '\'' +
                ", apn='" + apn + '\'' +
                ", language='" + language + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", rom='" + rom + '\'' +
                ", appList='" + appList + '\'' +
                '}';
    }
}
