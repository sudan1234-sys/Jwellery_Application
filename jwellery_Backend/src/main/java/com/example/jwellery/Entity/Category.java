package com.example.jwellery.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String catImage;
    @Column(nullable = false, unique = true)
    private String name; // e.g. Rings, Necklaces, Earrings
    public Category() {
    }


    public Category(Long id, String name, String catImage) {
        this.id = id;
        this.name = name;
        this.catImage = catImage;
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

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }



}
