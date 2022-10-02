package com.achengovo.myfiles.serviceImpl;

import com.achengovo.myfiles.entity.User;
import com.achengovo.myfiles.entity.UserFile;
import com.achengovo.myfiles.mapper.FileMapper;
import com.achengovo.myfiles.service.FileService;
import com.achengovo.myfiles.utils.RedisClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class FileServiceImpl implements FileService {
    Logger log = org.slf4j.LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    FileMapper fileMapper;
    @Autowired
    RedisClient redisUtils;
    /**
     * 通过dir查询文件夹列表
     * @param userFile
     * @param user
     * @return
     */
    public List<UserFile> getFileByDir(UserFile userFile, User user) {
        String userToken=user.getUserToken();
        if(userToken==null){
            return null;
        }
        //保证查询的是当前用户的文件
        userFile.setUserId(((User) redisUtils.get(userToken)).getUserId());
        return fileMapper.getFileByDir(userFile);
    }
    /**
     * 通过fileId查询文件信息
     * @param userFile
     * @param user
     * @return
     */
    public UserFile getFileByUserFileId(UserFile userFile,User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return null;
        }
        //保证查询的是当前用户的文件
        userFile.setUserId(((User) redisUtils.get(userToken)).getUserId());
        return fileMapper.getFileByUserFileId(userFile);
    }

    /**
     * 通过fileId删除文件
     * @param userFile
     * @param user
     * @return
     */
    public boolean delFileById(UserFile userFile,User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return false;
        }
        //保证查询的是当前用户的文件
        userFile.setUserId(((User) redisUtils.get(userToken)).getUserId());
        if(fileMapper.delFileById(userFile)>0){
            return true;
        }
        return false;
    }

    /**
     * 重命名文件
     * @param userFile
     * @param user
     * @return
     */
    public boolean reName(UserFile userFile,User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return false;
        }
        //保证查询的是当前用户的文件
        userFile.setUserId(((User) redisUtils.get(userToken)).getUserId());
        if(fileMapper.reName(userFile)>0){
            return true;
        }
        return false;
    }
    /**
     * 移动文件
     * @param userFile
     * @param user
     * @return
     */
    public boolean moveFile(UserFile userFile,User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return false;
        }
        //保证移动的是当前用户的文件
        userFile.setUserId(((User) redisUtils.get(userToken)).getUserId());
        if(fileMapper.moveFile(userFile)>0){
            return true;
        }
        return false;
    }
    /**
     * 上传文件向userFiles表插入信息
     * @param userFile
     * @param user
     * @return
     */
    public boolean insertToUserFiles(UserFile userFile,User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return false;
        }
        //保证查询的是当前用户的文件
        userFile.setUserId(((User) redisUtils.get(userToken)).getUserId());
        if(fileMapper.insertToUserFiles(userFile)>0){
            return true;
        }
        return false;
    }
    /**
     * 新建文件夹向userFiles表插入信息
     * @param userFile
     * @param user
     * @return
     */
    public boolean newDir(UserFile userFile,User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return false;
        }
        //保证查询的是当前用户的文件
        userFile.setUserId(((User) redisUtils.get(userToken)).getUserId());
        if(fileMapper.newDir(userFile)>0){
            return true;
        }
        return false;
    }
}
