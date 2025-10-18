package com.example.jwellery.Dto;

public class ReviewDto {
    private String review;
    private String username;
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Constructor
    public ReviewDto(String review, String username , int rating) {
        this.review = review;
        this.username = username;
        this.rating=rating;
    }

    // Getters and setters
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
