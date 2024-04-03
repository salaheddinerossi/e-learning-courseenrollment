package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.Chapter;
import com.example.enrollingservice.model.CourseEnrollment;
import com.example.enrollingservice.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentLessonRepository extends JpaRepository<StudentLesson,Long> {
    List<StudentLesson> findByLessonChapterAndCourseEnrollment(Chapter chapter, CourseEnrollment courseEnrollment);
}
