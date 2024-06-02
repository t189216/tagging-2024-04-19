package com.ll.tg.controller;

import com.ll.tg.domain.Item;
import com.ll.tg.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("{itemId}/update")
    public String update(@PathVariable("itemId") Long itemId, Model model) {

        Optional<Item> getItem = itemService.findByItemId(itemId);
        ItemForm form = new ItemForm();

        if (getItem.isPresent()) {
            Item item = getItem.get();
            form.setTitle(item.getTitle());
            form.setContent(item.getContent());
        }

        model.addAttribute("form", form);
        return "domain/item/updateItem";
    }

    @PostMapping("{itemId}/update")
    public String update(@PathVariable Long itemId, @ModelAttribute("form") ItemForm form) {
        itemService.updateItem(itemId, form.getTitle(), form.getContent());
        return "redirect:/items";
    }

    @GetMapping("{itemId}/delete")
    public String delete(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return "redirect:/items";
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