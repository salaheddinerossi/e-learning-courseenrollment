package com.example.enrollingservice.controller;


import com.example.enrollingservice.dto.ReviewDto;
import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.UnauthorizedException;
import com.example.enrollingservice.response.ReviewResponse;
import com.example.enrollingservice.service.AuthService;
import com.example.enrollingservice.service.ReviewService;
import com.example.enrollingservice.service.StudentService;
import com.example.enrollingservice.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Value("${auth.url}")
    private String authUrl;

    final
    AuthService authService;

    final
    StudentService studentService;

    final
    ReviewService reviewService;

    public ReviewController(AuthService authService, StudentService studentService, ReviewService reviewService) {
        this.authService = authService;
        this.studentService = studentService;
        this.reviewService = reviewService;
    }

    @PostMapping("/{courseEnrollmentId}")
    ResponseEntity<ApiResponse<ReviewResponse>> createReview(@RequestBody ReviewDto reviewDto, @PathVariable Long courseEnrollmentId,@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);
        if (!studentService.studentHasEnrollment(userDetailsDto.getEmail(), courseEnrollmentId)){
            throw  new UnauthorizedException("you are not the owner of this course enrollment");
        }

        ReviewResponse reviewResponse = reviewService.createReview(courseEnrollmentId,reviewDto);
        return ResponseEntity.ok(new ApiResponse<>(true,"review has been created",reviewResponse));
    }

    @GetMapping("/{courseId}")
    ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(@PathVariable Long courseId){

        List<ReviewResponse> reviewResponses = reviewService.getReviews(courseId);
        return ResponseEntity.ok(new ApiResponse<>(true,"review has been fetched", reviewResponses));
    }
}
