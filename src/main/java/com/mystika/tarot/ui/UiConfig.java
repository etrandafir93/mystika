package com.mystika.tarot.ui;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class UiConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring:[^\\.]*}")
            .setViewName("forward:/index.html");
        registry.addViewController("/**/{spring:[^\\.]*}")
            .setViewName("forward:/index.html");
    }

}
