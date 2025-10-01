package com.example.jwellery.Services;

import com.example.jwellery.Dto.ReviewDto;
import com.example.jwellery.Entity.Reviews;
import com.example.jwellery.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    // Method to Add Reviews For Products
    public void addReviews(Reviews review){
        reviewRepository.save(review);
    }

    // Get All Riviews By Product Id
    public List<ReviewDto> getReviewsById(Long productId){
        List<Reviews> reviews = reviewRepository.findAllByProductIdWithUser(productId);

        // Map Reviews → ReviewDTO
        return reviews.stream()
                .map(r -> new ReviewDto(r.getReview(), r.getUser().getName()))
                .toList(); // or .collect(Collectors.toList()) for older Java
    }


}
