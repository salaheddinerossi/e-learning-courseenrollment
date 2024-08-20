package com.example.enrollingservice.ControllerTest;


import com.example.enrollingservice.Enums.CourseStatus;
import com.example.enrollingservice.controller.CourseEnrollmentController;
import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.response.CourseEnrollmentIds;
import com.example.enrollingservice.response.CourseEnrollmentResponse;
import com.example.enrollingservice.response.CourseResponse;
import com.example.enrollingservice.serviceImpl.AuthServiceImpl;
import com.example.enrollingservice.serviceImpl.CourseEnrollmentServiceImpl;
import com.example.enrollingservice.serviceImpl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CourseEnrollmentController.class)
public class CourseEnrollmentControllerTest {

    Long courseId = 1L;
    String token = "Bearer some-token";
    UserDetailsDto mockUserDetails;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private CourseEnrollmentServiceImpl courseEnrollmentService;

    @MockBean
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp(){

        Long courseId = 1L;
        String token = "Bearer some-token";
        mockUserDetails = new UserDetailsDto();
        mockUserDetails.setEmail("student@example.com");

    }

    @Test
    void testEnrollCourseSuccess() throws Exception {
        Long courseEnrollmentId = 100L;

        when(authService.getUserDetailsFromAuthService(anyString(), anyString())).thenReturn(mockUserDetails);

        when(authService.isStudent(mockUserDetails)).thenReturn(true);

        when(courseEnrollmentService.enrollCourse(courseId,mockUserDetails.getEmail())).thenReturn(courseEnrollmentId);

        mockMvc.perform(MockMvcRequestBuilders.post("/enrollment/{id}", courseId)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("course has been enrolled"))
                .andExpect(jsonPath("$.data").value(courseEnrollmentId));

    }

    @Test
    void testGetCourseEnrollmentSuccess() throws Exception {

        Long courseEnrollmentId = 100L;
        CourseEnrollmentResponse mockCourseEnrollmentResponse = new CourseEnrollmentResponse();
        mockCourseEnrollmentResponse.setId(courseId);
        mockCourseEnrollmentResponse.setCurrentLessonId(100L);
        mockCourseEnrollmentResponse.setCategoryId(200L);
        mockCourseEnrollmentResponse.setCategoryName("Math");
        mockCourseEnrollmentResponse.setIsReviewed(false);
        mockCourseEnrollmentResponse.setIsCourseCompleted(false);
        mockCourseEnrollmentResponse.setChapterResponses(new ArrayList<>());

        when(authService.getUserDetailsFromAuthService(anyString(),anyString())).thenReturn(mockUserDetails);
        when(studentService.studentHasEnrollment(mockUserDetails.getEmail(),courseEnrollmentId)).thenReturn(true);
        when(courseEnrollmentService.getCourseEnrollmentResponse(courseEnrollmentId)).thenReturn(mockCourseEnrollmentResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/enrollment/{id}", courseEnrollmentId)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("courseEnrollment has been fetched "))
                .andExpect(jsonPath("$.data.id").value(courseId))
                .andExpect(jsonPath("$.data.currentLessonId").value(100L))
                .andExpect(jsonPath("$.data.categoryId").value(200L))
                .andExpect(jsonPath("$.data.categoryName").value("Math"))
                .andExpect(jsonPath("$.data.isReviewed").value(false))
                .andExpect(jsonPath("$.data.isCourseCompleted").value(false));

    }

    @Test
    void  testGetEnrolledCoursesSuccess() throws Exception {

        List<CourseResponse> mockCourseResponses = new ArrayList<>();
        CourseResponse course1 = new CourseResponse();
        course1.setId(1L);

        course1.setTitle("Course 1");
        course1.setAbout("About Course 1");
        course1.setImage("image1.png");
        course1.setCourseStatusEnum("DRAFT");

        CourseResponse course2 = new CourseResponse();
        course2.setId(2L);
        course2.setTitle("Course 2");
        course2.setAbout("About Course 2");
        course2.setImage("image2.png");
        course2.setCourseStatusEnum("DRAFT");

        mockCourseResponses.add(course1);
        mockCourseResponses.add(course2);

        when(authService.getUserDetailsFromAuthService(any(),any())).thenReturn(mockUserDetails);
        when(authService.isStudent(mockUserDetails)).thenReturn(true);

        when(courseEnrollmentService.getEnrolledCourses(mockUserDetails.getEmail())).thenReturn(mockCourseResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/enrollment/enrolledCourses")
                .header(HttpHeaders.AUTHORIZATION,token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("courseEnrollment has been fetched "))
                .andExpect(jsonPath("$.data[0].title").value("Course 1"))
                .andExpect(jsonPath("$.data[0].about").value("About Course 1"))
                .andExpect(jsonPath("$.data[0].image").value("image1.png"))
                .andExpect(jsonPath("$.data[0].courseStatusEnum").value("DRAFT"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("Course 2"))
                .andExpect(jsonPath("$.data[1].about").value("About Course 2"))
                .andExpect(jsonPath("$.data[1].image").value("image2.png"))
                .andExpect(jsonPath("$.data[1].courseStatusEnum").value("DRAFT"));


    }

    @Test
    void getEnrolledCoursesIds() throws Exception {
        CourseEnrollmentIds courseEnrollmentIds = new CourseEnrollmentIds();
        courseEnrollmentIds.setCourse_id(1L);
        courseEnrollmentIds.setCourse_enrollment_id(1L);

        CourseEnrollmentIds courseEnrollmentIds1 = new CourseEnrollmentIds();
        courseEnrollmentIds1.setCourse_id(2L);
        courseEnrollmentIds1.setCourse_enrollment_id(2L);

        List<CourseEnrollmentIds> mockCourseEnrollmentIds = new ArrayList<>();
        mockCourseEnrollmentIds.add(courseEnrollmentIds);
        mockCourseEnrollmentIds.add(courseEnrollmentIds1);

        when(authService.getUserDetailsFromAuthService(any(),any())).thenReturn(mockUserDetails);
        when(authService.isStudent(mockUserDetails)).thenReturn(true);
        when(courseEnrollmentService.getEnrolledCoursesWithIds(mockUserDetails.getEmail())).thenReturn(mockCourseEnrollmentIds);

        mockMvc.perform(MockMvcRequestBuilders.get("/enrollment/courseEnrollments")
                        .header(HttpHeaders.AUTHORIZATION,token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("courseEnrollment has been fetched "))
                .andExpect(jsonPath("$.data[0].course_id").value(1L))
                .andExpect(jsonPath("$.data[0].course_enrollment_id").value(1L))
                .andExpect(jsonPath("$.data[1].course_id").value(2L))
                .andExpect(jsonPath("$.data[1].course_enrollment_id").value(2L));



    }



}
