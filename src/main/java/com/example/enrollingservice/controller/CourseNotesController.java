package com.example.enrollingservice.controller;


import com.example.enrollingservice.dto.CourseNoteDto;
import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.UnauthorizedException;
import com.example.enrollingservice.response.CourseNotesResponse;
import com.example.enrollingservice.service.AuthService;
import com.example.enrollingservice.service.CourseNotesService;
import com.example.enrollingservice.service.StudentService;
import com.example.enrollingservice.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class CourseNotesController {

    @Value("${auth.url}")
    private String authUrl;

    final
    AuthService authService;

    final
    StudentService studentService;

    final
    CourseNotesService courseNotesService;

    public CourseNotesController(AuthService authService, StudentService studentService, CourseNotesService courseNotesService) {
        this.authService = authService;
        this.studentService = studentService;
        this.courseNotesService = courseNotesService;
    }

    @PostMapping("/{id}")
    ResponseEntity<ApiResponse<CourseNotesResponse>> addNote(@RequestBody CourseNoteDto courseNoteDto, @PathVariable Long id,@RequestHeader("Authorization") String token){

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasStudentLesson(userDetailsDto.getEmail(), id)){
            throw new UnauthorizedException("you are not allowed to add notes in this student lesson");
        }

        CourseNotesResponse courseNote = courseNotesService.addCourseNote(courseNoteDto,id);
        return ResponseEntity.ok(new ApiResponse<>(true,"course note has been added", courseNote));

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<?>> deleteCourseNote(@PathVariable Long id,@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasCourseNote(userDetailsDto.getEmail(), id)){
            throw new UnauthorizedException("you are not allowed to delete this note in this student lesson");
        }

        courseNotesService.deleteCourseNote(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"note has been deleted",null));

    }

    @GetMapping("/{studentLessonId}")
    ResponseEntity<ApiResponse<List<CourseNotesResponse>>> getCourseNotesByStudentId(@PathVariable Long studentLessonId,@RequestHeader("Authorization") String token){
        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);

        if (!studentService.studentHasStudentLesson(userDetailsDto.getEmail(),studentLessonId)){
            throw new UnauthorizedException("you are not the owner of this student lesson");
        }

        return ResponseEntity.ok(new ApiResponse<>(true,"notes have been fetched",courseNotesService.getCourseNotes(studentLessonId)));

    }

}
