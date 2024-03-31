package com.example.enrollingservice.response;


import lombok.Data;

import java.util.List;

@Data
public class CourseEnrollmentResponse {
    private Long id;

    private List<ChapterResponse> chapterResponses;

}
