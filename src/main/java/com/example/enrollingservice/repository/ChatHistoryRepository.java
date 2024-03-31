package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory,Long> {
}
