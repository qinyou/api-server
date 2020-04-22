package com.qinyou.apiserver.core.config;


import com.qinyou.apiserver.core.component.JwtArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mvc 配置
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    JwtArgumentResolver jwtArgumentResolver;
    // 文件上传 资源文件解析
    @Value("${app.upload.access-path}")
    String uploadAccessPath;
    @Value("${app.upload.upload-folder}")
    String uploadFileFolder;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 增加 jwt 参数解析为controller 方法参数
        argumentResolvers.add(jwtArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件上传后 静态文件访问路径
        registry.addResourceHandler(uploadAccessPath).addResourceLocations("file:" + uploadFileFolder);
    }


}
