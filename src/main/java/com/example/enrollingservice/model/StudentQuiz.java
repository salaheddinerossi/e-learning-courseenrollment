package com.example.enrollingservice.model;


import com.example.enrollingservice.util.StringListToJsonConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class StudentQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isPassed;

    @Convert(converter = StringListToJsonConverter.class)
    private List<String> advices;

    @ManyToOne
    @JoinColumn(name = "student_lesson_id")
    StudentLesson studentLesson;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    Quiz quiz;

}
