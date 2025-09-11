package com.example.jwellery.Repositories;

import com.example.jwellery.Entity.Product_Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<Product_Image, Long> {

    List<Product_Image> findAllByProduct_IdIn(List<Long> productIds);

    List<Product_Image> findByProductId(Long productId);
    void deleteByProductId(Long productId);


}
