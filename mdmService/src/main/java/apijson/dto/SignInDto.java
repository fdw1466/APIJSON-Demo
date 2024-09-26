package apijson.dto;

public class SignInDto {

    /**
     * IMEI
     */
    private String imei;

    /**
     * 机型
     */
    private String model;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 签名：IMEI（MD5）+时间戳（MD5）的结果再MD5
     */
    private String sign;

    public SignInDto() {
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "SignInDto{" +
                "imei='" + imei + '\'' +
                ", model='" + model + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
