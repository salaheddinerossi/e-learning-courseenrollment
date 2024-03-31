package com.example.enrollingservice.mapper;

import com.example.enrollingservice.model.StudentQuiz;
import com.example.enrollingservice.response.StudentQuizResponse;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentQuizMapper {

    StudentQuizResponse studentQuizToStudentQuizResponse(StudentQuiz studentQuiz);

    List<StudentQuizResponse> studentQuizListToStudentQuizResponseList(List<StudentQuiz> studentQuiz);
}
