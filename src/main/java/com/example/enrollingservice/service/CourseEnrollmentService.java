package com.example.enrollingservice.service;

import com.example.enrollingservice.response.CourseEnrollmentIds;
import com.example.enrollingservice.response.CourseEnrollmentResponse;
import com.example.enrollingservice.response.CourseResponse;

import java.util.List;

public interface CourseEnrollmentService {

    // enroll a course
    Long enrollCourse(Long courseId,String email);

    //get course
    CourseEnrollmentResponse getCourseEnrollmentResponse(Long id);

    List<CourseResponse> getEnrolledCourses(String email);


    List<CourseEnrollmentIds> getEnrolledCoursesWithIds(String email);

}
