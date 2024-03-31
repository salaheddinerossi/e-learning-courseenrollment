package com.example.enrollingservice.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseEnrollment> courseEnrollments = new ArrayList<>();


}

