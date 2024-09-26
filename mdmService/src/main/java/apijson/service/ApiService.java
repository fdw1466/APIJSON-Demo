package apijson.service;

import apijson.dto.DeviceDto;
import apijson.dto.OtaRecordDto;
import apijson.dto.SignInDto;
import apijson.dto.SignOutDto;
import com.alibaba.fastjson.JSONObject;

public interface ApiService {
    JSONObject signIn(SignInDto dto);

    JSONObject signOut(SignOutDto dto);

    JSONObject heartbeat(DeviceDto dto);

    JSONObject checkUpdate(String imei, String version);

    JSONObject saveOtaRecord(OtaRecordDto dto);

    void handleSubjectExpire(String imei);
}
