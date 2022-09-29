package com.achengovo.myfiles.mapper;

import com.achengovo.myfiles.entity.UserFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface FileMapper {
    /**
     * 通过dir查询文件夹列表
     * @param userFile
     * @return
     */
    List<UserFile> getFileByDir(UserFile userFile);
    /**
     * 通过fileId查询文件信息
     * @param userFile
     * @return
     */
    UserFile getFileByUserFileId(UserFile userFile);
    /**
     * 删除文件更新userFiles表
     * @param userFile
     * @return
     */
    Integer delFileById(UserFile userFile);
    /**
     * 重命名
     * @param userFile
     * @return
     */
    Integer reName(UserFile userFile);
    /**
     * 移动文件
     * @param userFile
     * @return
     */
    Integer moveFile(UserFile userFile);
    /**
     * 上传文件向userFiles表插入信息
     * @param userFile
     * @return
     */
    Integer insertToUserFiles(UserFile userFile);
    /**
     * 新建文件夹向userFiles表插入信息
     * @param userFile
     * @return
     */
    Integer newDir(UserFile userFile);
}
