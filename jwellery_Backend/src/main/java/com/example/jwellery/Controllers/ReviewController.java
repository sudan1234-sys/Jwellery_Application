package com.example.jwellery.Controllers;

import com.example.jwellery.Dto.ReviewDto;
import com.example.jwellery.Entity.Reviews;
import com.example.jwellery.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @PostMapping("/saveReviews")
    public ResponseEntity<String> addReviews(@RequestBody Reviews review){
        reviewService.addReviews(review);
        return ResponseEntity.ok("Review Added SuccessFully");
    }
    @GetMapping("/getReviewsById")
    public ResponseEntity<List<ReviewDto>> getReviewsById(@RequestParam Long productId){
        List<ReviewDto> list = reviewService.getReviewsById(productId);
        return ResponseEntity.ok(list);
    }


}
