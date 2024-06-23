package com.example.enrollingservice.dto.QuizzCorrection;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrueFalseQuestionsDto {

    String transcribe;

    List<BooleanQuestionDto> booleanQuestionDtos = new ArrayList<>();
}
