package com.example.jwellery.Dto;

// Cart DTO
import java.util.List;

public class CartDTO {
    private Long cartId;
    private Long userId;

    public CartDTO() {
    }

    private String status;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public CartDTO(Long cartId, Long userId, String status, List<CartItemDTO> items, double total) {
        this.cartId = cartId;
        this.userId = userId;
        this.status = status;
        this.items = items;
        this.total = total;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    private List<CartItemDTO> items;
    private double total;
}

