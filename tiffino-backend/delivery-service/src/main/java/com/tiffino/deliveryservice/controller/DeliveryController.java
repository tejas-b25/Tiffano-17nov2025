package com.tiffino.deliveryservice.controller;

import com.tiffino.deliveryservice.dto.AssignDeliveryRequest;
import com.tiffino.deliveryservice.dto.DeliveryStatusUpdateDto;
import com.tiffino.deliveryservice.dto.LocationUpdateDto;
import com.tiffino.deliveryservice.entity.Delivery;
import com.tiffino.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DeliveryController
{

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/run")
    public String welcome()
    {
        return "Welcome to delivery service!";
    }

    @PostMapping("/assign")
    public ResponseEntity<Delivery> assignPartner(@RequestBody AssignDeliveryRequest request) {
        Delivery delivery = deliveryService.assignPartner(request);
        return new ResponseEntity<>(delivery, HttpStatus.CREATED);
    }

    @PutMapping("/status")
    public ResponseEntity<Delivery> updateStatus(@RequestBody DeliveryStatusUpdateDto dto) {
        Delivery updated = deliveryService.updateStatus(dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/location")
    public ResponseEntity<Delivery> updateLocation(@RequestBody LocationUpdateDto dto) {
        Delivery updated = deliveryService.updateLocation(dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable Long orderId) {
        Delivery delivery = deliveryService.getDeliveryByOrderId(orderId);
        return ResponseEntity.ok(delivery);
    }

    @GetMapping("/allOrderDeliveries")
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

}
