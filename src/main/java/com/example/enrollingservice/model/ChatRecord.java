package com.example.enrollingservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ChatRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name="content", length=100000)
    private String content;

    private Boolean fromAssistant;

    @ManyToOne
    @JoinColumn(name = "chat_history_id")
    private ChatHistory chatHistory;
}
