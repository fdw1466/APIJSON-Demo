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
 * 固件信息
 *
 * @author DWER
 */
public class Firmware extends BaseModel {
    private static final long serialVersionUID = 1L;

    private Integer customerId;
    private String md5;
    private String fileName;
    private String filePath;
    private String ftpPath;
    private String ftpUri;
    private String httpUri;
    private String fileSize;
    private Integer type;
    private String basicVer;
    private String version;
    private String versionCode;
    private String releaseNote;
    private String allowLteDownload;
    private String createTime;
    private String remark;

    public Firmware() {
    }

    @JSONField(name = "customer_id")
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @JSONField(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JSONField(name = "file_path")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @JSONField(name = "ftp_path")
    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    @JSONField(name = "ftp_uri")
    public String getFtpUri() {
        return ftpUri;
    }

    public void setFtpUri(String ftpUri) {
        this.ftpUri = ftpUri;
    }

    @JSONField(name = "http_uri")
    public String getHttpUri() {
        return httpUri;
    }

    public void setHttpUri(String httpUri) {
        this.httpUri = httpUri;
    }

    @JSONField(name = "file_size")
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JSONField(name = "basic_ver")
    public String getBasicVer() {
        return basicVer;
    }

    public void setBasicVer(String basicVer) {
        this.basicVer = basicVer;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JSONField(name = "version_code")
    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @JSONField(name = "release_note")
    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    @JSONField(name = "allow_lte_download")
    public String getAllowLteDownload() {
        return allowLteDownload;
    }

    public void setAllowLteDownload(String allowLteDownload) {
        this.allowLteDownload = allowLteDownload;
    }

    @JSONField(name = "create_time")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
