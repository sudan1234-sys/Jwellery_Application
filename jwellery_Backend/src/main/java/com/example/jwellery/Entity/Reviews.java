package com.example.jwellery.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Reviews")
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String review;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public Reviews(Long id, String review, User user, Product product , int rating) {
        this.id = id;
        this.review = review;
        this.user = user;
        this.product = product;
        this.rating=rating;
    }

    public Reviews() {
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    @JoinColumn(name = "User_Id" , nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="Product_Id",nullable = false)
    private Product product;

}
