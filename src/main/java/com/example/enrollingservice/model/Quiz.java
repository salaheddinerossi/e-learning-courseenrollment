package com.example.enrollingservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentQuiz> studentQuizzes = new ArrayList<>();


}
