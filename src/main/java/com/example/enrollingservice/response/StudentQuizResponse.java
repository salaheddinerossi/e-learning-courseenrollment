package com.example.enrollingservice.response;

import lombok.Data;

import java.util.List;


@Data
public class StudentQuizResponse {

    private Long id;

    private Boolean isPassed;

    private List<String> advices;

}
