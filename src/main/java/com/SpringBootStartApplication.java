package com;

import org.springframework.boot.builder.SpringApplicationBuilder;
//1.5.8.RELEASE 版本
//import org.springframework.boot.web.support.SpringBootServletInitializer;
//2.0.1.RELEASE 版本
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class SpringBootStartApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }
}
