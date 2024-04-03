package com.example.enrollingservice.response;


import com.example.enrollingservice.Enums.StudentLessonStatus;
import lombok.Data;

import java.util.List;

@Data
public class StudentLessonResponse {

    private Long id;

    private Boolean isChatExist;

    private StudentLessonStatus studentLessonStatus;

    private List<ChatHistoryResponse> chatHistoryResponseList;

    private List<CourseNotesResponse> courseNotesResponses;

    private List<StudentQuizResponse> studentQuizResponses;

}
