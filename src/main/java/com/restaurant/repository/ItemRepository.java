package com.restaurant.repository;

import com.restaurant.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.entity.Item;
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findByNameContaining(String name, Pageable pageable);
    Page<Item> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    Page<Item> findByNameContainingAndPrice(String name, Double price, Pageable pageable);
    Page<Item> findByPrice(Double price, Pageable pageable);
}