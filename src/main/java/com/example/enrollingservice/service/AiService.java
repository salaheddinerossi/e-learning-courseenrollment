package com.example.enrollingservice.service;

import com.example.enrollingservice.dto.MessageDto;

import java.util.List;

public interface AiService {

    String askChatBot(List<MessageDto> messageDtoList);

    //correct quiz

}
