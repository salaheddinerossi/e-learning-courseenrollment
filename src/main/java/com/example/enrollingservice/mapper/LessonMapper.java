package com.example.enrollingservice.mapper;

import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.response.StudentLessonStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(source = "lesson.id", target = "lesson_id")
    @Mapping(source = "lesson.title", target = "title")
    @Mapping(source = "studentLessonStatus", target = "studentLessonStatus")
    StudentLessonStatusResponse studentLessonToStudentLessonStatusResponse(StudentLesson studentLesson);

    List<StudentLessonStatusResponse> studentLessonsToStudentLessonStatusResponses(List<StudentLesson> studentLessons);


}
