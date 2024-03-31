package com.example.enrollingservice.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseEnrollment> courseEnrollments = new ArrayList<>();

}
