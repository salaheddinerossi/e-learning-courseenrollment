package com.example.enrollingservice.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isCompleted=false;

    private Long currentLessonId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @OneToMany(mappedBy = "courseEnrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentLesson> studentLessons = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review;

}
