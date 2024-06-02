package com.ll.tg.service;

import com.ll.tg.controller.ItemForm;
import com.ll.tg.domain.Item;
import com.ll.tg.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Optional<Item> findByItemId(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Transactional
    public void updateItem(Long itemId, String title, String content) {
        Optional<Item> getItem = itemRepository.findById(itemId);

        if (getItem.isPresent()) {

            Item item = getItem.get();
            item.setTitle(title);
            item.setContent(content);

            itemRepository.save(item);
        }
    }

    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
