package com.cs.artfactonline;

import com.cs.artfactonline.artifact.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArtfactOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtfactOnlineApplication.class, args);
    }

    @Bean
    public IdWorker idWorker()
    {
        return new IdWorker(1,1);
    }

}
