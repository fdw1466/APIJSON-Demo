package apijson.service.impl;

import apijson.JSONResponse;
import apijson.StringUtil;
import apijson.common.constant.CommonConstant;
import apijson.common.utils.*;
import apijson.creator.MyParser;
import apijson.dto.DeviceDto;
import apijson.dto.OtaRecordDto;
import apijson.dto.SignInDto;
import apijson.dto.SignOutDto;
import apijson.model.*;
import apijson.orm.JSONRequest;
import apijson.orm.exception.NotExistException;
import apijson.service.ApiService;
import apijson.vo.FirmwareVo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static apijson.RequestMethod.*;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 设备登入
     *
     * @param dto
     * @return
     */
    @Override
    public JSONObject signIn(SignInDto dto) {
        log.info("设备登入：{}", dto);

        //校验参数
        if (StringUtil.isEmpty(dto.getImei())) {
            return MyParser.newErrorResult(new IllegalArgumentException("IMEI not null"));
        }
        if (StringUtil.isEmpty(dto.getModel())) {
            return MyParser.newErrorResult(new IllegalArgumentException("Model not null"));
        }
        if (StringUtil.isEmpty(dto.getTimestamp())) {
            return MyParser.newErrorResult(new IllegalArgumentException("Timestamp not null"));
        }
        if (StringUtil.isEmpty(dto.getSign())) {
            return MyParser.newErrorResult(new IllegalArgumentException("Sign not null"));
        }

        //校验签名
        String sign = Md5Util.getMd5(Md5Util.getMd5(dto.getImei()) + Md5Util.getMd5(dto.getTimestamp()));
        if (!dto.getSign().equals(sign)) {
            return MyParser.newErrorResult(new IllegalArgumentException("Sign is error"));
        }

        //根据设备机型名称查询机型
        Model model = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Model().setName(dto.getModel())))
        ).getObject(Model.class);
        //机型不存在
        if (model == null) {
            return MyParser.newErrorResult(new NotExistException("Model " + dto.getModel() + " dont exist"));
        }

        //根据IMEI查询设备
        JSONResponse resp = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Device().setImei(dto.getImei())))
        );
        Device device = resp.getObject(Device.class);
        //设备不存在，则注册
        if (device == null) {
            //保存设备
            device = new Device();
            device.setCustomerId(Integer.parseInt(PropertyUtil.getProperty("regis_def_custom_id")));
            device.setImei(dto.getImei());
            device.setModelId(model.getId().intValue());
            device.setOnlineStatus(1);
            device.setLastReqTime(DateUtil.getUtcStr());
            device.setCreateTime(DateUtil.getUtcStr());
            resp = new JSONResponse(new MyParser(POST, false).parseResponse(new JSONRequest(device)));
            if (!resp.isSuccess()) {
                return MyParser.newErrorResult(new RuntimeException("Save device failed"));
            }
        } else {
            //更新设备在线状态
            device.setOnlineStatus(1);
            device.setLastReqTime(DateUtil.getUtcStr());
            resp = new JSONResponse(new MyParser(PUT, false).parseResponse(new JSONRequest(device)));
            if (!resp.isSuccess()) {
                return MyParser.newErrorResult(new RuntimeException("Update device failed"));
            }
        }

        //生成Token
        String token = JwtUtil.sign(dto.getImei(), sign);
        redisUtil.set(CommonConstant.REDIS_TOKEN_PRE + dto.getImei(), token, JwtUtil.EXPIRE_TIME / 1000);

        return MyParser.newResult(200, token);
    }

    /**
     * 设备登出
     *
     * @param dto
     * @return
     */
    @Override
    public JSONObject signOut(SignOutDto dto) {
        log.info("设备登出：{}", dto);

        //设置Token过期
        if (redisUtil.get(CommonConstant.REDIS_TOKEN_PRE + dto.getImei()) != null) {
            redisUtil.expire(CommonConstant.REDIS_TOKEN_PRE + dto.getImei(), 1);
        }

        return MyParser.newSuccessResult();
    }

    /**
     * 设备心跳
     *
     * @param dto
     * @return
     */
    @Override
    public JSONObject heartbeat(DeviceDto dto) {
        log.info("设备心跳：{}", dto);

        //校验参数
        if (StringUtil.isEmpty(dto.getImei())) {
            return MyParser.newErrorResult(new IllegalArgumentException("IMEI not null"));
        }
        if (StringUtil.isEmpty(dto.getModel())) {
            return MyParser.newErrorResult(new IllegalArgumentException("Model not null"));
        }

        //根据设备机型名称查询机型
        Model model = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Model().setName(dto.getModel())))
        ).getObject(Model.class);
        //机型不存在
        if (model == null) {
            return MyParser.newErrorResult(new NotExistException("Model " + dto.getModel() + " dont exist"));
        }

        //根据IMEI查询设备
        JSONResponse resp = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Device().setImei(dto.getImei())))
        );
        Device device = resp.getObject(Device.class);
        //设备不存在
        if (device == null) {
            return MyParser.newErrorResult(new NotExistException("Device " + dto.getImei() + " dont exist"));
        } else {
            //更新设备信息
            MyBeanUtils.copyPropertiesIgnoreNull(dto, device);
            device.setModelId(model.getId().intValue());
            device.setOnlineStatus(1);
            device.setLastReqTime(DateUtil.getUtcStr());
            resp = new JSONResponse(new MyParser(PUT, false).parseResponse(new JSONRequest(device)));
            if (!resp.isSuccess()) {
                return MyParser.newErrorResult(new RuntimeException("Update device failed"));
            }
        }

        //查询设备配置信息
        DeviceConfig deviceConfig = new JSONResponse(
                new MyParser(GET, false).parseResponse(
                        new JSONRequest(new DeviceConfig().setDeviceId(device.getId().intValue()))
                )
        ).getObject(DeviceConfig.class);
        if (deviceConfig == null) {
            //保存设备配置信息
            deviceConfig = new DeviceConfig();
            MyBeanUtils.copyPropertiesIgnoreNull(dto, deviceConfig);
            deviceConfig.setCustomerId(Integer.parseInt(PropertyUtil.getProperty("regis_def_custom_id")));
            deviceConfig.setDeviceId(device.getId().intValue());
            resp = new JSONResponse(new MyParser(POST, false).parseResponse(new JSONRequest(deviceConfig)));
            if (!resp.isSuccess()) {
                return MyParser.newErrorResult(new RuntimeException("Save device config failed"));
            }
        } else {
            //更新设备配置信息
            MyBeanUtils.copyPropertiesIgnoreNull(dto, deviceConfig);
            resp = new JSONResponse(new MyParser(PUT, false).parseResponse(new JSONRequest(deviceConfig)));
            if (!resp.isSuccess()) {
                return MyParser.newErrorResult(new RuntimeException("Update device config failed"));
            }
        }

        return resp;
    }

    /**
     * 检查更新
     *
     * @param imei
     * @param version
     * @return
     */
    @Override
    public JSONObject checkUpdate(String imei, String version) {
        log.info("检查更新：{imei: {}, version: {}}", imei, version);

        //校验参数
        if (StringUtil.isEmpty(imei)) {
            return MyParser.newErrorResult(new IllegalArgumentException("IMEI not null"));
        }

        //查询设备信息
        Device device = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Device().setImei(imei)))
        ).getObject(Device.class);
        if (device == null) {
            return MyParser.newErrorResult(new NotExistException("Device " + imei + " dont exist"));
        }

        //查询是否发布升级固件
        List<JSONResponse> otaListRes = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest("[]", new JSONRequest(new Ota())))
        ).getList(JSONResponse.class);
        if (CollectionUtils.isEmpty(otaListRes)) {
            return MyParser.newErrorResult(new NotExistException("No firmware upgrade released"));
        }

        //获取发布的升级固件列表
        List<FirmwareVo> firmwareVos = new ArrayList<>();
        otaListRes.forEach(otaItem -> {
            Ota ota = otaItem.getObject(Ota.class);

            //是否指定升级
            if (ota.getIsSpecify() == 1) {
                //指定群组
                List<JSONResponse> otaGroupsRes = new JSONResponse(
                        new MyParser(GET, false).parseResponse(
                                new JSONRequest(
                                        "[]", new JSONRequest(new OtaGroup().setOtaId(ota.getId().intValue()))
                                )
                        )
                ).getList(JSONResponse.class);
                if (!CollectionUtils.isEmpty(otaGroupsRes)) {
                    otaGroupsRes.forEach(otaGroupItem -> {
                        OtaGroup otaGroup = otaGroupItem.getObject(OtaGroup.class);

                        //查询群组是否包含该设备
                        GroupDevice groupDevice = new GroupDevice();
                        groupDevice.setGroupId(otaGroup.getGroupId());
                        groupDevice.setDeviceId(device.getId().intValue());
                        groupDevice = new JSONResponse(
                                new MyParser(GET, false).parseResponse(new JSONRequest(groupDevice))
                        ).getObject(GroupDevice.class);
                        if (groupDevice != null) {
                            getFirmwareVo(firmwareVos, ota, device.getModelId());
                        }
                    });
                }

                //指定设备
                OtaDevice otaDevice = new OtaDevice();
                otaDevice.setOtaId(ota.getId().intValue());
                otaDevice.setDeviceId(device.getId().intValue());
                otaDevice = new JSONResponse(
                        new MyParser(GET, false).parseResponse(new JSONRequest(otaDevice))
                ).getObject(OtaDevice.class);
                if (otaDevice != null) {
                    getFirmwareVo(firmwareVos, ota, device.getModelId());
                }
            } else {
                getFirmwareVo(firmwareVos, ota, device.getModelId());
            }
        });
        if (CollectionUtils.isEmpty(firmwareVos)) {
            return MyParser.newErrorResult(new NotExistException("No matching upgrade firmware"));
        }

        //根据版本代码比较出最新的固件
        List<FirmwareVo> sortVos = firmwareVos.stream().filter(
                p -> p.getFirmware().getType() == 0 || version.equals(p.getFirmware().getBasicVer())
        ).sorted(
                Comparator.comparing((FirmwareVo vo) -> vo.getFirmware().getVersionCode()).reversed()
        ).collect(Collectors.toList());
        if (sortVos.isEmpty()) {
            return MyParser.newErrorResult(new NotExistException("No matching upgrade firmware"));
        }

        JSONObject result = MyParser.newSuccessResult();
        result.put(Firmware.class.getSimpleName(), sortVos.get(0));

        return result;
    }

    /**
     * 获取固件信息
     *
     * @param firmwareVos
     * @param ota
     * @param modelId
     */
    private void getFirmwareVo(List<FirmwareVo> firmwareVos, Ota ota, Integer modelId) {
        //根据ID查询固件信息
        Firmware firmware = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(
                        new Firmware().setId(Long.valueOf(ota.getFirmwareId())))
                )
        ).getObject(Firmware.class);
        if (firmware != null) {
            //判断固件机型是否匹配
            FirmwareModel firmwareModel = new JSONResponse(
                    new MyParser(GET, false).parseResponse(
                            new JSONRequest(new FirmwareModel(firmware.getId().intValue(), modelId))
                    )
            ).getObject(FirmwareModel.class);
            if (firmwareModel != null) {
                FirmwareVo vo = new FirmwareVo();
                vo.setOtaId(ota.getId().intValue());
                vo.setIsForce(ota.getIsForce());
                vo.setFirmware(firmware);
                firmwareVos.add(vo);
            }
        }
    }

    /**
     * 保存升级记录
     *
     * @param dto
     * @return
     */
    @Override
    public JSONObject saveOtaRecord(OtaRecordDto dto) {
        log.info("保存升级记录：{}", dto);

        //校验参数
        if (StringUtil.isEmpty(dto.getImei())) {
            return MyParser.newErrorResult(new IllegalArgumentException("IMEI not null"));
        }
        if (StringUtil.isEmpty(dto.getOtaId())) {
            return MyParser.newErrorResult(new IllegalArgumentException("Ota ID not null"));
        }

        //查询设备信息
        Device device = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Device().setImei(dto.getImei())))
        ).getObject(Device.class);
        if (device == null) {
            return MyParser.newErrorResult(new NotExistException("Device " + dto.getImei() + " dont exist"));
        }

        //查询升级记录是否存在
        OtaRecord otaRecord = new OtaRecord();
        otaRecord.setOtaId(dto.getOtaId());
        otaRecord.setDeviceId(device.getId().intValue());
        JSONResponse jsonResponse = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(otaRecord))
        );

        JSONResponse resp;
        //存在则更新，不存在则新增
        if (jsonResponse.getObject(OtaRecord.class) != null) {
            //更新升级记录
            otaRecord = jsonResponse.getObject(OtaRecord.class);
            setOtaRecordTime(dto, otaRecord);
            resp = new JSONResponse(new MyParser(PUT, false).parseResponse(new JSONRequest(otaRecord)));
            if (!resp.isSuccess()) {
                return MyParser.newErrorResult(new RuntimeException("Update ota record failed"));
            }
        } else {
            //新增升级记录
            setOtaRecordTime(dto, otaRecord);
            otaRecord.setFirmwareId(dto.getFirmwareId());
            otaRecord.setCustomerId(device.getCustomerId());
            otaRecord.setCreateTime(DateUtil.getUtcStr());
            resp = new JSONResponse(new MyParser(POST, false).parseResponse(new JSONRequest(otaRecord)));
            if (!resp.isSuccess()) {
                return MyParser.newErrorResult(new RuntimeException("Add ota record failed"));
            }
        }

        return resp;
    }

    /**
     * 设置升级记录的时间
     *
     * @param dto
     * @param otaRecord
     */
    private void setOtaRecordTime(OtaRecordDto dto, OtaRecord otaRecord) {
        if ("1".equals(dto.getDownload())) {
            otaRecord.setDownloadTime(DateUtil.getUtcStr());
            return;
        }
        if ("1".equals(dto.getDownloaded())) {
            otaRecord.setDownloadedTime(DateUtil.getUtcStr());
            return;
        }
        if ("1".equals(dto.getUpgrade())) {
            otaRecord.setUpgradeTime(DateUtil.getUtcStr());
            if (!StringUtil.isEmpty(dto.getOldVersion())) {
                otaRecord.setOldVersion(dto.getOldVersion());
            }
            return;
        }
        if ("1".equals(dto.getUpgraded())) {
            otaRecord.setUpgradedTime(DateUtil.getUtcStr());
            if (!StringUtil.isEmpty(dto.getNewVersion())) {
                otaRecord.setNewVersion(dto.getNewVersion());
            }
        }
    }

    /**
     * 处理身份认证过期
     *
     * @param imei
     */
    @Override
    public void handleSubjectExpire(String imei) {
        //查询设备信息
        Device device = new JSONResponse(
                new MyParser(GET, false).parseResponse(new JSONRequest(new Device().setImei(imei)))
        ).getObject(Device.class);
        if (device != null) {
            //更新设备在线状态
            device.setOnlineStatus(0);
            device.setLastReqTime(DateUtil.getUtcStr());
            new JSONResponse(new MyParser(PUT, false).parseResponse(new JSONRequest(device)));
        }
    }
}
