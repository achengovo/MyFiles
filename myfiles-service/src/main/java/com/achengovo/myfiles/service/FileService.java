package com.achengovo.myfiles.service;

import com.achengovo.myfiles.entity.User;
import com.achengovo.myfiles.entity.UserFile;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface FileService {
    /**
     * 通过dir查询文件夹列表
     * @param userFile
     * @param user
     * @return
     */
    List<UserFile> getFileByDir(UserFile userFile, User user);
    /**
     * 通过fileId查询文件信息
     * @param userFile
     * @param user
     * @return
     */
    UserFile getFileByUserFileId(UserFile userFile,User user);
    /**
     * 删除文件更新userFiles表
     * @param userFile
     * @param user
     * @return
     */
    boolean delFileById(UserFile userFile,User user);
    /**
     * 重命名
     * @param userFile
     * @param user
     * @return
     */
    boolean reName(UserFile userFile,User user);
    /**
     * 移动文件
     * @param userFile
     * @param user
     * @return
     */
    boolean moveFile(UserFile userFile,User user);
    /**
     * 上传文件向userFiles表插入信息
     * @param userFile
     * @param user
     * @return
     */
    boolean insertToUserFiles(UserFile userFile,User user);
    /**
     * 新建文件夹向userFiles表插入信息
     * @param userFile
     * @param user
     * @return
     */
    boolean newDir(UserFile userFile,User user);
}
