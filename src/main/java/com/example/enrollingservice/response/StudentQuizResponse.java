package com.example.enrollingservice.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class StudentQuizResponse {

    private Long id;

    private Long quiz_id;

    private Boolean isPassed;

    private List<QuestionResponses> questionResponsesList = new ArrayList<>();

    private String type;

    private Double mark;

    private List<String> advices;

}
