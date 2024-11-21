package com.restaurant.repository;

import com.restaurant.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.entity.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findByNameContainingOrPhoneNumberContaining(String name, String phoneNumber, Pageable pageable);
}