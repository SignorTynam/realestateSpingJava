// src/main/java/com/realestate/realestate/config/MvcConfig.java
package com.realestate.realestate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/uploads/**")
          .addResourceLocations("file:upload/");
    }
}
