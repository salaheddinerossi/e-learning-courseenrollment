package com.example.enrollingservice.ServiceTest;


import com.example.enrollingservice.model.*;
import com.example.enrollingservice.repository.CourseEnrollmentRepository;
import com.example.enrollingservice.repository.CourseRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.repository.StudentRepository;
import com.example.enrollingservice.serviceImpl.CourseEnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseEnrollmentServiceTest {

    @Mock
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentLessonRepository studentLessonRepository;

    @InjectMocks
    CourseEnrollmentServiceImpl courseEnrollmentService;


    private Course mockedCourse;

    private Student mockedStudent;

    private String email = "salah@gmail.com";

    private Long courseId = 1L;

    @Captor
    private ArgumentCaptor<StudentLesson> studentLessonCaptor;


    private Lesson mockedLesson;
    private Lesson mockedLesson1;
    private Lesson mockedLesson2;
    @BeforeEach
    void SetUp() {
        // Initialize the course and set basic properties
        mockedCourse = new Course();
        mockedCourse.setId(courseId);
        mockedCourse.setAbout("test");
        mockedCourse.setTitle("course1");
        mockedCourse.setChapters(new ArrayList<>());  // Initialize chapters list

        // Create lessons
        mockedLesson = new Lesson();
        mockedLesson.setId(1L);
        mockedLesson.setTitle("hello");
        mockedLesson.setIsDeleted(false);

        mockedLesson1 = new Lesson();
        mockedLesson1.setId(2L);
        mockedLesson1.setTitle("hello2");
        mockedLesson1.setIsDeleted(false);

        mockedLesson2 = new Lesson();
        mockedLesson2.setId(3L);
        mockedLesson2.setTitle("hello3");
        mockedLesson2.setIsDeleted(false);

        // Assign lessons to a chapter without subchapters
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setLessons(new ArrayList<>());  // Initialize lessons list
        chapter.getLessons().add(mockedLesson);
        chapter.getLessons().add(mockedLesson1);
        chapter.setContainsChapters(false);

        // Create a chapter with subchapters
        Chapter chapter1 = new Chapter();
        chapter1.setId(2L);
        chapter1.setContainsChapters(true);
        chapter1.setChildChapters(new ArrayList<>());  // Initialize child chapters list

        Chapter chapter2 = new Chapter();
        chapter2.setId(3L);
        chapter2.setContainsChapters(false);
        chapter2.setLessons(new ArrayList<>());  // Initialize lessons list
        chapter2.getLessons().add(mockedLesson2);

        chapter1.getChildChapters().add(chapter2);  // Add subchapter to chapter1

        // Add chapters to the course
        mockedCourse.getChapters().add(chapter);
        mockedCourse.getChapters().add(chapter1);

        // Initialize the student
        mockedStudent = new Student();
        mockedStudent.setFirstName("salah eddine");
        mockedStudent.setLastName("rossi");
        mockedStudent.setEmail(email);  // This line should set the email instead of last name

        // Ensure the student's email is set correctly
        mockedStudent.setEmail(email);
    }

    @Test
    void testEnrollCourseSuccess(){

        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(mockedStudent));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockedCourse));
        when(courseEnrollmentRepository.findByCourseIdAndStudent(courseId,mockedStudent)).thenReturn(Optional.empty());


        when(studentLessonRepository.save(any(StudentLesson.class))).thenAnswer(invocation -> {
            StudentLesson savedStudentLesson = invocation.getArgument(0);
            savedStudentLesson.setId(100L);
            return savedStudentLesson;
        });

        when(courseEnrollmentRepository.save(any(CourseEnrollment.class))).thenAnswer(invocation -> {
            CourseEnrollment savedCourseEnrollment = invocation.getArgument(0);
            savedCourseEnrollment.setId(1L);
            return savedCourseEnrollment;
        });



        Long enrollmentId = courseEnrollmentService.enrollCourse(courseId, email);

        verify(studentLessonRepository, times(3)).save(studentLessonCaptor.capture());
        List<StudentLesson> capturedStudentLessons = studentLessonCaptor.getAllValues();

        assertEquals(mockedLesson.getTitle(), capturedStudentLessons.get(0).getLesson().getTitle());
        assertEquals(mockedLesson1.getTitle(), capturedStudentLessons.get(1).getLesson().getTitle());
        assertEquals(mockedLesson2.getTitle(), capturedStudentLessons.get(2).getLesson().getTitle());

        assertEquals(3, capturedStudentLessons.size());



        verify(courseEnrollmentRepository, times(1)).save(any(CourseEnrollment.class));
        assertEquals(1L, enrollmentId);

    }

}
