package com.restaurant.repository;

import com.restaurant.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByCreationDate(LocalDate creationDate, Pageable pageable);
    Page<Order> findByTotalPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    Page<Order> findByCustomerNameContainingOrDeliverymanNameContaining(String customerName, String deliverymanName, Pageable pageable);
}