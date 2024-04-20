//package com.ll.tg.global.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/h2-console/**", "/css/**", "/")
//                                .permitAll()
//                                .anyRequest().authenticated()
//                )
//                .headers(
//                        headers ->
//                                headers.frameOptions(
//                                        frameOptions ->
//                                                frameOptions.sameOrigin()
//                                )
//                )
//                .csrf(
//                        csrf ->
//                                csrf.ignoringRequestMatchers(
//                                        "/h2-console/**"
//                                )
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 생성 정책
//                        .maximumSessions(1) // 동시 세션 수 제한
//                        .maxSessionsPreventsLogin(false) // 동시 로그인 차단 여부
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}