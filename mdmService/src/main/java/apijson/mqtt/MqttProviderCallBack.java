package apijson.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class MqttProviderCallBack implements MqttCallback {

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
        log.info(String.format("接收消息主题: %s", topic));
        log.info(String.format("接收消息Qos: %d", message.getQos()));
        log.info(String.format("接收消息Retained: %b", message.isRetained()));
        log.info(String.format("接收消息内容: %s", new String(message.getPayload())));
    }

}