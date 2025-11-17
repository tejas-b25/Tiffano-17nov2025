package com.tiffino.menuservice.service;


import com.tiffino.menuservice.dto.CartItemDTO;
import com.tiffino.menuservice.dto.CartItemResponseDTO;

import java.util.List;

public interface CartServiceI
{
    CartItemResponseDTO addToCart(CartItemDTO dto);

    List<CartItemResponseDTO> getCartItems(Long userId);

    CartItemResponseDTO updateCartItem(Long itemId, int quantity);

    void removeCartItem(Long itemId);
}
