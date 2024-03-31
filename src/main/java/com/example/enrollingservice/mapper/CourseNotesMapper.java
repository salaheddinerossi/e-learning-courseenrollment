package com.example.enrollingservice.mapper;
import com.example.enrollingservice.model.CourseNotes;
import com.example.enrollingservice.response.CourseNotesResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseNotesMapper {


    CourseNotesResponse courseNotesToCourseNotesResponse(CourseNotes courseNotes);

    List<CourseNotesResponse> courseNotesListToCourseNotesResponseList(List<CourseNotes> courseNotes);
}
