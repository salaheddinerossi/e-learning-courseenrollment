package com.example.enrollingservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private Integer review;

    @OneToOne(mappedBy = "review")
    private CourseEnrollment courseEnrollment;


}
