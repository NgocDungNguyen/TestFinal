package com.restaurant.service;

import com.restaurant.entity.Order;
import com.restaurant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> searchOrders(String searchTerm, PageRequest pageRequest) {
        try {
            Integer id = Integer.parseInt(searchTerm);
            Optional<Order> orderOptional = orderRepository.findById(id);
            if (orderOptional.isPresent()) {
                List<Order> orderList = Collections.singletonList(orderOptional.get());
                return new PageImpl<>(orderList, pageRequest, 1);
            } else {
                return Page.empty(pageRequest);
            }
        } catch (NumberFormatException e) {
            try {
                LocalDate date = LocalDate.parse(searchTerm);
                return orderRepository.findByCreationDate(date, pageRequest);
            } catch (Exception ex) {
                // If it's neither a valid ID nor a valid date, search by customer or deliveryman name
                return orderRepository.findByCustomerNameContainingOrDeliverymanNameContaining(searchTerm, searchTerm, pageRequest);
            }
        }
    }

    public Page<Order> searchOrdersByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return orderRepository.findByTotalPriceBetween(minPrice, maxPrice, pageable);
    }
}