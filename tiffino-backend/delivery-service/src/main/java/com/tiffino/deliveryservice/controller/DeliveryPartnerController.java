package com.tiffino.deliveryservice.controller;

import com.tiffino.deliveryservice.entity.DeliveryPartner;
import com.tiffino.deliveryservice.service.DeliveryPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                           // ← ADD THIS
@RequestMapping("/api/delivery-partners")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")// ← ADD THIS
public class DeliveryPartnerController {
    @Autowired
    public DeliveryPartnerService deliveryPartnerService;

    @GetMapping("/available")
    public List<DeliveryPartner> getAvailablePartners()
    {
        return deliveryPartnerService.getAvailablePartners();
    }

    @PostMapping("/createDeliveryPartner")
    public ResponseEntity<DeliveryPartner> createDeliveryPartner(@RequestBody DeliveryPartner deliveryPartner)
    {
        DeliveryPartner savedPartner = deliveryPartnerService.save(deliveryPartner);
        return new ResponseEntity<>(savedPartner, HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public List<DeliveryPartner> getAllPartners()
    {
        return deliveryPartnerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable Long id)
    {
        DeliveryPartner partner = deliveryPartnerService.findById(id);
        if (partner != null) {
            return ResponseEntity.ok(partner);
        }
        return ResponseEntity.notFound().build();
    }
}
