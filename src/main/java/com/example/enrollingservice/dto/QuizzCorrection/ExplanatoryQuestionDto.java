package com.example.enrollingservice.dto.QuizzCorrection;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExplanatoryQuestionDto {

    private Long id;

    private String question;


    private String answer;

    private String correctAnswer;
}
