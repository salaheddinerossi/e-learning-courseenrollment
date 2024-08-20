package com.example.enrollingservice.RepositoryTest;

import com.example.enrollingservice.Enums.StudentLessonStatus;
import com.example.enrollingservice.model.*;
import com.example.enrollingservice.repository.CourseEnrollmentRepository;
import com.example.enrollingservice.repository.CourseNotesRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CourseRepositoryTest {

    @Autowired
    private CourseNotesRepository courseNotesRepository;

    @Autowired
    private StudentLessonRepository studentLessonRepository;

    @Autowired
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    StudentRepository studentRepository;

    private StudentLesson studentLesson;


    String email = "salahdinroussi@gmail.com";
    String password = "password";

    @BeforeEach
    void setUp() {
        Student user = new Student();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName("salah eddine");
        user.setLastName("rossi");
        studentRepository.save(user);

        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.setCourse(null);
        courseEnrollment.setCurrentLessonId(null);
        courseEnrollment.setIsCompleted(true);
        courseEnrollment.setReview(null);
        courseEnrollment.setStudent(user);
        courseEnrollmentRepository.save(courseEnrollment);

        studentLesson = new StudentLesson();
        studentLesson.setStudentLessonStatus(StudentLessonStatus.INITIAL);
        studentLesson.setCourseEnrollment(courseEnrollment);
        studentLesson.setStudentQuizzes(null);
        studentLesson.setChatHistory(null);

        studentLesson = studentLessonRepository.save(studentLesson);

        CourseNotes note1 = new CourseNotes();
        note1.setId(1L);
        note1.setRecord("Note 1");
        note1.setStudentLesson(studentLesson);

        CourseNotes note2 = new CourseNotes();
        note1.setId(2L);
        note2.setRecord("Note 2");
        note2.setStudentLesson(studentLesson);

        courseNotesRepository.save(note1);
        courseNotesRepository.save(note2);
    }

    @Test
    void testFindByIdAndStudentLessonCourseEnrollmentStudentEmail() {
        Optional<CourseNotes> courseNote = courseNotesRepository.findByIdAndStudentLessonCourseEnrollmentStudentEmail(1L,email);

        assertThat(courseNote.get().getRecord()).isEqualTo("Note 1");

    }

    @Test
    void testFindByStudentLesson(){
        List<CourseNotes> notes = courseNotesRepository.findByStudentLessonId(studentLesson.getId());

        assertThat(notes).isNotEmpty();
        assertThat(notes).hasSize(2);
        assertThat(notes.get(0).getRecord()).isEqualTo("Note 1");
        assertThat(notes.get(1).getRecord()).isEqualTo("Note 2");


    }


}
