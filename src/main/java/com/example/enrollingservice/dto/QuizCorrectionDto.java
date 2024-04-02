package com.example.enrollingservice.dto;

import com.example.enrollingservice.Enums.QuizType;
import lombok.Data;

import java.util.List;


@Data
public class QuizCorrectionDto {

    private  Long id;

    private QuizType quizType;

    List<AnswerDto> answerDtos;

}
