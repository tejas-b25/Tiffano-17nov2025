package com.tiffino.menuservice.controller;


import com.tiffino.menuservice.dto.CartItemDTO;
import com.tiffino.menuservice.dto.CartItemResponseDTO;
import com.tiffino.menuservice.service.CartServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartServiceI cartService;

    @PostMapping("/add")
    public CartItemResponseDTO addToCart(@RequestBody CartItemDTO dto) {
        return cartService.addToCart(dto);
    }

    @GetMapping("/{userId}")
    public List<CartItemResponseDTO> getCart(@PathVariable Long userId) {
        return cartService.getCartItems(userId);
    }

    @PutMapping("/update/{itemId}")
    public CartItemResponseDTO updateItem(@PathVariable Long itemId, @RequestParam int quantity) {
        return cartService.updateCartItem(itemId, quantity);
    }

    @DeleteMapping("/remove/{itemId}")
    public void removeItem(@PathVariable Long itemId) {
        cartService.removeCartItem(itemId);
    }
}
