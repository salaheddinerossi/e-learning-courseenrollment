package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.ReviewDto;
import com.example.enrollingservice.exception.BadRequestException;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.mapper.ReviewMapper;
import com.example.enrollingservice.model.CourseEnrollment;
import com.example.enrollingservice.model.Review;
import com.example.enrollingservice.repository.CourseEnrollmentRepository;
import com.example.enrollingservice.repository.ReviewRepository;
import com.example.enrollingservice.response.ReviewResponse;
import com.example.enrollingservice.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReviewServiceImpl implements ReviewService {

    final
    ReviewRepository reviewRepository;

    final
    ReviewMapper reviewMapper;

    final
    CourseEnrollmentRepository courseEnrollmentRepository;

    public ReviewServiceImpl(CourseEnrollmentRepository courseEnrollmentRepository, ReviewMapper reviewMapper, ReviewRepository reviewRepository) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
    }


    @Override
    public ReviewResponse createReview(Long courseEnrollmentId, ReviewDto reviewDto) {
        CourseEnrollment courseEnrollment = findCourseEnrollmentById(courseEnrollmentId);
        if (!courseEnrollment.getIsCompleted()) {
            throw  new BadRequestException("you need to complete the course first to add a review");
        }

        Review review = reviewMapper.reviewDtoToReview(reviewDto);
        review.setCourseEnrollment(courseEnrollment);

        reviewRepository.save(review);

        return reviewMapper.reviewToReviewResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviews(Long courseId) {
        return reviewMapper.reviewListToReviewResponseList(reviewRepository.findByCourseEnrollmentCourseId(courseId));
    }

    private CourseEnrollment findCourseEnrollmentById(Long id){
        return courseEnrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("couse enrollment not found with this id: "+id)
        );
    }
}
