package com.example.enrollingservice.service;

public interface StudentService {

    Boolean studentHasStudentLesson(String email,Long studentLessonId);

    Boolean studentHasStudentChat(String email,Long ChatID);

    Boolean studentHasEnrollment(String email,Long courseEnrollmentId);
}
