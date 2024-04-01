package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.Student;
import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.repository.ChatHistoryRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.repository.StudentRepository;
import com.example.enrollingservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentServiceImpl implements StudentService {

    final
    ChatHistoryRepository chatHistoryRepository;

    final
    StudentRepository studentRepository;

    final
    StudentLessonRepository studentLessonRepository;

    public StudentServiceImpl(StudentRepository studentRepository, StudentLessonRepository studentLessonRepository, ChatHistoryRepository chatHistoryRepository) {
        this.studentRepository = studentRepository;
        this.studentLessonRepository = studentLessonRepository;
        this.chatHistoryRepository = chatHistoryRepository;
    }

    @Override
    public Boolean studentHasStudentLesson(String email, Long studentLessonId) {

        return findStudentLessonById(studentLessonId).getCourseEnrollment().getStudent() == findStudentByEmail(email);
    }

    @Override
    public Boolean studentHasStudentChat(String email, Long chatId) {
        return findChatHistory(chatId).getStudentLesson().getCourseEnrollment().getStudent() == findStudentByEmail(email);
    }

    Student findStudentByEmail(String email){
        return studentRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("no student with this id: " +email)
        );
    }

    StudentLesson findStudentLessonById(Long id){
        return studentLessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no student lesson with this id: " +id)
        );
    }

    ChatHistory findChatHistory(Long id){
        return chatHistoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no chat history found with this id: " +id)
        );
    }
}
