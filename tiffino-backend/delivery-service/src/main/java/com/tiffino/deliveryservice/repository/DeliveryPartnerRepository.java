package com.tiffino.deliveryservice.repository;

import com.tiffino.deliveryservice.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long>
{
    List<DeliveryPartner> findByStatus(DeliveryPartner.Status status);

}
