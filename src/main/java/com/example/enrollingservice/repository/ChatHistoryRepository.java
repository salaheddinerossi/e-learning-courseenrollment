package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory,Long> {
    Optional<ChatHistory> findByStudentLesson(StudentLesson studentLesson);

}
