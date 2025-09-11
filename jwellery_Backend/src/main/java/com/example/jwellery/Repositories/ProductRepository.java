package com.example.jwellery.Repositories;

import com.example.jwellery.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Use EntityGraph to fetch the single-valued association 'category' together with Product.
     * This avoids per-row lazy loads for category when paging.
     */
    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAll(Pageable pageable);
    @Query(value = """
SELECT * FROM products p
WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
  AND (:minPrice IS NULL OR p.price >= :minPrice)
  AND (:maxPrice IS NULL OR p.price <= :maxPrice)
  AND (:search IS NULL OR
       LOWER(p.name::text) LIKE LOWER(CONCAT('%', :search, '%')) OR
       LOWER(p.description::text) LIKE LOWER(CONCAT('%', :search, '%')))
""", nativeQuery = true)
    Page<Product> findFilteredProducts(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("search") String search,
            Pageable pageable
    );

    List<Product> findByFeaturedTrue(Pageable pageable);
    List<Product> findTop5ByCategoryId(Long categoryId);
}
