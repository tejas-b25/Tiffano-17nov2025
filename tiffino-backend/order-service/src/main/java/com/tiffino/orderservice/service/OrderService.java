package com.tiffino.orderservice.service;

import com.tiffino.orderservice.dto.OrderDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    void deleteOrder(Long id);
}
