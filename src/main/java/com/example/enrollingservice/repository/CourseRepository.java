package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
