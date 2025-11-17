package com.tiffino.deliveryservice.service;

import com.tiffino.deliveryservice.entity.DeliveryPartner;
import com.tiffino.deliveryservice.repository.DeliveryPartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryPartnerService
{
    @Autowired
    public DeliveryPartnerRepository deliveryPartnerRepository;

    // 1. Used in: GET /api/delivery-partners/available
    public List<DeliveryPartner> getAvailablePartners()
    {
        return deliveryPartnerRepository.findByStatus(DeliveryPartner.Status.AVAILABLE);
    }

    //  2. Used in: POST /api/delivery-partners
    public DeliveryPartner save(DeliveryPartner partner)
    {
        return deliveryPartnerRepository.save(partner);
    }

    //  3. Used in: GET /api/delivery-partners
    public List<DeliveryPartner> findAll()
    {
        return deliveryPartnerRepository.findAll();
    }

    //  4. Used in: GET /api/delivery-partners/{id}
    public DeliveryPartner findById(Long id)
    {
        Optional<DeliveryPartner> partner = deliveryPartnerRepository.findById(id);
        return partner.orElse(null);
    }
}
