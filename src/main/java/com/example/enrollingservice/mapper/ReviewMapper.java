package com.example.enrollingservice.mapper;


import com.example.enrollingservice.dto.ReviewDto;
import com.example.enrollingservice.model.Review;
import com.example.enrollingservice.response.ReviewResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ReviewMapper {

    ReviewResponse reviewToReviewResponse(Review review);

    Review reviewDtoToReview(ReviewDto reviewDto);

    List<ReviewResponse> reviewListToReviewResponseList(List<Review> reviewList);
}
