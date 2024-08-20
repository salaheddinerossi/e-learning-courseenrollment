package com.example.enrollingservice.model;


import com.example.enrollingservice.Enums.CourseLevel;
import com.example.enrollingservice.Enums.CourseStatus;
import com.example.enrollingservice.Enums.Language;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String title;

    @Lob
    @Column(name="about", length=100000)
    private String about;


    @Lob
    @Column(name="requirements", length=100000)
    private String requirements;

    private Language languageEnum;
    private CourseLevel courseLevelEnum;

    private CourseStatus courseStatusEnum;

    private LocalDate date;



    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseEnrollment> courseEnrollments = new ArrayList<>();



    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
