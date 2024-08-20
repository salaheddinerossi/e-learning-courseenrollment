package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.CourseNotes;
import com.example.enrollingservice.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseNotesRepository extends JpaRepository<CourseNotes,Long> {
    List<CourseNotes> findByStudentLesson(StudentLesson studentLesson);

    Optional<CourseNotes> findByIdAndStudentLessonCourseEnrollmentStudentEmail(Long courseNoteId,String email);

    List<CourseNotes> findByStudentLessonId(Long studentLessonId);
}
