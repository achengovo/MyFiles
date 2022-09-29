package com.achengovo.myfiles.myfilesweb;

import com.achengovo.myfiles.myfilesweb.Interceptor.LoginInterceptor;
import com.achengovo.myfiles.myfilesweb.Interceptor.UserFileInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootWebMvcConfigurer implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        //设置默认首页
        registry.addViewController("/").setViewName("forward:/page/file");
    }
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //头像和文件目录映射
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
            registry.addResourceHandler("/userFile/**")
                    .addResourceLocations("file:/MyFiles/userFile/");
            registry.addResourceHandler("/avatar/**")
                    .addResourceLocations("file:/MyFiles/avatar/");
        }else{
            registry.addResourceHandler("/userFile/**")
                    .addResourceLocations("file:D:/MyFiles/userFile/");
            registry.addResourceHandler("/avatar/**")
                    .addResourceLocations("file:D:/MyFiles/avatar/");
        }
    }
    /**
     * 添加拦截器
     * @param registry
     */
    @Autowired
    private UserFileInterceptor userFileInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        //登录拦截
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/page/login")
                .excludePathPatterns("/page/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/registerCode")
                .excludePathPatterns("/register")
                .excludePathPatterns("/hasUser");
        //文件目录访问拦截
        registry.addInterceptor(userFileInterceptor)
                .addPathPatterns("/userFile/**");
    }
}
