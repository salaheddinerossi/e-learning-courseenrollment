package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentLessonRepository extends JpaRepository<StudentLesson,Long> {
}
