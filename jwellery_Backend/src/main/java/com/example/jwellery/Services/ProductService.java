package com.example.jwellery.Services;
import com.example.jwellery.Entity.Category;
import com.example.jwellery.Entity.Product;
import com.example.jwellery.Entity.ProductImage;
import com.example.jwellery.Mapper.ProductMapper;
import com.example.jwellery.Repositories.CategoryRepository;
import com.example.jwellery.Repositories.ProductImageRepository;
import com.example.jwellery.Repositories.ProductRepository;
import com.example.jwellery.Dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    CategoryRepository categoryRepository;

    // Post Product Function
    public void addProduct(Product product) {
        // Ensure bidirectional consistency
        if (product.getImages() != null) {
            for (ProductImage image : product.getImages()) {
                image.setProduct(product);
            }
        }
        productRepository.save(product);
    }


    // Get Product Function
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        // 1) Fetch products with pagination
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> products = productPage.getContent();

        // 2) Collect product IDs
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3) Fetch all related images in one query & group by productId
        final Map<Long, List<String>> imagesByProduct =
                productIds.isEmpty()
                        ? Collections.emptyMap()
                        : productImageRepository.findAllByProduct_IdIn(productIds).stream()
                        .collect(Collectors.groupingBy(
                                img -> img.getProduct().getId(),
                                Collectors.mapping(ProductImage::getImageUrl, Collectors.toList())
                        ));

        // 4) Convert each Product -> ProductDTO
        List<ProductDTO> dtos = products.stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getName(),
                        p.getAverageRating(),
                        p.getRatingCount(),
                        p.getPrice(),
                        p.getDescription(),
                        p.getStock(),
                        p.getCategory() != null ? p.getCategory().getName() : null,
                        imagesByProduct.getOrDefault(p.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());

        // 5) Return a new Page<ProductDTO> with pagination info
        return new PageImpl<>(dtos, pageable, productPage.getTotalElements());
    }

    // Get Product by id
    public ProductDTO getProductById(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getAverageRating(),
                product.getRatingCount(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                product.getCategory().getName(),
                productImageRepository.findByProductId(id).stream()
                        .map(ProductImage::getImageUrl) // assuming field is `image` or `url`
                        .toList()
        );


    }
    // Delete Product By id
    public void deleteProductById(Long id){
        productImageRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }
    //Update Product By id
    @Transactional
    public Product updateProduct(Long id, ProductDTO dto) {
        // 1) find existing
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // 2) map DTO -> a temp entity that has basic fields and resolved Category
        Product mapped = productMapper.toEntity(dto);
        mapped.setId(id); // ensure ID is set (mapper may set id from dto or not)

        // 3) copy basic fields from mapped to existing (no images here)
        existing.setName(mapped.getName());
        existing.setPrice(mapped.getPrice());
        existing.setDescription(mapped.getDescription());
        existing.setStock(mapped.getStock());
        existing.setCategory(mapped.getCategory());

        // 4) replace images: delete old ones, then save new ones from DTO
        productImageRepository.deleteByProductId(existing.getId());

        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            for (String url : dto.getImageUrls()) {
                ProductImage img = new ProductImage();
                img.setImageUrl(url);        // or setImage() depending on your field name
                img.setProduct(existing);    // link child -> parent
                productImageRepository.save(img);
            }
        }

        // 5) save and return updated product
        return productRepository.save(existing);
    }
    // filters for products
    public Page<ProductDTO> getFilteredProducts(
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            String search,
            Pageable pageable
    ) {
        // Treat empty search string as null
        if (search != null && search.isBlank()) {
            search = null;
        }

        Page<Product> productPage;

        // If no filters applied, fetch all products
        if (categoryId == null && minPrice == null && maxPrice == null && search == null) {
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository.findFilteredProducts(categoryId, minPrice, maxPrice, search, pageable);
        }

        List<Product> products = productPage.getContent();

        // Batch load images
        List<Long> productIds = products.stream().map(Product::getId).toList();
        final Map<Long, List<String>> imagesByProduct = productIds.isEmpty()
                ? Collections.emptyMap()
                : productImageRepository.findAllByProduct_IdIn(productIds)
                .stream()
                .collect(Collectors.groupingBy(
                        img -> img.getProduct().getId(),
                        Collectors.mapping(ProductImage::getImageUrl, Collectors.toList())
                ));

        // Map Product entities → ProductDTOs
        List<ProductDTO> dtos = products.stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getName(),
                        p.getAverageRating(),
                        p.getRatingCount(),
                        p.getPrice(),
                        p.getDescription(),
                        p.getStock(),
                        p.getCategory() != null ? p.getCategory().getName() : null,
                        imagesByProduct.getOrDefault(p.getId(), Collections.emptyList())
                ))
                .toList();

        return new PageImpl<>(dtos, pageable, productPage.getTotalElements());
    }

    //Featured Products
    public List<Product> getFeaturedProducts(Pageable pageable) {
        return productRepository.findByFeaturedTrue(pageable);
    }
    // get 5 products by each id
    public Map<Category, List<Product>> getCategoryProducts() {
        Map<Category, List<Product>> result = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            List<Product> products = productRepository.findTop5ByCategoryId(category.getId());
            result.put(category, products);
        }
        return result;
    }


}
