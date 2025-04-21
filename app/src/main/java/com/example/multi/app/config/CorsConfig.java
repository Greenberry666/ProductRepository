package com.example.multi.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

public class CorsConfig implements WebMvcConfigurer {
    //Access-Control-Allow-Origin: 指定允许访问的域。
    //Access-Control-Allow-Methods: 指定允许的 HTTP 请求方法。
    //Access-Control-Allow-Headers: 指定允许的请求头。
    //Access-Control-Allow-Credentials: 是否允许发送 Cookie 等认证信息。
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);  //cookie允许

        // 允许访问的客户端域名
        List<String> allowedOriginPatterns = new ArrayList<>();
        // for web dev
        allowedOriginPatterns.add("http://web.console.vvlife.com:8181");
        //for dev

        //for pro
        allowedOriginPatterns.add("https://xcx.vvlife.vip");
        allowedOriginPatterns.add("https://h5.vvlife.vip");
        allowedOriginPatterns.add("https://h5.server.vvlife.vip");


        corsConfiguration.setAllowedOriginPatterns(allowedOriginPatterns);
//        corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 允许任何头
        corsConfiguration.addAllowedMethod("*"); // 允许任何方法（post、get等）
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
