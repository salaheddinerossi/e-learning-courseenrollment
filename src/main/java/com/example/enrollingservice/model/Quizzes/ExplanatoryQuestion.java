package com.example.enrollingservice.model.Quizzes;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ExplanatoryQuestion extends Question {
    @Lob
    private String correctExplanation;

}