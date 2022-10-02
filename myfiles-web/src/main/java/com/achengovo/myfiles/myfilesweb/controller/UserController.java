package com.achengovo.myfiles.myfilesweb.controller;

import com.achengovo.lightning.client.Reference;
import com.achengovo.lightning.client.filter.Filter;
import com.achengovo.lightning.client.loadbalance.WeigthRandomLoadbalanceImpl;
import com.achengovo.myfiles.entity.User;
import com.achengovo.myfiles.myfilesweb.filter.LogFilter;
import com.achengovo.myfiles.service.UserService;
import com.achengovo.myfiles.utils.CookiesUtils;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Controller
public class UserController {
    private UserService userService;

    public UserController() throws NacosException, InterruptedException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new LogFilter());
        Reference reference = new Reference("127.0.0.1:8848", UserService.class.getName(), "DEFAULT_GROUP",filters);
        userService= (UserService) reference.createProxy(UserService.class,new WeigthRandomLoadbalanceImpl());
    }

    /**
     * 用户登录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(@RequestBody User user, HttpServletResponse response) {
        System.out.println(user);
        String userToken = userService.login(user);
        if(!"fail".equals(userToken)){
            CookiesUtils.setCookie(response,"userToken",userToken,60*60*24*7);
        }
        return userToken;
    }

    /**
     * 退出登录
     */
    @ResponseBody
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public boolean loginOut(HttpServletRequest request, HttpServletResponse response) {
        //获取cookie
        String userToken=CookiesUtils.getCookie(request,"userToken");
        if(userToken==null){
            return false;
        }
        if(!userService.loginOut(userToken)){
            return false;
        }
        //删除cookie
        CookiesUtils.removeCookie(request,response,"userToken");
        return true;
    }

    /**
     * 获取注册验证码
     * @param email 邮箱
     * @return 是否发送成功
     */
    @ResponseBody
    @RequestMapping(value = "/registerCode")
    public boolean register(String email) throws Exception {
        return userService.getVerifyCode(email,"register");
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 查询用户名是否存在
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/hasUser")
    public boolean hasUser(@RequestBody User user){
        return userService.hasUser(user);
    }

    /**
     * 获取个人资料
     */
    @ResponseBody
    @RequestMapping(value = "/getUserInfo")
    public User getUserInfo(HttpServletRequest request) {
        String userToken =CookiesUtils.getCookie(request,"userToken");
        User user=new User();
        user.setUserToken(userToken);
        User result = userService.getUserInfoInRedis(user);
        return result;
    }

    /**
     * 上传头像
     */
    @ResponseBody
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public boolean upload(@RequestParam("uploadAvatar") List<MultipartFile> uploadfiles,HttpServletRequest request) {
        String userToken =CookiesUtils.getCookie(request,"userToken");
        User user=new User();
        user.setUserToken(userToken);
        //判断上传文件是否存在
        if (uploadfiles.isEmpty()) {
            return false;
        }
        for(MultipartFile file:uploadfiles){
            //文件名
            String originalFilename = file.getOriginalFilename();
//            String dirPath = request.getServletContext().getRealPath("/avatar/");
            //保存地址
            String dirPath = "D:\\MyFiles\\avatar\\";
            //判断系统类型
            if(System.getProperty("os.name").toLowerCase().contains("linux")){
                dirPath="/MyFiles/avatar/";
            }
            File filePath=new File(dirPath);
            //判断路径是否存在，不存在则创建
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            //新文件名
            String newFileName = UUID.randomUUID().toString().replace("-","_")+"_"+originalFilename.substring(originalFilename.lastIndexOf("."));
            try{
                //将文件保存到指定目录
                file.transferTo(new File(dirPath+newFileName));
                user.setUserAvatar(newFileName);
                return userService.uploadAvatar(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取修改邮箱的验证码
     * @param email
     * @return
     * @throws MessagingException
     */
    @ResponseBody
    @RequestMapping(value="/changeEmailCode")
    public boolean changeEmailCode(String email) throws Exception {
        return userService.getVerifyCode(email,"changeEmail");
    }

    /**
     * 修改用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/changeUser", method = RequestMethod.POST)
    public String changeUser(@RequestBody User user,HttpServletRequest request) {
        user.setUserToken(CookiesUtils.getCookie(request,"userToken"));
        return userService.changeUser(user);
    }

    /**
     * 修改密码
     */
    @ResponseBody
    @RequestMapping(value = "/changePassByPass", method = RequestMethod.POST)
    public boolean changePassByPass(@RequestParam String userPass, @RequestParam String newPass,HttpServletRequest request) {
        String userToken=CookiesUtils.getCookie(request,"userToken");
        User user=new User();
        user.setUserToken(userToken);
        user.setUserPass(userPass);
        return userService.changePassByPass(user,newPass);
    }

    /**
     * 根据用户名发送邮件
     */
//    @ResponseBody
//    @RequestMapping(value = "/forgetPassSendEmail")
//    public String forgetPassSendEmail(@RequestBody User user, HttpSession session) throws MessagingException {
//        user = userService.getUserByUserName(user);
//        if (user != null) {
//            String varCode = getRandomVerCode(5);
//            session.setAttribute("varCode", varCode);
//            session.setAttribute("userName", user.getUserName());
//            SendMail.sendNetMail(user.getUserEmail(), "您本次操作的验证码为【" + varCode + "】", "MyFiles验证码");
//            return "success";
//        } else {
//            return "用户名不存在";
//        }
//    }


    /**
     * 新密码
     */
//    @ResponseBody
//    @RequestMapping(value = "/forgetPassNewPass")
//    public String forgetPassNewPass(@RequestBody User user, HttpSession session) {
//        String varCode = (String) session.getAttribute("varCode");
//        if (user.getVarCode().equalsIgnoreCase(varCode)) {
//            user.setUserName((String) session.getAttribute("userName"));
//            if (userService.updatePassByUserName(user) > 0) {
//                return "success";
//            } else {
//                return "错误";
//            }
//        } else {
//            return "验证码错误";
//        }
//    }
}
