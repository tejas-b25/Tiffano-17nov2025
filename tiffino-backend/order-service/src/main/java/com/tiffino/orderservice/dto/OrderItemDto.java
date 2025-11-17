package com.tiffino.orderservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long id;
    private Long mealId;
    private Integer quantity;
    private String customizations;
    private BigDecimal pricePerItem;
}
