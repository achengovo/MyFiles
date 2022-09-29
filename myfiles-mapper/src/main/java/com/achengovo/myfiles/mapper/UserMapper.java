package com.achengovo.myfiles.mapper;

import com.achengovo.myfiles.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper {
    /**
     * 用户登录
     * @param user
     * @return
     */
    User login(User user);
    /**
     * 用户注册
     * @param user
     * @return
     */
    Integer register(User user);
    /**
     * 查询用户名是否存在
     * @param user
     * @return
     */
    Integer hasUser(User user);
    /**
     * 新建文件夹
     * @param fileId
     * @param fileLocation
     * @return
     */
    Integer newDir(String fileId, String fileLocation);

    /**
     * 修改头像
     * @param user
     * @return
     */
    Integer uploadAvatar(User user);
    /**
     * 获取用户信息
     * @param user
     * @return
     */
    User getUserInfo(User user);
    /**
     * 修改用户信息
     * @param user
     * @return
     */
    Integer changeUser(User user);
    /**
     * 根据ID修改密码
     * @param user
     * @return
     */
    Integer changePassById(User user);
    /**
     * 修改密码
     * @param userId
     * @param userPass
     * @param newPass
     * @return
     */
    Integer changePassByPass(@Param("userId") Integer userId, @Param("userPass") String userPass, @Param("newPass") String newPass);
    /**
     * 根据用户名修改密码
     * @param user
     * @return
     */
    Integer updatePassByUserName(User user);
}
