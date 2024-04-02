package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.Quizzes.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
