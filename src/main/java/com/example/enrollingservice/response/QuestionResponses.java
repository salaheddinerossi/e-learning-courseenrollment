package com.example.enrollingservice.response;

import lombok.Data;

import java.util.List;


@Data
public class QuestionResponses {
    private Long id;

    private String question;


    private List<String> options;

}
