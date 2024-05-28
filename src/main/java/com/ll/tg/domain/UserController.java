package com.ll.tg.domain;

import com.ll.tg.controller.SignupForm;
import com.ll.tg.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "domain/user/sign-up";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupForm signUpForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "domain/user/sign-up";
        }

        if (!signUpForm.getPassword().equals(signUpForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "비밀번호가 일치하지 않습니다.");
            return "domain/user/sign-up";
        }

        try {
            userService.register(signUpForm.getUsername(), signUpForm.getEmail(), signUpForm.getPassword());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "domain/user/sign-up";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "domain/user/sign-up";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "domain/user/login";
    }
}