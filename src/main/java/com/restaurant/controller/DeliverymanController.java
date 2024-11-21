package com.restaurant.controller;

import com.restaurant.entity.Deliveryman;
import com.restaurant.service.DeliverymanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliverymen")
public class DeliverymanController {

    @Autowired
    private DeliverymanService deliverymanService;

    @PostMapping
    public ResponseEntity<Deliveryman> addDeliveryman(@RequestBody Deliveryman deliveryman) {
        Deliveryman savedDeliveryman = deliverymanService.addDeliveryman(deliveryman);
        return ResponseEntity.ok(savedDeliveryman);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deliveryman> updateDeliveryman(@PathVariable Integer id, @RequestBody Deliveryman deliveryman) {
        deliveryman.setId(id);
        Deliveryman updatedDeliveryman = deliverymanService.updateDeliveryman(deliveryman);
        return ResponseEntity.ok(updatedDeliveryman);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryman(@PathVariable Integer id) {
        deliverymanService.deleteDeliveryman(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Deliveryman>> searchDeliverymen(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sort[0]);
        PageRequest pageRequest = PageRequest.of(page, size, sortObj);

        Page<Deliveryman> deliverymen = deliverymanService.searchDeliverymen(name, pageRequest);
        return ResponseEntity.ok(deliverymen);
    }
}