package com.example.enrollingservice.model;


import com.example.enrollingservice.Enums.StudentLessonStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class StudentLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private StudentLessonStatus studentLessonStatus;

    @ManyToOne
    @JoinColumn(name = "course_enrollement_id")
    CourseEnrollment courseEnrollment;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    Lesson lesson;



    @OneToMany(mappedBy = "studentLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseNotes> courseNotes = new ArrayList<>();

    @OneToOne(mappedBy = "studentLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatHistory chatHistory;

    @OneToMany(mappedBy = "studentLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentQuiz> studentQuizzes = new ArrayList<>();

}
