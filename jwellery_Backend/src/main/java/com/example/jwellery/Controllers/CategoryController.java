package com.example.jwellery.Controllers;

import com.example.jwellery.Entity.Category;
import com.example.jwellery.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/addCategory")
    public ResponseEntity<String>addCategories(@RequestBody Category category){
        categoryService.addCategory(category);
        return ResponseEntity.ok("Category Added Successfully");
    }
    @GetMapping("/AllCategories")
    public ResponseEntity<List<Category>> getAllCategory(){
       return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
