package com.example.enrollingservice.mapper;


import com.example.enrollingservice.model.Course;
import com.example.enrollingservice.response.CourseResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface CourseMapper {

    CourseResponse courseToCourseResponse(Course course);

    List<CourseResponse> courseListToCourseResponseList(List<Course> courses);
}
