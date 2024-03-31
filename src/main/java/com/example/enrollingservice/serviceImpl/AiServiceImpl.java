package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiServiceImpl implements AiService {

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    WebClient webClient;

    public AiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

}
