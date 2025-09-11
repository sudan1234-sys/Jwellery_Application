package com.example.jwellery.Mapper;

import com.example.jwellery.Dto.ProductDTO;
import com.example.jwellery.Entity.Category;
import com.example.jwellery.Entity.Product;
import com.example.jwellery.Entity.Product_Image;
import com.example.jwellery.Repositories.CategoryRepository;
import com.example.jwellery.Repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductMapper(CategoryRepository categoryRepository, ProductImageRepository productImageRepository) {
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    // DTO → Entity
    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());

        // category by name
        Category category = categoryRepository.findByName(dto.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryName()));
        product.setCategory(category);

        return product;
    }

    // Entity → DTO
    public ProductDTO toDto(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                product.getCategory().getName(),
                productImageRepository.findByProductId(product.getId())
                        .stream()
                        .map(Product_Image::getImageUrl)
                        .toList()
        );
    }
}

