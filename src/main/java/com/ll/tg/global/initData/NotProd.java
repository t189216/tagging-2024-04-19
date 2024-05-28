package com.ll.tg.global.initData;

import com.ll.tg.controller.SignupForm;
import com.ll.tg.repository.UserRepository;
import com.ll.tg.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Profile("!prod")
@Configuration
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final UserService userService;
    private final UserRepository userRepository;

    @Bean
    @Order(2)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                // 샘플 데이터 생성
                SignupForm adminForm = new SignupForm("admin", "admin@example.com", "abcd123412", "abcd123412");
                SignupForm user1Form = new SignupForm("user1", "user1@example.com", "abcd123412", "abcd123412");
                SignupForm user2Form = new SignupForm("user2", "user2@example.com", "abcd123412", "abcd123412");

                // 샘플 데이터 등록
                userService.register(adminForm.getUsername(), adminForm.getEmail(), adminForm.getPassword());
                userService.register(user1Form.getUsername(), user1Form.getEmail(), user1Form.getPassword());
                userService.register(user2Form.getUsername(), user2Form.getEmail(), user2Form.getPassword());
            }
        };
    }
}