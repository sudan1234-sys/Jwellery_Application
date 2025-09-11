package com.example.jwellery.Repositories;

import com.example.jwellery.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Example: get all items in a cart
    List<CartItem> findByCartId(Long cartId);

    // Example: check if product is already in the cart
    CartItem findByCartIdAndProductId(Long cartId, Long productId);
}
