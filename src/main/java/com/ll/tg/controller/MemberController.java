package com.ll.tg.controller;

import com.ll.tg.domain.Member;
import com.ll.tg.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", new MemberForm());
        return "domain/user/sign-up";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberForm memberForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "domain/user/sign-up";
        }

        if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "비밀번호가 일치하지 않습니다.");
            return "domain/user/sign-up";
        }

        Member member = new Member();
        member.setUsername(memberForm.getUsername());
        member.setEmail(memberForm.getEmail());
        member.setPassword(memberForm.getPassword());

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "domain/user/login";
    }
}