package com.example.jwellery.Services;

import com.example.jwellery.Dto.CartDTO;
import com.example.jwellery.Entity.Cart;
import com.example.jwellery.Entity.CartItem;
import com.example.jwellery.Entity.Product;
import com.example.jwellery.Entity.User;
import com.example.jwellery.Mapper.CartMapper;
import com.example.jwellery.Repositories.CartItemRepository;
import com.example.jwellery.Repositories.CartRepository;
import com.example.jwellery.Repositories.ProductRepository;
import com.example.jwellery.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartMapper cartMapper;

    /**
     * Ensure the user always has an active cart.
     */
    private Cart assignCartToUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUserAndStatus(user, "ACTIVE")
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setStatus("ACTIVE");
                    return cartRepository.save(newCart);
                });
    }

    /**
     * Add product to cart (or increase quantity if it already exists).
     */
    public CartDTO addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = assignCartToUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setPrice(existingItem.getQuantity() * product.getPrice());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice() * quantity);
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem);
        }

        Cart saved = cartRepository.save(cart);
        return cartMapper.toDTO(saved);
    }

    /**
     * Remove product from cart.
     */
    public CartDTO removeProductFromCart(Long userId, Long productId) {
        Cart cart = assignCartToUser(userId);

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        return cartMapper.toDTO(cartRepository.save(cart));
    }

    /**
     * Get active cart for a user.
     */
    public CartDTO getUserCart(Long userId) {
        Cart cart = assignCartToUser(userId);
        return cartMapper.toDTO(cart);
    }
    public CartDTO updateProductQuantity(Long userId, Long productId, int quantity) {
        Cart cart = assignCartToUser(userId);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        item.setQuantity(quantity);
        item.setPrice(item.getProduct().getPrice() * quantity);
        cartItemRepository.save(item);

        Cart saved = cartRepository.save(cart);
        return cartMapper.toDTO(saved);
    }

}
