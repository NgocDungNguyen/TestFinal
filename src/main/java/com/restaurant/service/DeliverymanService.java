package com.restaurant.service;

import com.restaurant.entity.Deliveryman;
import com.restaurant.repository.DeliverymanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeliverymanService {

    @Autowired
    private DeliverymanRepository deliverymanRepository;

    public Deliveryman addDeliveryman(Deliveryman deliveryman) {
        return deliverymanRepository.save(deliveryman);
    }

    public Deliveryman updateDeliveryman(Deliveryman deliveryman) {
        return deliverymanRepository.save(deliveryman);
    }

    public void deleteDeliveryman(Integer id) {
        deliverymanRepository.deleteById(id);
    }

    public Page<Deliveryman> getAllDeliverymen(Pageable pageable) {
        return deliverymanRepository.findAll(pageable);
    }

    public Page<Deliveryman> searchDeliverymen(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return deliverymanRepository.findAll(pageable);
        }
        return deliverymanRepository.findByNameContainingOrPhoneNumberContaining(searchTerm, searchTerm, pageable);
    }
}