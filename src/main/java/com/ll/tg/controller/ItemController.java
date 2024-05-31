package com.ll.tg.controller;

import com.ll.tg.domain.Item;
import com.ll.tg.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("itemForm", new ItemForm());
        return "domain/item/createItem";
    }

    @PostMapping("/write")
    public String write(@Valid ItemForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "domain/item/createItem";
        }

        itemService.saveItem(form);

        return "redirect:/";
    }

    @GetMapping("/items")
    public String items(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "domain/item/items";
    }

    @GetMapping("/survey")
    public String survey() {
        return "domain/item/survey";
    }

    @GetMapping("/introduction")
    public String introduction() {
        return "domain/item/introduction";
    }

    @GetMapping("/api")
    public String api() {
        return "domain/item/api";
    }
}