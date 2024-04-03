package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.CourseNoteDto;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.mapper.CourseNotesMapper;
import com.example.enrollingservice.model.CourseNotes;
import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.repository.CourseNotesRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.response.CourseNotesResponse;
import com.example.enrollingservice.service.CourseNotesService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseNotesServiceImpl implements CourseNotesService {

    final
    CourseNotesRepository courseNotesRepository;

    final
    StudentLessonRepository studentLessonRepository;

    final
    CourseNotesMapper courseNotesMapper;

    public CourseNotesServiceImpl(CourseNotesRepository courseNotesRepository, StudentLessonRepository studentLessonRepository, CourseNotesMapper courseNotesMapper) {
        this.courseNotesRepository = courseNotesRepository;
        this.studentLessonRepository = studentLessonRepository;
        this.courseNotesMapper = courseNotesMapper;
    }

    @Override
    public CourseNotesResponse addCourseNote(CourseNoteDto courseNote, Long studentLessonID) {


        CourseNotes courseNotes = new CourseNotes();
        StudentLesson studentLesson = findCourseNotesById(studentLessonID);
        courseNotes.setStudentLesson(studentLesson);
        courseNotes.setRecord(courseNote.getRecord());

        CourseNotes courseNotes1 = courseNotesRepository.save(courseNotes);

        return courseNotesMapper.courseNotesToCourseNotesResponse(courseNotes1);
    }

    @Override
    public void deleteCourseNote(Long id) {
        courseNotesRepository.deleteById(id);
    }

    @Override
    public List<CourseNotesResponse> getCourseNotes(Long studentLessonId) {

        List<CourseNotes> courseNotes = courseNotesRepository.findByStudentLessonId(studentLessonId);

        return courseNotesMapper.courseNotesListToCourseNotesResponseList(courseNotes);
    }

    private StudentLesson findCourseNotesById(Long id){
        return studentLessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("student lesson not found with this id "+ id)
        );
    }
}
