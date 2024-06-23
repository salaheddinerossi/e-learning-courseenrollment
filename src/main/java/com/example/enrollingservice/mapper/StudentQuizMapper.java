package com.example.enrollingservice.mapper;

import com.example.enrollingservice.model.StudentQuiz;
import com.example.enrollingservice.response.StudentQuizResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentQuizMapper {
    @Mapping(source = "quiz.id", target = "quiz_id")
    @Mapping(source = "mark", target = "mark")
    StudentQuizResponse studentQuizToStudentQuizResponse(StudentQuiz studentQuiz);

    List<StudentQuizResponse> studentQuizListToStudentQuizResponseList(List<StudentQuiz> studentQuiz);
}
