package apijson.dto;

public class SignOutDto {
    private String imei;

    public SignOutDto() {
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "SignOutDto{" +
                "imei='" + imei + '\'' +
                '}';
    }
}
