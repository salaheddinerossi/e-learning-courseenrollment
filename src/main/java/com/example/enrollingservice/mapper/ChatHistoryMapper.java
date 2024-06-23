package com.example.enrollingservice.mapper;

import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.ChatRecord;
import com.example.enrollingservice.response.ChatHistoryResponse;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatHistoryMapper {

    ChatHistoryResponse chatRecordToChatHistoryResponse(ChatRecord chatRecord);

    List<ChatHistoryResponse> chatRecordListToChatHistoryResponseList(List<ChatRecord> chatRecords);
}
