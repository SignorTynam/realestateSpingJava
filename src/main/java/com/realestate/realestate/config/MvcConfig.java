// src/main/java/com/realestate/realestate/config/MvcConfig.java
package com.realestate.realestate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Value("${file.upload-dir}")
  private String uploadDir;    // e.g. “upload”

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // map all /upload/** requests to the filesystem folder
    registry
      .addResourceHandler("/upload/**")
      .addResourceLocations("file:" + uploadDir + "/");
  }
}
