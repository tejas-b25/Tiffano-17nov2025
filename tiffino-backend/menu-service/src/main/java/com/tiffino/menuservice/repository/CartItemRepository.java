package com.tiffino.menuservice.repository;

import com.tiffino.menuservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByUserIdAndIsActiveTrue(Long userId);

}
