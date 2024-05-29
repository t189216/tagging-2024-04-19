package com.ll.tg.global.initData;

import com.ll.tg.domain.Member;
import com.ll.tg.repository.MemberRepository;
import com.ll.tg.service.MemberService;
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
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Bean
    @Order(2)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberRepository.findByUsername("admin").isEmpty()) {
                Member member1 = new Member();
                Member member2 = new Member();
                Member member3 = new Member();


            }
        };
    }
}