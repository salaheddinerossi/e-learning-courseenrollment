package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.CourseNotes;
import com.example.enrollingservice.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseNotesRepository extends JpaRepository<CourseNotes,Long> {
    List<CourseNotes> findByStudentLesson(StudentLesson studentLesson);
}
