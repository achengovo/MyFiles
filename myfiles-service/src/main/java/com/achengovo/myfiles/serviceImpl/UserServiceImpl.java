package com.achengovo.myfiles.serviceImpl;
import com.achengovo.myfiles.entity.User;
import com.achengovo.myfiles.mapper.UserMapper;
import com.achengovo.myfiles.service.UserService;
import com.achengovo.myfiles.utils.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import static com.achengovo.myfiles.utils.VerCode.getRandomVerCode;
@Service
public class UserServiceImpl implements UserService {
    Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
//    @Autowired
//    MybatisUtils mybatisUtils;
////    MybatisUtils mybatisUtils=new MybatisUtils();
//    UserMapper userMapper = mybatisUtils.getMapper(UserMapper.class);
    @Autowired
    UserMapper userMapper;
    /**
     * 用户登录
     * @param user
     * @return
     */
    public String login(User user){
        User resultUser = userMapper.login(user);
        if(resultUser!=null){
            String userToken = resultUser.getUserId()+"#"+java.util.UUID.randomUUID();
            RedisUtils.setObject(userToken,resultUser,60*60*24*7);
            return userToken;
        }
        return "fail";
    }

    /**
     * 退出登录
     * @param userToken
     * @return
     */
    public boolean loginOut(String userToken) {
        if(RedisUtils.del(userToken)){
            return true;
        }
        return false;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    public String register(User user){
        //检查验证码是否正确
        if(!user.getVarCode().equals(RedisUtils.get(user.getUserEmail()+"register"))){
            return "验证码错误";
        }
        //检查用户名是否存在
        if(userMapper.hasUser(user)!=0){
            return "用户名已存在";
        }
        //注册用户
        if(userMapper.register(user)==1){
            //注册成功后从redis中删除验证码
            RedisUtils.del(user.getUserEmail()+"register");
            return "注册成功";
        }
        return "注册失败";
    }

    /**
     * 获取验证码
     * @param email 邮箱
     * @param type 类型
     * @return 是否成功
     * @throws MessagingException
     */
    public boolean getVerifyCode(String email, String type) throws MessagingException {
        String content = "";
        String verCode = getRandomVerCode(5);
        switch (type){
            case "register":{
                content = "您的注册验证码为：【"+verCode+"】，请在5分钟内完成注册。";
                break;
            }
            case "changePass": {
                content = "您的修改密码验证码为：【" + verCode + "】，请在5分钟内完成修改。";
                break;
            }
            case "changeEmail": {
                content = "您的修改邮箱验证码为：【" + verCode + "】，请在5分钟内完成修改。";
                break;
            }
        }
        try{
            RedisUtils.set(email+type, verCode, 60 * 5);
            SendMail.sendNetMail(email, content, "MyFiles验证码");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 查询是否存在用户
     * @param user
     * @return
     */
    public boolean hasUser(User user){
        if(userMapper.hasUser(user)>0){
            return true;
        }
        return false;
    }
    /**
     * 新建文件夹
     * @param fileId
     * @param fileLocation
     * @return
     */
    public Integer newDir(String fileId,String fileLocation){
        return userMapper.newDir(fileId,fileLocation);
    }
    /**
     * 修改头像
     * @param user
     * @return
     */
    public boolean uploadAvatar(User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return false;
        }
        //保证修改的是自己的头像
        user.setUserId(((User) RedisUtils.getObject(userToken)).getUserId());
        if(userMapper.uploadAvatar(user)>0){
            //更新redis中的用户信息
            RedisUtils.setObject(user.getUserToken(),userMapper.getUserInfo(user),60*60*24*7);
            return true;
        }
        return false;
    }

    /**
     * 从Redis中获取用户信息
     * @param user
     * @return
     */
    public User getUserInfoInRedis(User user){
        String userToken=user.getUserToken();
        if(userToken==null){
            return null;
        }
        User resultUser= (User) RedisUtils.getObject(userToken);
        return resultUser;
    }

    /**
     * 从数据库获取用户信息
     * @param user
     * @return
     */
    public User getUserInfoInDB(User user){
        return userMapper.getUserInfo(user);
    }
    /**
     * 修改用户信息
     * @param user
     * @return
     */
    public String changeUser(User user){
        User nowUser = (User) RedisUtils.getObject(user.getUserToken());
        //保证要修改的用户是登录用户
        user.setUserId(nowUser.getUserId());
        //检查用户名是否修改
        if(!user.getUserName().equals(nowUser.getUserName())){
            //修改了就检查新用户名是否存在
            if(userMapper.hasUser(user)>0){
                return "用户名已存在";
            }
        }
        //检查邮箱是否修改
        if(!user.getUserEmail().equals(nowUser.getUserEmail())){
            //修改了就检查邮箱验证码是否正确
            String varCode=user.getVarCode();
            if(varCode==null){
                return "邮箱验证码不能为空";
            }
            if(!user.getVarCode().equals(RedisUtils.get(user.getUserEmail()+"changeEmail"))){
                return "邮箱验证码错误";
            }
            //从redis中删除验证码
            RedisUtils.del(user.getUserEmail()+"changeEmail");
        }
        //修改用户信息并更新redis
        if(userMapper.changeUser(user)>0){
            RedisUtils.setObject(user.getUserToken(),userMapper.getUserInfo(user));
            return "success";
        }
        return "用户信息修改失败";
    }
    /**
     * 根据ID修改密码
     * @param user
     * @return
     */
    public boolean changePassById(User user){
        if(userMapper.changePassById(user)>0) {
            return true;
        }
        return false;
    }

    /**
     * 根据原密码修改密码
     * @param user
     * @param newPass
     * @return
     */
    public boolean changePassByPass(User user, String newPass){
        //保证要修改的用户是登录用户
        user.setUserId(((User)RedisUtils.getObject(user.getUserToken())).getUserId());
        newPass= Md5Util.remd5(newPass);
        //修改密码
        if(userMapper.changePassByPass(user.getUserId(),user.getUserPass(),newPass)>0) {
            return true;
        }
        return false;
    }
    //根据用户名修改密码
    public Integer updatePassByUserName(User user){
        return userMapper.updatePassByUserName(user);
    }
}
