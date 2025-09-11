package com.example.jwellery.Controllers;
import com.example.jwellery.Dto.CartDTO;
import com.example.jwellery.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/cart/items")
    public CartDTO addProduct(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        return cartService.addProductToCart(userId, productId, quantity);
    }

    @DeleteMapping("/{userId}/cart/items/{productId}")
    public CartDTO removeProduct(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        return cartService.removeProductFromCart(userId, productId);
    }

    @GetMapping("/{userId}/cart")
    public CartDTO getCart(@PathVariable Long userId) {
        return cartService.getUserCart(userId);
    }

    // in CartController
    @PutMapping("/{userId}/cart/items")
    public CartDTO updateCartItemQuantity(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        return cartService.updateProductQuantity(userId, productId, quantity);
    }

}


