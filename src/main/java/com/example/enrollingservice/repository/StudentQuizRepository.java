package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.model.StudentQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentQuizRepository extends JpaRepository<StudentQuiz,Long> {
    List<StudentQuiz> findByStudentLesson(StudentLesson studentLesson);
}
