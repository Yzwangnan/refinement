package com.refinement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截路径可自行配置多个 可用 ，分隔开
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/sys/**")
                .excludePathPatterns("/project/createProjectId")
                .excludePathPatterns("/project/findAll")
                .excludePathPatterns("/project/reportRecord")
                .excludePathPatterns("/project/reportInfo")
                .excludePathPatterns("/project/workOutput")
                .excludePathPatterns("/project/savePreSpecify")
                .excludePathPatterns("/dept/findAll")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
                .excludePathPatterns("/doc.html");
    }
}
