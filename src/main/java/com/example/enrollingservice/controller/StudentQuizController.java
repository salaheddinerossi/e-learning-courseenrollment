package com.example.enrollingservice.controller;


import com.example.enrollingservice.dto.QuizCorrectionDto;
import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.UnauthorizedException;
import com.example.enrollingservice.response.QuizCorrectionResponse;
import com.example.enrollingservice.service.AuthService;
import com.example.enrollingservice.service.StudentQuizService;
import com.example.enrollingservice.service.StudentService;
import com.example.enrollingservice.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/correct")
public class StudentQuizController {

    @Value("${auth.url}")
    private String authUrl;

    final
    AuthService authService;

    final
    StudentQuizService studentQuizService;

    final
    StudentService studentService;

    public StudentQuizController(AuthService authService, StudentQuizService studentQuizService, StudentService studentService) {
        this.authService = authService;
        this.studentQuizService = studentQuizService;
        this.studentService = studentService;
    }

    @PostMapping("/")
    ResponseEntity<ApiResponse<QuizCorrectionResponse>> correctQuiz(@RequestBody QuizCorrectionDto quizCorrectionDto, @RequestHeader("Authorization")String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isStudent(userDetailsDto)){
            throw new UnauthorizedException("you need to login first");
        }

        QuizCorrectionResponse quizCorrectionResponse = studentQuizService.correctQuiz(quizCorrectionDto, userDetailsDto.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true,"correction is done ",quizCorrectionResponse));
    }
}
