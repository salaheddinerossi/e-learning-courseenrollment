package com.example.enrollingservice.controller;

import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.UnauthorizedException;
import com.example.enrollingservice.response.CourseEnrollmentIds;
import com.example.enrollingservice.response.CourseEnrollmentResponse;
import com.example.enrollingservice.response.CourseResponse;
import com.example.enrollingservice.service.AuthService;
import com.example.enrollingservice.service.CourseEnrollmentService;
import com.example.enrollingservice.service.StudentService;
import com.example.enrollingservice.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class CourseEnrollmentController {

    @Value("${auth.url}")
    private String authUrl;


    final
    AuthService authService;

    final
    CourseEnrollmentService courseEnrollmentService;

    final
    StudentService studentService;

    public CourseEnrollmentController(CourseEnrollmentService courseEnrollmentService, AuthService authService, StudentService studentService) {
        this.courseEnrollmentService = courseEnrollmentService;
        this.authService = authService;
        this.studentService = studentService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> enrollCourse(@PathVariable Long id,@RequestHeader("Authorization") String token){

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isStudent(userDetailsDto)){
            throw new UnauthorizedException("you need to login first");
        }

        courseEnrollmentService.enrollCourse(id,userDetailsDto.getEmail());

        return ResponseEntity.ok(new ApiResponse<>(true,"course has been enrolled", null));

    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseEnrollmentResponse>> getCourseEnrollment(@PathVariable Long id,@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);
        if (!studentService.studentHasEnrollment(userDetailsDto.getEmail(),id)){
            throw new UnauthorizedException("you are not the owner of this enrollment");
        }
        return ResponseEntity.ok(new ApiResponse<>(true,"courseEnrollment has been fetched ",courseEnrollmentService.getCourseEnrollmentResponse(id)));
    }

    @GetMapping("/enrolledCourses")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getEnrolledCourses(@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isStudent(userDetailsDto)){
            throw new UnauthorizedException("you are not allowed to perform this action");
        }
        return ResponseEntity.ok(new ApiResponse<>(true,"courseEnrollment has been fetched ",courseEnrollmentService.getEnrolledCourses(userDetailsDto.getEmail())));
    }

    @GetMapping("/courseEnrollments")
    public ResponseEntity<ApiResponse<List<CourseEnrollmentIds>>> getEnrolledCoursesIds(@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!authService.isStudent(userDetailsDto)){
            throw new UnauthorizedException("you are not allowed to perform this action");
        }
        return ResponseEntity.ok(new ApiResponse<>(true,"courseEnrollment has been fetched ",courseEnrollmentService.getEnrolledCoursesWithIds(userDetailsDto.getEmail())));
    }



}
