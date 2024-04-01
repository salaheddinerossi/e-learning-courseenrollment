package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.CourseEnrollment;
import com.example.enrollingservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment,Long> {
    Optional<CourseEnrollment> findByCourseIdAndStudent(Long courseId, Student student);
}
