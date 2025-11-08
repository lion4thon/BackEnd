package com.api.mov.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // API의 모든 경로에 대해 CORS 설정을 적용한다.
                .allowedOrigins("http://localhost:3000") //프론트 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //허용할 HTTP 메소드 지정
                .allowedHeaders("*") //허용할 요청 헤더
                .allowCredentials(true); //자격 증명(쿠키, 인증 헤더 등)을 허용할지 말지의 여부 결정
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
