package com.example.jwellery.Repositories;

import com.example.jwellery.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findAllByProduct_IdIn(List<Long> productIds);

    List<ProductImage> findByProductId(Long productId);
    void deleteByProductId(Long productId);


}
