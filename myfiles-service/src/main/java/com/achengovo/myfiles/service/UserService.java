package com.achengovo.myfiles.service;

import com.achengovo.myfiles.entity.User;
import org.springframework.stereotype.Component;
@Component
public interface UserService {
    /**
     * 用户登录
     * @param user
     * @return
     */
    String login(User user);
    /**
     * 退出登录
     * @param userToken
     * @return
     */
    boolean loginOut(String userToken);
    /**
     * 用户注册
     * @param user
     * @return
     */
    String register(User user);
    /**
     * 获取验证码
     * @param email 邮箱
     * @param type 类型
     * @return
     * @throws Exception
     */
    boolean getVerifyCode(String email,String type) throws Exception;
    /**
     * 查询用户是否存在
     * @param user
     * @return
     */
    boolean hasUser(User user);
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
    boolean uploadAvatar(User user);
    /**
     * 从Redis（Session）获取用户信息
     * @param user
     * @return
     */
    User getUserInfoInRedis(User user);
    /**
     * 从数据库获取用户信息
     * @param user
     * @return
     */
    User getUserInfoInDB(User user);
    /**
     * 修改用户信息
     * @param user
     * @return
     */
    String changeUser(User user);
    /**
     * 修改密码
     * @param user
     * @param newPass
     * @return
     */
    boolean changePassByPass(User user, String newPass);
    //根据Id修改密码
    boolean changePassById(User user);
    //根据用户名修改密码
    Integer updatePassByUserName(User user);
}
