package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.model.ChatHistory;
import com.example.enrollingservice.model.Student;
import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.repository.*;
import com.example.enrollingservice.service.CourseEnrollmentService;
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

    final
    StudentQuizRepository studentQuizRepository;

    final
    CourseEnrollmentRepository courseEnrollmentRepository;

    final
    CourseNotesRepository courseNotesRepository;

    public StudentServiceImpl(StudentRepository studentRepository, StudentLessonRepository studentLessonRepository, ChatHistoryRepository chatHistoryRepository, StudentQuizRepository studentQuizRepository, CourseEnrollmentRepository courseEnrollmentRepository, CourseNotesRepository courseNotesRepository) {
        this.studentRepository = studentRepository;
        this.studentLessonRepository = studentLessonRepository;
        this.chatHistoryRepository = chatHistoryRepository;
        this.studentQuizRepository = studentQuizRepository;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseNotesRepository = courseNotesRepository;
    }

    @Override
    public Boolean studentHasStudentLesson(String email, Long studentLessonId) {

        return findStudentLessonById(studentLessonId).getCourseEnrollment().getStudent() == findStudentByEmail(email);
    }

    @Override
    public Boolean studentHasStudentChat(String email, Long chatId) {
        return findChatHistory(chatId).getStudentLesson().getCourseEnrollment().getStudent() == findStudentByEmail(email);
    }

    @Override
    public Boolean studentHasEnrollment(String email, Long courseEnrollmentId) {
        return courseEnrollmentRepository.findByIdAndStudentEmail(courseEnrollmentId,email).isPresent();
    }

    @Override
    public Boolean studentHasCourseNote(String email, Long courseNoteId) {
        return courseNotesRepository.findByIdAndStudentLessonCourseEnrollmentStudentEmail(courseNoteId,email).isPresent();
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
