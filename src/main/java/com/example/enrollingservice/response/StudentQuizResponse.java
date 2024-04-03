package com.example.enrollingservice.response;

import lombok.Data;

import java.util.List;


@Data
public class StudentQuizResponse {

    private Long id;

    private Long quiz_id;

    private Boolean isPassed;

    private Double mark;

    private List<String> advices;

}
