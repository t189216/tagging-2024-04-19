package com.ll.tg.service;

import com.ll.tg.controller.ItemForm;
import com.ll.tg.domain.Item;
import com.ll.tg.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(ItemForm itemForm) {

        Item item = Item.builder()
                .title(itemForm.getTitle())
                .content(itemForm.getContent())
                .build();
        itemRepository.save(item);

        return item.getId();
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}
