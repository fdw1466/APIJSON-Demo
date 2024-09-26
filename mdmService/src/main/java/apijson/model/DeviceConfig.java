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
 * 设备配置信息
 *
 * @author DWER
 */
public class DeviceConfig extends BaseModel {
    private static final long serialVersionUID = 1L;

    private Integer customerId;
    private Integer deviceId;
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

    public DeviceConfig() {
    }

    @JSONField(name = "customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @JSONField(name = "device_id")
    public Integer getDeviceId() {
        return deviceId;
    }

    public DeviceConfig setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
        return this;
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

    @JSONField(name = "bt_name")
    public String getBtName() {
        return btName;
    }

    public void setBtName(String btName) {
        this.btName = btName;
    }

    @JSONField(name = "bt_state")
    public Integer getBtState() {
        return btState;
    }

    public void setBtState(Integer btState) {
        this.btState = btState;
    }

    @JSONField(name = "net_state")
    public Integer getNetState() {
        return netState;
    }

    public void setNetState(Integer netState) {
        this.netState = netState;
    }

    @JSONField(name = "net_type")
    public Integer getNetType() {
        return netType;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    @JSONField(name = "bt_mac")
    public String getBtMac() {
        return btMac;
    }

    public void setBtMac(String btMac) {
        this.btMac = btMac;
    }

    @JSONField(name = "wifi_state")
    public Integer getWifiState() {
        return wifiState;
    }

    public void setWifiState(Integer wifiState) {
        this.wifiState = wifiState;
    }

    @JSONField(name = "wifi_mac")
    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    @JSONField(name = "wifi_rssi")
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

    @JSONField(name = "app_list")
    public String getAppList() {
        return appList;
    }

    public void setAppList(String appList) {
        this.appList = appList;
    }
}
