

package com.example.enrollingservice.ServiceTest;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.enrollingservice.mapper.ChatHistoryMapper;
import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.ChatRecord;
import com.example.enrollingservice.model.Lesson;
import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.repository.ChatHistoryRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.response.ChatHistoryResponse;
import com.example.enrollingservice.response.ChatResponse;
import com.example.enrollingservice.serviceImpl.ChatHistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatHistoryServiceTest {

    @Mock
    ChatHistoryRepository chatHistoryRepository;

    @Mock
    private ChatHistoryMapper chatHistoryMapper;

    @Mock
    private StudentLessonRepository studentLessonRepository;


    @InjectMocks
    private ChatHistoryServiceImpl chatHistoryService;



    @Test
    void testGetChatSuccess(){
        Long chatId = 1L;

        ChatHistory mockChatHistory = new ChatHistory();

        ChatRecord chatRecord = new ChatRecord();

        chatRecord.setContent("hello1");
        ChatRecord chatRecord1 = new ChatRecord();

        chatRecord1.setContent("hello2");

        List<ChatRecord> chatRecords = new ArrayList<>();

        chatRecords.add(chatRecord1);
        chatRecords.add(chatRecord);

        mockChatHistory.setId(chatId);
        mockChatHistory.setChatRecords(chatRecords);

        List<ChatHistoryResponse> mockChatHistoryResponses  = new ArrayList<>();

        ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse();
        chatHistoryResponse.setContent("hello1");

        ChatHistoryResponse chatHistoryResponse1 = new ChatHistoryResponse();
        chatHistoryResponse.setContent("hello2");


        mockChatHistoryResponses.add(chatHistoryResponse);
        mockChatHistoryResponses.add(chatHistoryResponse1);

        ChatResponse mockChatResponse = new ChatResponse();

        mockChatResponse.setId(chatId);
        mockChatResponse.setChatHistoryResponses(mockChatHistoryResponses);



        when(chatHistoryRepository.findById(chatId)).thenReturn(Optional.of(mockChatHistory));
        when(chatHistoryMapper.chatRecordListToChatHistoryResponseList(mockChatHistory.getChatRecords()))
                .thenReturn(mockChatHistoryResponses);

        ChatResponse actualResponse = chatHistoryService.getChat(chatId);

        assertEquals(mockChatResponse.getId(), actualResponse.getId());
        assertEquals(mockChatResponse.getChatHistoryResponses(), actualResponse.getChatHistoryResponses());

    }

    @Test
    void testDeleteRecords (){

        Long chatHistoryId = 1L;

        ChatHistory mockChatHistory = new ChatHistory() ;

        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setContent("hello1");

        ChatRecord chatRecord1 = new ChatRecord();
        chatRecord1.setContent("hello2");

        mockChatHistory.getChatRecords().add(chatRecord);
        mockChatHistory.getChatRecords().add(chatRecord1);

        when(chatHistoryRepository.findById(chatHistoryId)).thenReturn(Optional.of(mockChatHistory));

        chatHistoryService.deleteRecords(chatHistoryId);

        assertThat(mockChatHistory.getChatRecords()).isEmpty();

    }


    @Test
    void testCreateChat(){

        Long studentLessonId = 1L;

        Lesson mockLesson = new Lesson();

        mockLesson.setTranscribe("test");



        StudentLesson mockStudentLesson = new StudentLesson();
        mockStudentLesson.setId(studentLessonId);
        mockStudentLesson.setLesson(mockLesson);

        when(studentLessonRepository.findById(studentLessonId)).thenReturn(Optional.of(mockStudentLesson));
        when(chatHistoryRepository.findByStudentLesson(mockStudentLesson)).thenReturn(Optional.empty());

        chatHistoryService.createChat(studentLessonId);

        verify(chatHistoryRepository, times(1)).save(any(ChatHistory.class));

    }



}
