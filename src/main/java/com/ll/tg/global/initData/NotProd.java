package com.ll.tg.global.initData;

import com.ll.tg.controller.ItemForm;
import com.ll.tg.domain.Member;
import com.ll.tg.repository.MemberRepository;
import com.ll.tg.service.ItemService;
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
    private final ItemService itemService;

    @Bean
    @Order(2)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberRepository.findByName("admin").isEmpty()) {
                Member member1 = new Member();
                Member member2 = new Member();
                Member member3 = new Member();

                member1.setName("admin");
                member1.setEmail("admin@example.com");
                member1.setPassword("abcd123412");

                member2.setName("user1");
                member2.setEmail("user1@example.com");
                member2.setPassword("abcd123412");

                member3.setName("user2");
                member3.setEmail("user2@example.com");
                member3.setPassword("abcd123412");

                memberService.join(member1);
                memberService.join(member2);
                memberService.join(member3);

                for (int i = 1; i <= 10; i++) {
                    ItemForm itemForm = new ItemForm();
                    itemForm.setTitle("Sample Title " + i);
                    itemForm.setContent("Sample Content " + i);
                    itemService.saveItem(itemForm);
                }
            }
        };
    }
}