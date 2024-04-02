package com.example.enrollingservice.response;


import lombok.Data;

import java.util.List;

@Data
public class QuizCorrectionResponse {

    private long id;

    private Boolean isPassed;

    private Double mark;

    private List<String> advices;

}
