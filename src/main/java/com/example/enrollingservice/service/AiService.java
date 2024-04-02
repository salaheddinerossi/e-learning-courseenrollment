package com.example.enrollingservice.service;

import com.example.enrollingservice.dto.MessageDto;
import com.example.enrollingservice.dto.QuizzCorrection.ExplanatoryQuestionsDto;
import com.example.enrollingservice.dto.QuizzCorrection.MultipleChoiceQuestionsDto;
import com.example.enrollingservice.dto.QuizzCorrection.TrueFalseQuestionsDto;
import com.example.enrollingservice.response.ExplanatoryQuestionsCorrectionResponse;
import com.example.enrollingservice.response.CorrectionResponse;

import java.util.List;

public interface AiService {

    String askChatBot(List<MessageDto> messageDtoList);

    //correct quiz
    CorrectionResponse correctMultipleChoiceQuiz(MultipleChoiceQuestionsDto multipleChoiceQuestionsDto);

    CorrectionResponse correctTrueFalseQuiz(TrueFalseQuestionsDto trueFalseQuestionsDto);
    ExplanatoryQuestionsCorrectionResponse correctExplanatoryQuiz(ExplanatoryQuestionsDto explanatoryQuestionsDto);

}
