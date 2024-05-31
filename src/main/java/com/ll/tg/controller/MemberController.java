package com.ll.tg.controller;

import com.ll.tg.domain.Member;
import com.ll.tg.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@PreAuthorize("isAnonymous()")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", new MemberForm());
        return "domain/member/sign-up";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberForm memberForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "domain/member/sign-up";
        }

        if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "비밀번호가 일치하지 않습니다.");
            return "domain/member/sign-up";
        }

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setEmail(memberForm.getEmail());
        member.setPassword(passwordEncoder.encode(memberForm.getPassword()));

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "domain/member/login";
    }
}