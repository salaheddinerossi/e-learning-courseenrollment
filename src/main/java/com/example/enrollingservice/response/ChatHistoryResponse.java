package com.example.enrollingservice.response;


import lombok.Data;

@Data
public class ChatHistoryResponse {
    private Long id;

    private String record;

    private Boolean fromStudent;

}
