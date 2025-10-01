package com.example.jwellery.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.jwellery.Entity.Reviews;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews,Long> {
    @Query("SELECT r FROM Reviews r JOIN FETCH r.user WHERE r.product.id = :productId")
    List<Reviews> findAllByProductIdWithUser(@Param("productId") Long productId);

}
