package com.example.enrollingservice.service;

import com.example.enrollingservice.dto.QuestionDto;
import com.example.enrollingservice.response.ChatResponse;

public interface ChatHistoryService {

    //get a chat records
    ChatResponse getChat(Long chatId);

    //ask chat bot
    String askChatBot(Long chatBotId, QuestionDto question);

    void createChat(Long studentLessonID);

    void deleteRecords(Long chatId);

    //reset chat

}
