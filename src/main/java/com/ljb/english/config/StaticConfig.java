package com.ljb.english.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //添加默认的静态资源访问路径
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:static/","classpath:META-IFA/resources/",
                        "classpath:resources/","classpath:public/","classpath:/");
        registry.addResourceHandler("**.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/audio/**")
                .addResourceLocations("file:"+ GlobalStatic.AUDIO_PATH);

    }
}
