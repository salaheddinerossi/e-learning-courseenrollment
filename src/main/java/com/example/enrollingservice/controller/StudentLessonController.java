package com.example.enrollingservice.controller;


import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.UnauthorizedException;
import com.example.enrollingservice.response.StudentLessonResponse;
import com.example.enrollingservice.service.AuthService;
import com.example.enrollingservice.service.StudentLessonService;
import com.example.enrollingservice.service.StudentService;
import com.example.enrollingservice.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studentLesson")
public class StudentLessonController {

    @Value("${auth.url}")
    private String authUrl;

    final
    AuthService authService;

    final
    StudentService studentService;

    final
    StudentLessonService studentLessonService;

    public StudentLessonController(AuthService authService, StudentService studentService, StudentLessonService studentLessonService) {
        this.authService = authService;
        this.studentService = studentService;
        this.studentLessonService = studentLessonService;
    }

    @GetMapping("/{studentLessonId}")
    ResponseEntity<ApiResponse<StudentLessonResponse>> getStudentLesson(@PathVariable Long studentLessonId, @RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasStudentLesson(userDetailsDto.getEmail(),studentLessonId)){
            throw new UnauthorizedException("you are not the owner of this student lesson ");
        }

        return ResponseEntity.ok(new ApiResponse<>(true,"student lesson has been fetched successfully",studentLessonService.getStudentLesson(studentLessonId)));
    }

}
