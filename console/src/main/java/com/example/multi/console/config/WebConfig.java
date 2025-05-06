package com.example.multi.console.config;

import com.example.multi.console.Interceptor.LoginInterceptor;
import com.example.multi.module.user.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.List;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ApplicationArguments appArguments;

//    public WebConfig(ApplicationArguments appArguments) {
//        this.appArguments = appArguments;
//        log.info("WebConfig initialized with ApplicationArguments: {}", appArguments);
//    }
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(newUserAuthResolver());
//    }
//
//    @Bean
//    public UserAuthorityResolver newUserAuthResolver() {
//        return new UserAuthorityResolver(appArguments);
//
//
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                // 拦截所有请求
//                .addPathPatterns("/**")
//                //放行路径
//                .excludePathPatterns("/user/login");
//    }
public WebConfig(ApplicationArguments appArguments) {
    this.appArguments = appArguments;
    log.info("WebConfig initialized with ApplicationArguments: {}", appArguments);
}

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(newUserAuthResolver());
    }

    @Bean
    public UserAuthorityResolver newUserAuthResolver() {
        return new UserAuthorityResolver(appArguments);
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }



}