package com.api.mov.global.config;

import com.api.mov.global.jwt.JwtAuthenticationFilter;
import com.api.mov.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                //CSRF 보호를 비활성화
                //REST API에서는 세션을 사용하지 않으므로 일반적으로 비활성화한다.
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //HTTP 요청 인가 규칙 설정
                .authorizeHttpRequests(authorize -> authorize
                        //로그인, 회원가입 API는 인증 없이 접근 허용
                        .requestMatchers("/api/auth/signup","api/auth/login").permitAll()
                        .requestMatchers("/api/passes").permitAll()
                        .requestMatchers("/api/facilities/**").hasRole("USER")
                        .requestMatchers("/api/reservations").hasRole("USER")
                        .requestMatchers("/api/pass","/api/my-passes").hasRole("USER")
                        .requestMatchers("/api/summary").hasRole("USER")
                        .requestMatchers("/api/payment/complete").hasRole("USER")


                        //나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
