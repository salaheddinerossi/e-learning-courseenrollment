package com.example.enrollingservice.response;


import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private Long id;

    private List<ChatHistoryResponse> chatHistoryResponses;
}
