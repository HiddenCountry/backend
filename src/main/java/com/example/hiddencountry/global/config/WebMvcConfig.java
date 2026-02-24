package com.example.hiddencountry.global.config;

import com.example.hiddencountry.global.handler.HiddenCountryUserArgumentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final HiddenCountryUserArgumentHandler hiddenCountryUserArgumentHandler;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(hiddenCountryUserArgumentHandler);
    }
}
