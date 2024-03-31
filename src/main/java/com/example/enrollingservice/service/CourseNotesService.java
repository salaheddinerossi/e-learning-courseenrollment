package com.example.enrollingservice.service;

import com.example.enrollingservice.dto.CourseNoteDto;
import com.example.enrollingservice.response.CourseNotesResponse;

public interface CourseNotesService {

    CourseNotesResponse addCourseNote(CourseNoteDto courseNote,Long studentLessonId);

    void deleteCourseNote(Long id);
}
