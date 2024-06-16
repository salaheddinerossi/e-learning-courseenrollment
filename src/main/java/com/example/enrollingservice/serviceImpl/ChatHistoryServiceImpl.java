package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.MessageDto;
import com.example.enrollingservice.dto.QuestionDto;
import com.example.enrollingservice.exception.BadRequestException;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.mapper.ChatHistoryMapper;
import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.ChatRecord;
import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.repository.ChatHistoryRepository;
import com.example.enrollingservice.repository.ChatRecordRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.response.ChatResponse;
import com.example.enrollingservice.service.AiService;
import com.example.enrollingservice.service.ChatHistoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    final
    ChatHistoryRepository chatHistoryRepository;

    final
    StudentLessonRepository studentLessonRepository;

    final
    AiService aiService;

    final
    ChatRecordRepository chatRecordRepository;

    final
    ChatHistoryMapper chatHistoryMapper;

    @Value("${instructions.system}")
    private String instructions;

    public ChatHistoryServiceImpl(ChatHistoryRepository chatHistoryRepository, StudentLessonRepository studentLessonRepository, AiService aiService, ChatRecordRepository chatRecordRepository, ChatHistoryMapper chatHistoryMapper) {
        this.chatHistoryRepository = chatHistoryRepository;
        this.studentLessonRepository = studentLessonRepository;
        this.aiService = aiService;
        this.chatRecordRepository = chatRecordRepository;
        this.chatHistoryMapper = chatHistoryMapper;
    }


    @Override
    public ChatResponse getChat(Long chatId) {
        ChatHistory chatHistory = findChatHistoryById(chatId);

        ChatResponse chatResponse = new ChatResponse();

        chatResponse.setId(chatHistory.getId());
        chatResponse.setChatHistoryResponses(chatHistoryMapper.chatRecordListToChatHistoryResponseList(chatHistory.getChatRecords()));

        return chatResponse;
    }

    @Override
    @Transactional
    public String askChatBot(Long studentLessonId, QuestionDto question) {

        ChatHistory chatHistory = chatHistoryRepository.findByStudentLessonId(studentLessonId);

        List<MessageDto> messageDtos = new ArrayList<>();

        messageDtos.add(new MessageDto("system",chatHistory.getSystemMessage()));
        messageDtos.add(new MessageDto("system",chatHistory.getStudentLesson().getLesson().getTranscribe()));
        for (ChatRecord chatRecord:chatHistory.getChatRecords()){

            String role;
            if (chatRecord.getFromAssistant()){
                role="assistant";
            }else {
                role="user";
            }
            messageDtos.add(new MessageDto(role,chatRecord.getContent()));
        }

        messageDtos.add(new MessageDto("user",question.getQuestion()));

        String reply = aiService.askChatBot(messageDtos);

        ChatRecord chatRecord =new ChatRecord();

        chatRecord.setChatHistory(chatHistory);

        chatRecord.setContent(question.getQuestion());
        chatRecord.setFromAssistant(false);
        chatRecordRepository.save(chatRecord);

        ChatRecord chatRecord1 = new ChatRecord();

        chatRecord1.setChatHistory(chatHistory);

        chatRecord1.setContent(reply);
        chatRecord1.setFromAssistant(true);
        chatRecordRepository.save(chatRecord1);

        return reply;
    }

    @Override
    @Transactional
    public void createChat(Long studentLessonId) {
        ChatHistory chatHistory = new ChatHistory();

        StudentLesson studentLesson = findStudentLessonByID(studentLessonId);


        if (chatHistoryRepository.findByStudentLesson(studentLesson).isPresent()){
            throw new BadRequestException("Chat history already created for student lesson with the id: " + studentLessonId);
        }

        chatHistory.setStudentLesson(studentLesson);

        chatHistory.setSystemMessage(instructions);
        chatHistory.setTranscribe(studentLesson.getLesson().getTranscribe());

        chatHistoryRepository.save(chatHistory);

    }

    @Override
    @Transactional
    public void deleteRecords(Long chatId) {
        ChatHistory chatHistory = findChatHistoryById(chatId);
        chatHistory.getChatRecords().clear();
        chatHistoryRepository.save(chatHistory);
    }

    ChatHistory findChatHistoryById(Long id){
        return chatHistoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no chat history found with this id")
        );
    }

    StudentLesson findStudentLessonByID(Long id){
        return studentLessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no student lesson  found with this id")
        );
    }
}
