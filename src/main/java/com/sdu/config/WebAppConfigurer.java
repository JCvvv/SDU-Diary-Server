package com.sdu.config;

import com.sdu.interceptor.SysInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用户头像配置
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Bean
    public SysInterceptor sysInterceptor(){
        return new SysInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String []patterns=new String[]{"/user/wxLogin","/image/**"};
        registry.addInterceptor(sysInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/root/project/user/**").addResourceLocations("file:/root/project/user/");
        registry.addResourceHandler("/root/project/cover/**").addResourceLocations("file:/root/project/cover/");
        // registry.addResourceHandler("/root/project/user/**").addResourceLocations("file:C:\\Users\\Jc\\Desktop\\sdu\\img\\");
      //  registry.addResourceHandler("/root/project/cover/**").addResourceLocations("file:C:\\Users\\Jc\\Desktop\\sdu\\img\\");

    }
}
