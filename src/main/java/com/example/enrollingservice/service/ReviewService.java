package com.example.enrollingservice.service;

import com.example.enrollingservice.dto.ReviewDto;
import com.example.enrollingservice.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(Long courseEnrollmentId, ReviewDto reviewDto);

    List<ReviewResponse> getReviews(Long courseId);


}
