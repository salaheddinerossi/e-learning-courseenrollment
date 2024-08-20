package com.example.enrollingservice.RepositoryTest;


import com.example.enrollingservice.model.Course;
import com.example.enrollingservice.model.CourseEnrollment;
import com.example.enrollingservice.model.Student;
import com.example.enrollingservice.repository.CourseEnrollmentRepository;
import com.example.enrollingservice.repository.CourseRepository;
import com.example.enrollingservice.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CourseEnrollmentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    CourseRepository courseRepository;

    private Student student;

    private CourseEnrollment courseEnrollment;



    @BeforeEach
    void SetUp(){

        student = new Student();


        student.setPassword("password");
        student.setEmail("email");
        student.setFirstName("test");
        student.setLastName("test");

        student = studentRepository.save(student);

        Course course = new Course();
        course.setId(1L);
        courseRepository.save(course);

        courseEnrollment = new CourseEnrollment();
        courseEnrollment.setCourse(null);
        courseEnrollment.setCurrentLessonId(null);
        courseEnrollment.setIsCompleted(false);
        courseEnrollment.setReview(null);
        courseEnrollment.setStudent(student);
        courseEnrollment.setCourse(course);

        courseEnrollment = courseEnrollmentRepository.save(courseEnrollment);


    }

    @Test
    void findByCourseIdAndStudentTest() {
        Optional<CourseEnrollment> courseEnrollment = courseEnrollmentRepository.findByCourseIdAndStudent(1L, student);

        assertThat(courseEnrollment.isPresent()).isTrue();
        CourseEnrollment enrollment = courseEnrollment.get();
        assertThat(enrollment.getStudent().getFirstName()).isEqualTo("test");
        assertThat(enrollment.getCourse().getId()).isEqualTo(1L);
        assertThat(enrollment.getIsCompleted()).isFalse();
        assertThat(enrollment.getReview()).isNull();
    }

    @Test
    void findByCourseIdAndStudentWhenNotEnrolledTest() {
        Optional<CourseEnrollment> courseEnrollment = courseEnrollmentRepository.findByCourseIdAndStudent(2L, student);
        assertThat(courseEnrollment.isPresent()).isFalse();
    }

    @Test
    void findByIdAndStudentEmailTest(){
        Optional<CourseEnrollment> courseEnrollmentTested = courseEnrollmentRepository.findByIdAndStudentEmail(courseEnrollment.getId(),student.getEmail());

        assertThat(courseEnrollmentTested.isPresent()).isTrue();

    }

    @Test
    void DontFindByIdAndStudentEmailTest(){
        Optional<CourseEnrollment> courseEnrollmentTested = courseEnrollmentRepository.findByIdAndStudentEmail(courseEnrollment.getId(),"else");

        assertThat(courseEnrollmentTested.isPresent()).isFalse();
    }

    @Test
    void findByStudentEmail(){
        List<CourseEnrollment> courseEnrollments = courseEnrollmentRepository.findByStudentEmail(student.getEmail());

        Assertions.assertThat(courseEnrollments).isNotEmpty();
        Assertions.assertThat(courseEnrollments).hasSize(1);
        assertThat(courseEnrollments.get(0).getStudent().getFirstName()).isEqualTo("test");

    }



}
