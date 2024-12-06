package apijson.mqtt;

import apijson.JSONObject;
import apijson.common.utils.Md5Util;
import apijson.common.utils.SpringContextUtils;
import apijson.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class MqttProviderCallBack implements MqttCallback {

    private static final ApiService apiService = SpringContextUtils.getBean(ApiService.class);

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("与服务器断开连接，可重连...");
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("接收消息：主题【{}】，内容：{}", topic, new String(message.getPayload()));

        if ("MDM-HEARTBEAT".equals(topic)) {
            HbMessage hbMessage = JSONObject.parseObject(new String(message.getPayload()), HbMessage.class);

            //校验Auth
            String md5 = Md5Util.getMd5(hbMessage.getImei() + "=" + hbMessage.getTimestamp());
            if (hbMessage.getAuth() != null && !hbMessage.getAuth().equals(md5)) {
                log.error("Auth校验失败");
                return;
            }

            //处理设备心跳
            apiService.heartbeat(hbMessage);
        }
    }

}