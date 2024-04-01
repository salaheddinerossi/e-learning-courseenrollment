package com.example.enrollingservice.controller;

import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.UnauthorizedException;
import com.example.enrollingservice.response.CourseEnrollmentResponse;
import com.example.enrollingservice.service.AuthService;
import com.example.enrollingservice.service.CourseEnrollmentService;
import com.example.enrollingservice.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollment")
public class CourseEnrollmentController {

    @Value("${auth.url}")
    private String authUrl;


    final
    AuthService authService;

    final
    CourseEnrollmentService courseEnrollmentService;

    public CourseEnrollmentController(CourseEnrollmentService courseEnrollmentService, AuthService authService) {
        this.courseEnrollmentService = courseEnrollmentService;
        this.authService = authService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> enrollCourse(@PathVariable Long id,@RequestHeader("Authorization") String token){

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isStudent(userDetailsDto)){
            throw new UnauthorizedException("you need to login first");
        }

        courseEnrollmentService.enrollCourse(id,userDetailsDto.getEmail());

        return ResponseEntity.ok(new ApiResponse<>(true,"course has been enrolled", null));

    }

}
