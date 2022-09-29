package com.achengovo.myfiles.myfilesweb.Interceptor;

import com.achengovo.myfiles.entity.User;
import com.achengovo.myfiles.utils.CookiesUtils;
import com.achengovo.myfiles.utils.RedisUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class UserFileInterceptor implements HandlerInterceptor {
    /**
     * 文件目录访问拦截器
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userToken= CookiesUtils.getCookie(request,"userToken");
        User user= (User) RedisUtils.getObject(userToken);
        if(user==null){
            return false;
        }
        //获取请求的url
        String url = request.getRequestURI();
        //截取url
        String requestId=url.split("/")[2];
        if(requestId.equals(String.valueOf(user.getUserId()))){
            return true;
        }
        return false;
    }
}
