package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.Enums.StudentLessonStatus;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.mapper.ChatHistoryMapper;
import com.example.enrollingservice.mapper.CourseNotesMapper;
import com.example.enrollingservice.mapper.StudentQuizMapper;
import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.CourseNotes;
import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.model.StudentQuiz;
import com.example.enrollingservice.repository.ChatHistoryRepository;
import com.example.enrollingservice.repository.CourseNotesRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.repository.StudentQuizRepository;
import com.example.enrollingservice.response.ChatHistoryResponse;
import com.example.enrollingservice.response.CourseNotesResponse;
import com.example.enrollingservice.response.StudentLessonResponse;
import com.example.enrollingservice.response.StudentQuizResponse;
import com.example.enrollingservice.service.StudentLessonService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StudentLessonServiceImpl implements StudentLessonService {

    final
    StudentLessonRepository studentLessonRepository;

    final
    CourseNotesRepository courseNotesRepository;

    final
    CourseNotesMapper courseNotesMapper;

    final
    ChatHistoryMapper chatHistoryMapper;

    final
    ChatHistoryRepository chatHistoryRepository;

    final
    StudentQuizRepository studentQuizRepository;

    final
    StudentQuizMapper studentQuizMapper;

    public StudentLessonServiceImpl(CourseNotesMapper courseNotesMapper, CourseNotesRepository courseNotesRepository, StudentLessonRepository studentLessonRepository, ChatHistoryMapper chatHistoryMapper, ChatHistoryRepository chatHistoryRepository, StudentQuizRepository studentQuizRepository, StudentQuizMapper studentQuizMapper) {
        this.courseNotesMapper = courseNotesMapper;
        this.courseNotesRepository = courseNotesRepository;
        this.studentLessonRepository = studentLessonRepository;
        this.chatHistoryMapper =chatHistoryMapper;
        this.chatHistoryRepository =chatHistoryRepository;
        this.studentQuizRepository = studentQuizRepository;
        this.studentQuizMapper = studentQuizMapper;
    }

    @Override
    @Transactional
    public StudentLessonResponse getStudentLesson(Long id) {
        StudentLesson studentLesson = findStudentLessonById(id);
        StudentLessonResponse studentLessonResponse = new StudentLessonResponse();
        studentLessonResponse.setId(studentLesson.getId());
        studentLessonResponse.setStudentLessonStatus(studentLesson.getStudentLessonStatus());

        List<CourseNotes> courseNotes = courseNotesRepository.findByStudentLesson(studentLesson);
        List<CourseNotesResponse> courseNotesResponses = courseNotesMapper.courseNotesListToCourseNotesResponseList(courseNotes);

        Optional<ChatHistory> chatHistoryOptional = chatHistoryRepository.findByStudentLesson(studentLesson);

        if (chatHistoryOptional.isPresent()){
            studentLessonResponse.setIsChatExist(true);
            ChatHistory chatHistory = chatHistoryOptional.get();
            List<ChatHistoryResponse> chatHistoryResponses = chatHistoryMapper.chatRecordListToChatHistoryResponseList(chatHistory.getChatRecords());

            studentLessonResponse.setChatHistoryResponseList(chatHistoryResponses);
        }else{
            studentLessonResponse.setIsChatExist(false);
        }

        List<StudentQuiz> studentQuizzes = studentQuizRepository.findByStudentLesson(studentLesson);
        List<StudentQuizResponse> studentQuizResponses = studentQuizMapper.studentQuizListToStudentQuizResponseList(studentQuizzes);

        studentLessonResponse.setStudentQuizResponses(studentQuizResponses);
        studentLessonResponse.setCourseNotesResponses(courseNotesResponses);


        if (studentLesson.getStudentLessonStatus()==StudentLessonStatus.INITIAL){
            studentLesson.setStudentLessonStatus(StudentLessonStatus.READ);
            studentLessonRepository.save(studentLesson);
        }

        return studentLessonResponse;
    }



    StudentLesson findStudentLessonById(Long id){
        return studentLessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no student lesson with this id: "+id)
        );
    }
}
