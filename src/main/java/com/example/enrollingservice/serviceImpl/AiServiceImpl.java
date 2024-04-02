package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.ChatRequestDto;
import com.example.enrollingservice.dto.MessageDto;
import com.example.enrollingservice.dto.QuizzCorrection.ExplanatoryQuestionsDto;
import com.example.enrollingservice.dto.QuizzCorrection.MultipleChoiceQuestionsDto;
import com.example.enrollingservice.dto.QuizzCorrection.TrueFalseQuestionsDto;
import com.example.enrollingservice.response.ChatResponseDTO;
import com.example.enrollingservice.response.CorrectionResponse;
import com.example.enrollingservice.response.ExplanatoryQuestionsCorrectionResponse;
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

    @Override
    public CorrectionResponse correctMultipleChoiceQuiz(MultipleChoiceQuestionsDto multipleChoiceQuestionsDto) {



        return webClient.post()
                .uri("correction/multiple-choice")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(multipleChoiceQuestionsDto), MultipleChoiceQuestionsDto.class)
                .retrieve()
                .bodyToMono(CorrectionResponse.class)
                .block();
    }

    @Override
    public CorrectionResponse correctTrueFalseQuiz(TrueFalseQuestionsDto trueFalseQuestionsDto) {
        return webClient.post()
                .uri("correction/true-false")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(trueFalseQuestionsDto), TrueFalseQuestionsDto.class)
                .retrieve()
                .bodyToMono(CorrectionResponse.class)
                .block();
    }

    @Override
    public ExplanatoryQuestionsCorrectionResponse correctExplanatoryQuiz(ExplanatoryQuestionsDto explanatoryQuestionsDto) {
        return webClient.post()
                .uri("correction/explanatory")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(explanatoryQuestionsDto), ExplanatoryQuestionsDto.class)
                .retrieve()
                .bodyToMono(ExplanatoryQuestionsCorrectionResponse.class)
                .block();
    }
}
