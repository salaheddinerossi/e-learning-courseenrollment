package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment,Long> {
}
