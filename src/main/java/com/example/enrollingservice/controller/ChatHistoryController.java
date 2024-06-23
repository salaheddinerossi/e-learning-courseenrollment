package com.example.enrollingservice.controller;


import com.example.enrollingservice.dto.QuestionDto;
import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.UnauthorizedException;
import com.example.enrollingservice.response.ChatHistoryResponse;
import com.example.enrollingservice.response.ChatResponse;
import com.example.enrollingservice.response.ChatResponseDTO;
import com.example.enrollingservice.service.AuthService;
import com.example.enrollingservice.service.ChatHistoryService;
import com.example.enrollingservice.service.StudentService;
import com.example.enrollingservice.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatHistoryController {

    @Value("${auth.url}")
    private String authUrl;


    final
    AuthService authService;

    final
    ChatHistoryService chatHistoryService;

    final
    StudentService studentService;

    public ChatHistoryController(AuthService authService, ChatHistoryService chatHistoryService, StudentService studentService) {
        this.authService = authService;
        this.chatHistoryService = chatHistoryService;
        this.studentService = studentService;
    }


    @PostMapping("/{studentLessonId}")
    ResponseEntity<ApiResponse<?>> createChatHistory(@PathVariable long studentLessonId, @RequestHeader("Authorization") String token){

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasStudentLesson(userDetailsDto.getEmail(),studentLessonId)){
            throw new UnauthorizedException("you are not the owner of this student lesson");
        }

        chatHistoryService.createChat(studentLessonId);
        return ResponseEntity.ok(new ApiResponse<>(true,"chat has been created",null));
    }

    @PostMapping("/question/{studentLessonId}")
    ResponseEntity<ApiResponse<String>> askChatBot(@RequestBody QuestionDto questionDto,@PathVariable long studentLessonId, @RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasStudentLesson(userDetailsDto.getEmail(),studentLessonId)){
            throw new UnauthorizedException("you are not the owner of this chat");
        }

        String reply  = chatHistoryService.askChatBot(studentLessonId,questionDto);
        return ResponseEntity.ok(new ApiResponse<>(true,"chat has been created",reply));
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ChatResponse>> getChat(@PathVariable long id, @RequestHeader("Authorization") String token) {

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasStudentChat(userDetailsDto.getEmail(),id)){
            throw new UnauthorizedException("you are not the owner of this chat");
        }

        ChatResponse chatResponse = chatHistoryService.getChat(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"chat has been fetched successfully",chatResponse));

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<ChatResponse>> deleteRecords(@PathVariable long id, @RequestHeader("Authorization") String token) {

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasStudentChat(userDetailsDto.getEmail(),id)){
            throw new UnauthorizedException("you are not the owner of this chat");
        }

        chatHistoryService.deleteRecords(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"chat records have been deleted",null));

    }

}
