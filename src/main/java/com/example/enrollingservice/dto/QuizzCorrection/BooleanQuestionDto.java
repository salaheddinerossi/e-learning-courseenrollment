package com.example.enrollingservice.dto.QuizzCorrection;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@AllArgsConstructor
public class BooleanQuestionDto {

    private Long id;

    private String question;

    private Boolean answer;

    private Boolean correctAnswer;
}
