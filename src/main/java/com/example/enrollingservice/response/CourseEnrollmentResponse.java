package com.example.enrollingservice.response;


import lombok.Data;

import java.util.List;

@Data
public class CourseEnrollmentResponse {
    private Long id;

    private Long currentLessonId;

    private Long categoryId;

    private String categoryName;

    private Boolean isReviewed;

    private Boolean isCourseCompleted;

    private List<ChapterResponse> chapterResponses;

}
