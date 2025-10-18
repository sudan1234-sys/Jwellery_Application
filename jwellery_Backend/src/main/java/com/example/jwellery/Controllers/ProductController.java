package com.example.jwellery.Controllers;
import com.example.jwellery.Dto.ProductDTO;
import com.example.jwellery.Entity.Category;
import com.example.jwellery.Entity.Product;
import com.example.jwellery.Entity.Product_Image;
import com.example.jwellery.Mapper.ProductMapper;
import com.example.jwellery.Services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductMapper productMapper;
    @PostMapping("/addProducts")
    ResponseEntity<String> addProducts(@Valid @RequestBody Product product){
        productService.addProduct(product);
        return ResponseEntity.ok("Product Added successfully");
    }
    @GetMapping("/allProducts")
    public ResponseEntity<Page<ProductDTO>> getProducts(Pageable pageable) {
        Page<ProductDTO> page = productService.getAllProducts(pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductsById(@PathVariable long id){
        ProductDTO productdto=productService.getProductById(id);
        return ResponseEntity.ok(productdto);
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProductsById(@PathVariable long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product Deleted Successfully");
    }
    @PutMapping("/updateProducts/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {

        Product updated = productService.updateProduct(id, productDTO);          // service takes DTO
        ProductDTO response = productMapper.toDto(updated);                     // reuse mapper to create response
        return ResponseEntity.ok(response);
    }
    @GetMapping("/products/filter")
    public ResponseEntity<Page<ProductDTO>> getFilteredProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Treat empty search string as null
        if (search != null && search.isBlank()) {
            search = null;
        }

        Page<ProductDTO> products;

        boolean isFilterApplied = categoryId != null || minPrice != null || maxPrice != null || search != null;

        if (isFilterApplied) {
            products = productService.getFilteredProducts(categoryId, minPrice, maxPrice, search, pageable);
        } else {
            products = productService.getAllProducts(pageable);
        }

        return ResponseEntity.ok(products);
    }


    @GetMapping("/products/featured")
    public ResponseEntity<List<ProductDTO>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<ProductDTO> dtos = productService.getFeaturedProducts(pageable)
                .stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getAverageRating(),       // double
                        product.getRatingCount(),        // int
                        product.getPrice(),              // Double
                        product.getDescription(),        // String
                        product.getStock(),              // long
                        product.getCategory().getName(), // String
                        product.getImages().stream()
                                .map(Product_Image::getImageUrl)
                                .toList()                  // List<String>
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/getProductbycategory")
    public ResponseEntity<List<Map<String, Object>>> getCategoryProducts() {
        Map<Category, List<Product>> rawResult = productService.getCategoryProducts();

        List<Map<String, Object>> response = rawResult.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> categoryBlock = new HashMap<>();
                    categoryBlock.put("category", entry.getKey().getName());
                    categoryBlock.put("products", entry.getValue().stream()
                            .map(productMapper::toDto)
                            .toList());
                    return categoryBlock;
                })
                .toList();

        return ResponseEntity.ok(response);
    }








}
