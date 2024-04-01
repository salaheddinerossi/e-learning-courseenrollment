package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.ChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRecordRepository extends JpaRepository<ChatRecord,Long> {
}
