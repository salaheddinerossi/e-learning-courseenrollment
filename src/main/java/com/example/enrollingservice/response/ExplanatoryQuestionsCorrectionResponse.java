package com.example.enrollingservice.response;

import lombok.Data;

import java.util.List;

@Data
public class ExplanatoryQuestionsCorrectionResponse {

    private List<String> advices;

    private double mark;
}
