package com.tea.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Value("${wechat.upload-path}")
    private String uploadPath;


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + uploadPath);

    }

    // @Override
    // protected void addInterceptors(InterceptorRegistry registry) {
    //     super.addInterceptors(registry);
    //     registry.addInterceptor(loginHandlerInterceptor()).addPathPatterns("/**/*")
    //             .excludePathPatterns("/user/login")
    //             .excludePathPatterns("/companys/downloadIdCard/*")
    //             .excludePathPatterns("/user/logout")
    //             .excludePathPatterns("/webjars/**/*")
    //             .excludePathPatterns("/export/**");
    // }
    //
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     registry.addResourceHandler("/webjars/**")
    //             .addResourceLocations("classpath:/META-INF/resources/webjars/");
    // }
    //
    // @Bean
    // public RestTemplate restTemplate() {
    //     return new RestTemplateBuilder().build();
    // }
}
