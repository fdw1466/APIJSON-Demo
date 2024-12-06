package apijson.mqtt;

import apijson.dto.DeviceDto;

public class HbMessage extends DeviceDto {
    private String auth;
    private String timestamp;

    public HbMessage() {
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HbMessage{" +
                "auth='" + auth + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}' + ", " + super.toString();
    }
}
