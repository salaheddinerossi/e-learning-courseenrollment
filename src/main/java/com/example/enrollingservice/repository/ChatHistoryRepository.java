package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory,Long> {
    List<ChatHistory> findByStudentLesson(StudentLesson studentLesson);
}
