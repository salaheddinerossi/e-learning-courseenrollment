package com.example.enrollingservice.response;

import lombok.Data;

import java.util.List;

@Data
public class ChapterResponse {

    private Long id;

    private String title;

    private Boolean containsChapters;

    private List<ChapterResponse> chapterResponses;

    private List<StudentLessonStatusResponse> studentLessonStatusResponses;

}
