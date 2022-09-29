package com.achengovo.myfiles.myfilesweb.Interceptor;

import com.achengovo.myfiles.entity.User;
import com.achengovo.myfiles.utils.CookiesUtils;
import com.achengovo.myfiles.utils.RedisUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 登录拦截器
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userToken= CookiesUtils.getCookie(request,"userToken");
        if(userToken==null){
            response.sendRedirect("/page/login");
            return false;
        }
        User user= (User) RedisUtils.getObject(userToken);
        System.out.println(user);
        if(user!=null){
            return true;
        }
        //重定向
        response.sendRedirect("/page/login");
        return false;
    }
}
