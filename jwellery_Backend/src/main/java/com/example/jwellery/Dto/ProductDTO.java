package com.example.jwellery.Dto;

import lombok.*;

import java.util.List;


public class ProductDTO {
    private Long id;
    private String name;
    private double averageRating;
    private int reviewCount;

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public ProductDTO(Long id, String name, double averageRating, int reviewCount, Double price, String description, long stock, String categoryName, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.categoryName = categoryName;
        this.imageUrls = imageUrls;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    private Double price;
    private String description;
    private long stock;
    private String categoryName;
    private List<String> imageUrls;
}
