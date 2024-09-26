package apijson.dto;

public class PublishDto {
    private int qos;
    private boolean retained;
    private String topic;
    private String message;

    public PublishDto() {
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PublishDto{" +
                "qos=" + qos +
                ", retained=" + retained +
                ", topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
