package com.restaurant.service;

import com.restaurant.entity.Item;
import com.restaurant.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Integer id) {
        itemRepository.deleteById(id);
    }

    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<Item> searchItemsByName(String name, Pageable pageable) {
        return itemRepository.findByNameContaining(name, pageable);
    }

    public Page<Item> searchItemsByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return itemRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    public Page<Item> searchItems(String name, Double price, Pageable pageable) {
        if (name != null && price != null) {
            return itemRepository.findByNameContainingAndPrice(name, price, pageable);
        } else if (name != null) {
            return itemRepository.findByNameContaining(name, pageable);
        } else if (price != null) {
            return itemRepository.findByPrice(price, pageable);
        } else {
            return itemRepository.findAll(pageable);
        }
    }
}