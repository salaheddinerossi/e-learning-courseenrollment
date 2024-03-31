package com.example.enrollingservice.response;


import com.example.enrollingservice.Enums.StudentLessonStatus;
import lombok.Data;

@Data
public class StudentLessonStatusResponse {

    private Long id;

    private Long lesson_id;

    private String title;

    private StudentLessonStatus studentLessonStatus;

}
