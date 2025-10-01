package com.example.jwellery.Dto;

public class ReviewDto {
    private String review;
    private String username;

    // Constructor
    public ReviewDto(String review, String username) {
        this.review = review;
        this.username = username;
    }

    // Getters and setters
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
