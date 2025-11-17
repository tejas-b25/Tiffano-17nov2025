package com.tiffino.orderservice.service;

import com.tiffino.orderservice.dto.OrderDto;
import com.tiffino.orderservice.dto.OrderItemDto;
import com.tiffino.orderservice.dto.AddressDto;
import com.tiffino.orderservice.entity.Order;
import com.tiffino.orderservice.entity.OrderItem;
import com.tiffino.orderservice.entity.Address;
import com.tiffino.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = mapToEntity(orderDto);

        // Link items
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }

        // Link addresses
        if (order.getAddresses() != null) {
            order.getAddresses().forEach(address -> address.setOrder(order));
        }

        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        return mapToDto(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // ----------------- Mappers -----------------
    private Order mapToEntity(OrderDto dto) {
        if (dto == null) return null;

        return Order.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .orderType(dto.getOrderType())
                .isSubscription(dto.getIsSubscription())
                .subscriptionType(dto.getSubscriptionType())
                .userSubscriptionId(dto.getUserSubscriptionId())
                .orderDate(dto.getOrderDate())
                .deliveryDate(dto.getDeliveryDate())
                .deliveryTimeSlot(dto.getDeliveryTimeSlot())
                .totalAmount(dto.getTotalAmount())
                .orderItems(dto.getOrderItems() != null
                        ? dto.getOrderItems().stream()
                        .map(this::mapToEntity)
                        .collect(Collectors.toList())
                        : null)
                .addresses(dto.getAddresses() != null
                        ? dto.getAddresses().stream()
                        .map(this::mapToEntity)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    private OrderItem mapToEntity(OrderItemDto dto) {
        if (dto == null) return null;

        return OrderItem.builder()
                .id(dto.getId())
                .mealId(dto.getMealId())
                .quantity(dto.getQuantity())
                .customizations(dto.getCustomizations())
                .pricePerItem(dto.getPricePerItem())
                .build();
    }

    private Address mapToEntity(AddressDto dto) {
        if (dto == null) return null;

        return Address.builder()
                .id(dto.getId())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .isDefault(dto.getIsDefault())
                .addressType(dto.getAddressType())
                .build();
    }

    private OrderDto mapToDto(Order order) {
        if (order == null) return null;

        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderType(order.getOrderType())
                .isSubscription(order.getIsSubscription())
                .subscriptionType(order.getSubscriptionType())
                .userSubscriptionId(order.getUserSubscriptionId())
                .orderDate(order.getOrderDate())
                .deliveryDate(order.getDeliveryDate())
                .deliveryTimeSlot(order.getDeliveryTimeSlot())
                .totalAmount(order.getTotalAmount())
                .orderItems(order.getOrderItems() != null
                        ? order.getOrderItems().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList())
                        : null)
                .addresses(order.getAddresses() != null
                        ? order.getAddresses().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    private OrderItemDto mapToDto(OrderItem item) {
        if (item == null) return null;

        return OrderItemDto.builder()
                .id(item.getId())
                .mealId(item.getMealId())
                .quantity(item.getQuantity())
                .customizations(item.getCustomizations())
                .pricePerItem(item.getPricePerItem())
                .build();
    }

    private AddressDto mapToDto(Address address) {
        if (address == null) return null;

        return AddressDto.builder()
                .id(address.getId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.getIsDefault())
                .addressType(address.getAddressType())
                .build();
    }
}
