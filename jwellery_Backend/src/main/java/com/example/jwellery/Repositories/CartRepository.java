package com.example.jwellery.Repositories;

import com.example.jwellery.Entity.Cart;
import com.example.jwellery.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Example: find all carts of a user
    List<Cart> findByUserId(Long userId);

    // Example: get the active cart of a user
    Optional<Cart> findByUserAndStatus(User user, String status);

}
