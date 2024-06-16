package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByCourseEnrollmentsStudentEmail(String email);
}
