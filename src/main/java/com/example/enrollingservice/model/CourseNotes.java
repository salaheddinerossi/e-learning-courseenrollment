package com.example.enrollingservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CourseNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String record;

    @ManyToOne
    @JoinColumn(name = "student_lesson_id")
    StudentLesson studentLesson;


}
