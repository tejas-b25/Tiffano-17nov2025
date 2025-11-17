package com.tiffino.deliveryservice.service;


import com.tiffino.deliveryservice.client.OrderClient;
import com.tiffino.deliveryservice.dto.AssignDeliveryRequest;
import com.tiffino.deliveryservice.dto.DeliveryStatusUpdateDto;
import com.tiffino.deliveryservice.dto.LocationUpdateDto;
import com.tiffino.deliveryservice.entity.Delivery;
import com.tiffino.deliveryservice.entity.DeliveryPartner;
import com.tiffino.deliveryservice.repository.DeliveryPartnerRepository;
import com.tiffino.deliveryservice.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService
{
     @Autowired
    private DeliveryRepository deliveryRepository;

     @Autowired
    private DeliveryPartnerRepository partnerRepository;

    @Autowired
    private OrderClient orderClient;


    //  Assign delivery partner to an order
    public Delivery assignPartner(AssignDeliveryRequest request)
    {

       //  ✅ Step 1: Validate orderId via Feign
//        OrderDto order;
//        try {
//            order = orderClient.getOrderById(request.getOrderId());
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found in Order Service");
//        }
        //User when we dont have order service in demo
         Long confirmedOrderId = request.getOrderId(); // simulate success for testing purpose

        // ✅ Step 1: Validate orderId via Feign
//        OrderDto order;
//        try {
//            order = orderClient.getOrderById(request.getOrderId());
//            if (order == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found (fallback used)");
//            }
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Order Service is not available right now");
//        }

        // ✅ Step 2: Continue with your existing logic
        List<DeliveryPartner> available = partnerRepository.findByStatus(DeliveryPartner.Status.AVAILABLE);

        if (available.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No delivery partner available");
        }

        DeliveryPartner assigned = available.get(0);
        assigned.setStatus(DeliveryPartner.Status.ON_DELIVERY);
        partnerRepository.save(assigned);

        Delivery delivery = Delivery.builder()
                //.orderId(order.getId()) // use the confirmed orderId
                .orderId(confirmedOrderId)
                .deliveryPartner(assigned)
                .pickupTime(LocalDateTime.now())
                //.estimatedDeliveryTime(request.getEstimatedDeliveryTime())
                .status(Delivery.Status.ASSIGNED)
                .currentLatitude(assigned.getCurrentLatitude())
                .currentLongitude(assigned.getCurrentLongitude())
                .build();

        return deliveryRepository.save(delivery);
    }


    //  Update delivery status
    public Delivery updateStatus(DeliveryStatusUpdateDto dto) {
        Delivery delivery = deliveryRepository.findByOrderId(dto.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));

        try {
            Delivery.Status newStatus = Delivery.Status.valueOf(dto.getStatus().toUpperCase());
            delivery.setStatus(newStatus);

            if (newStatus == Delivery.Status.DELIVERED) {
                delivery.setActualDeliveryTime(LocalDateTime.now());

                 DeliveryPartner partner = delivery.getDeliveryPartner();
                 partner.setStatus(DeliveryPartner.Status.AVAILABLE);
                 partnerRepository.save(partner);
            }

            return deliveryRepository.save(delivery);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + dto.getStatus());
        }
    }

    //  Update current location
    public Delivery updateLocation(LocationUpdateDto dto) {
        Delivery delivery = deliveryRepository.findByOrderId(dto.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));

        delivery.setCurrentLatitude(dto.getLatitude());
        delivery.setCurrentLongitude(dto.getLongitude());

        return deliveryRepository.save(delivery);
    }

    // Get delivery by orderId
    public Delivery getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
    }

    public List<Delivery> getAllDeliveries()
    {
        return deliveryRepository.findAll();
    }


}
