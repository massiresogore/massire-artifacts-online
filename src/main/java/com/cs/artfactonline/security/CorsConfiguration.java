package com.cs.artfactonline.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                //Active le cors Globalement afin que toutes les requettes javascript et autre puisse acceder
                registry.addMapping("/**");
            }
        };
    }
}
