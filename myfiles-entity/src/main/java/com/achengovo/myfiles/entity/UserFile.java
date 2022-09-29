package com.achengovo.myfiles.entity;

import java.io.Serializable;

public class UserFile implements Serializable {
    private Integer userFileId;
    private Integer userId;
    private String userFileName;
    private String userFileSaveTime;
    private String userFileState;
    private String fileType;
    private String fileLocation;
    private String dir;
    private String userFileSize;

    @Override
    public String toString() {
        return "UserFile{" +
                "userFileId=" + userFileId +
                ", userId=" + userId +
                ", userFileName='" + userFileName + '\'' +
                ", userFileSaveTime=" + userFileSaveTime +
                ", userFileState='" + userFileState + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", dir='" + dir + '\'' +
                ", userFileSize='" + userFileSize + '\'' +
                '}';
    }

    public String getUserFileSize() {
        return userFileSize;
    }

    public void setUserFileSize(String userFileSize) {
        this.userFileSize = userFileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Integer getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(Integer userFileId) {
        this.userFileId = userFileId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFileName() {
        return userFileName;
    }

    public void setUserFileName(String userFileName) {
        this.userFileName = userFileName;
    }

    public String getUserFileSaveTime() {
        return userFileSaveTime;
    }

    public void setUserFileSaveTime(String userFileSaveTime) {
        this.userFileSaveTime = userFileSaveTime;
    }

    public String getUserFileState() {
        return userFileState;
    }

    public void setUserFileState(String userFileState) {
        this.userFileState = userFileState;
    }
}
