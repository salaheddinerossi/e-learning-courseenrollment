package com.example.enrollingservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name="systemMessage", length=100000)
    private String systemMessage;

    @Lob
    @Column(name="transcribe", length=100000)
    private String transcribe;

    @OneToMany(mappedBy = "chatHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRecord> chatRecords = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "student_lesson_id", referencedColumnName = "id")
    private StudentLesson studentLesson;
}
