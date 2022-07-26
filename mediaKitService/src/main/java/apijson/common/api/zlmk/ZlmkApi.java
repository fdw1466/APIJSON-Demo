package apijson.common.api.zlmk;

import java.io.InputStream;

public interface ZlmkApi {
    String getMediaList();

    InputStream getSnap(String snapUrl, int timeoutSec, int expireSec);

    String startRecord(String stream);

    String stopRecord(String stream);

    String isRecording(String stream);
}
