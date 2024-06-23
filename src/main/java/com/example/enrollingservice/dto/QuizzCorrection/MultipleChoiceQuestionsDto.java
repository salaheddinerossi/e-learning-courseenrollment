package com.example.enrollingservice.dto.QuizzCorrection;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MultipleChoiceQuestionsDto {

    private String transcribe;

    private List<StringQuestionDto> stringQuestionDtos = new ArrayList<>();
}
