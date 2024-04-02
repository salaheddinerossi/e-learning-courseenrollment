package com.example.enrollingservice.service;

import com.example.enrollingservice.dto.QuizCorrectionDto;
import com.example.enrollingservice.response.QuizCorrectionResponse;

public interface StudentQuizService {

    QuizCorrectionResponse correctQuiz(QuizCorrectionDto quizCorrectionDto ,String email);


}
