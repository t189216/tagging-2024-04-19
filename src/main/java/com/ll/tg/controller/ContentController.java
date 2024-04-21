package com.ll.tg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/survey")
    public String survey() {
        return "domain/content/survey";
    }

    @GetMapping("/introduction")
    public String introduction() {
        return "domain/content/introduction";
    }
}