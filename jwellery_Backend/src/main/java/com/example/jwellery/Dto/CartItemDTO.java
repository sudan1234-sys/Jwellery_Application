package com.example.jwellery.Dto;

// CartItem DTO
public class CartItemDTO {
    private Long productId;
    private String productName;
    private String image;
    private double price;
    private int quantity;

    public CartItemDTO(Long productId, String productName, String image, double price, double subtotal, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.subtotal = subtotal;
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private double subtotal;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItemDTO() {
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}

