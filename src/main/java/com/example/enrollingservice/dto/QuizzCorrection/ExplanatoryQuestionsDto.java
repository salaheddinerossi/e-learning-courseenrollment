package com.example.enrollingservice.dto.QuizzCorrection;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExplanatoryQuestionsDto {

    private String transcribe;

    private List<ExplanatoryQuestionDto> explanatoryQuestionDtos = new ArrayList<>();
}
