package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.model.StudentQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentQuizRepository extends JpaRepository<StudentQuiz,Long> {
    List<StudentQuiz> findByStudentLesson(StudentLesson studentLesson);

    Optional<StudentQuiz> findByStudentLessonCourseEnrollmentStudentEmailAndQuizId(String email, Long quizId);

    List<StudentQuiz> findByStudentLessonId(Long id);
}


