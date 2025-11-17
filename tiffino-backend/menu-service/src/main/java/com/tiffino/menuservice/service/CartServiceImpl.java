package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.CartItemDTO;
import com.tiffino.menuservice.dto.CartItemResponseDTO;
import com.tiffino.menuservice.entity.CartItem;
import com.tiffino.menuservice.entity.Cuisine;
import com.tiffino.menuservice.repository.CartItemRepository;
import com.tiffino.menuservice.repository.CuisineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartServiceI {

    @Autowired
    private CartItemRepository repo;

    @Autowired
    private CuisineRepository cuisineRepo;

    @Override
    public CartItemResponseDTO addToCart(CartItemDTO dto) {
        Cuisine cuisine = cuisineRepo.findById(dto.getCuisine_id())
                .orElseThrow(() -> new RuntimeException("Cuisine not found"));

        CartItem item = CartItem.builder()
                .userId(dto.getUserId())
                .cuisine(cuisine)
                .quantity(dto.getQuantity())
                .addedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        CartItem saved = repo.save(item);
        return toDTO(saved);
    }

    @Override
    public List<CartItemResponseDTO> getCartItems(Long userId) {
        return repo.findByUserIdAndIsActiveTrue(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponseDTO updateCartItem(Long itemId, int quantity) {
        CartItem item = repo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);
        return toDTO(repo.save(item));
    }

    @Override
    public void removeCartItem(Long itemId) {
      CartItem item = repo.findById(itemId)
               .orElseThrow(() -> new RuntimeException("Cart item not found"));
       item.setActive(false);
        repo.save(item);
        item.setCuisine(null);
        repo.deleteById(itemId);
    }

    private CartItemResponseDTO toDTO(CartItem item) {
        return new CartItemResponseDTO(
                item.getId(),
                item.getUserId(),
                item.getCuisine().getId(), // get cuisine ID from entity
                item.getQuantity()
        );
    }
}
