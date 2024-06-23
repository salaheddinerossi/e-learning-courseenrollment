package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByCourseEnrollmentCourseId(Long courseId);
}
