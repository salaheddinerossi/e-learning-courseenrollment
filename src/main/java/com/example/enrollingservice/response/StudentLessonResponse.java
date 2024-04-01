package com.example.enrollingservice.response;


import lombok.Data;

import java.util.List;

@Data
public class StudentLessonResponse {

    private Long id;

    private Boolean isChatExist;

    private List<ChatHistoryResponse> chatHistoryResponseList;

    private List<CourseNotesResponse> courseNotesResponses;

    private List<StudentQuizResponse> studentQuizResponses;

}
