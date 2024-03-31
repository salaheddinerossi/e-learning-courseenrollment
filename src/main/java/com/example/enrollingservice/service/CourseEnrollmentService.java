package com.example.enrollingservice.service;

import com.example.enrollingservice.response.CourseEnrollmentResponse;

public interface CourseEnrollmentService {

    // enroll a course
    void enrollCourse(Long courseId,Long studentId);

    //get course
    CourseEnrollmentResponse getCourseEnrollmentResponse(Long id);

}
