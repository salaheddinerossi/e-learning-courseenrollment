package com.example.enrollingservice.mapper;

import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.response.ChatHistoryResponse;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatHistoryMapper {

    ChatHistoryResponse chatHistoryToChatHistoryResponse(ChatHistory chatHistory);

    List<ChatHistoryResponse> chatHistoryListToChatHistoryResponseList(List<ChatHistory> chatHistory);
}
