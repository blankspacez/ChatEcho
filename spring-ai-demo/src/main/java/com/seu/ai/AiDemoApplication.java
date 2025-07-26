package com.seu.ai;


import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.seu.ai.mapper")
public class AiDemoApplication {
/*    @Value("${DASHSCOPE_API_KEY}")
    private String apiKey;

    @PostConstruct
    public void checkKey() {
        System.out.println("API Key: " + apiKey);
    }*/

    public static void main(String[] args) {
        SpringApplication.run(AiDemoApplication.class, args);
    }

}
