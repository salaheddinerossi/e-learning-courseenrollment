package com.example.enrollingservice.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User {
    private String phoneNumber;
    private Boolean isActive;
}
