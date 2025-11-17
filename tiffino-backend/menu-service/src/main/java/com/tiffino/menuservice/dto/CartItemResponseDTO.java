package com.tiffino.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    private Long itemId;
    private Long userId;
    private Long cuisine_id;
    private int quantity;


}