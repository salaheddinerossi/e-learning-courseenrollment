package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.StudentQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentQuizRepository extends JpaRepository<StudentQuiz,Long> {
}
