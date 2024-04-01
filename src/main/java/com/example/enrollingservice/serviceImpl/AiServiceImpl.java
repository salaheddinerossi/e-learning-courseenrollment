package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.ChatRequestDto;
import com.example.enrollingservice.dto.MessageDto;
import com.example.enrollingservice.response.ChatResponseDTO;
import com.example.enrollingservice.service.AiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    WebClient webClient;

    public AiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void initialize() {
        this.webClient = this.webClient.mutate().baseUrl(aiServiceUrl).build();
    }
    @Override
    public String askChatBot(List<MessageDto> messageDtoList) {

        ChatRequestDto requestDTO = new ChatRequestDto(messageDtoList);
        ChatResponseDTO responseDTO = webClient.post()
                .uri("chat/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestDTO), ChatRequestDto.class)
                .retrieve()
                .bodyToMono(ChatResponseDTO.class)
                .block();

        return responseDTO != null ? responseDTO.getReply() : null;
    }
}
