package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.Enums.StudentLessonStatus;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.model.*;
import com.example.enrollingservice.repository.CourseEnrollmentRepository;
import com.example.enrollingservice.repository.CourseRepository;
import com.example.enrollingservice.repository.StudentRepository;
import com.example.enrollingservice.response.ChapterResponse;
import com.example.enrollingservice.response.CourseEnrollmentResponse;
import com.example.enrollingservice.response.StudentLessonStatusResponse;
import com.example.enrollingservice.service.CourseEnrollmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    final
    CourseRepository courseRepository;

    final
    StudentRepository studentRepository;

    final
    CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseEnrollmentServiceImpl(CourseEnrollmentRepository courseEnrollmentRepository, CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public void enrollCourse(Long courseId, Long studentId) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();

        Course course = findCourseById(courseId);

        courseEnrollment.setCourse(course);
        courseEnrollment.setStudent(findStudentById(studentId));

        StudentLesson studentLesson = new StudentLesson();
        StudentQuiz studentQuiz = new StudentQuiz();
        for (Lesson lesson:getAllLessons(course)){

            if (lesson.getIsDeleted()){
                continue;
            }

            if (courseEnrollment.getCurrentLessonId()==null){
                courseEnrollment.setCurrentLessonId(lesson.getId());
            }else {
                if (courseEnrollment.getCurrentLessonId()> lesson.getId()){
                    courseEnrollment.setCurrentLessonId(lesson.getId());
                }
            }

            studentLesson.setCourseEnrollment(courseEnrollment);
            studentLesson.setLesson(lesson);
            studentLesson.setStudentLessonStatus(StudentLessonStatus.INITIAL);

            for (Quiz quiz:lesson.getQuizzes()){

                studentQuiz.setQuiz(quiz);
                studentQuiz.setStudentLesson(studentLesson);
                studentLesson.getStudentQuizzes().add(studentQuiz);

            }

            courseEnrollment.getStudentLessons().add(studentLesson);

        }

        courseEnrollmentRepository.save(courseEnrollment);

    }

    @Override
    @Transactional
    public CourseEnrollmentResponse getCourseEnrollmentResponse(Long id) {

        CourseEnrollment courseEnrollment = findCourseEnrollmentById(id);
        Course course = courseEnrollment.getCourse();

        CourseEnrollmentResponse courseEnrollmentResponse = new CourseEnrollmentResponse();
        courseEnrollmentResponse.setId(courseEnrollment.getId());

        List<ChapterResponse> chapterResponses = mapChaptersToChapterResponses(course.getChapters(),courseEnrollment);
        courseEnrollmentResponse.setChapterResponses(chapterResponses);

        return courseEnrollmentResponse;
    }


    private List<Lesson> getAllLessons(Course course) {
        List<Lesson> allLessons = new ArrayList<>();
        List<Chapter> chapters = course.getChapters();

        for (Chapter chapter : chapters) {
            if (chapter.getContainsChapters()) {
                allLessons.addAll(getLessonsFromChapterWithSubChapters(chapter));
            } else {
                allLessons.addAll(chapter.getLessons());
            }
        }

        return allLessons;
    }

    private List<Lesson> getLessonsFromChapterWithSubChapters(Chapter chapter) {
        List<Lesson> allLessons = new ArrayList<>();
        List<Chapter> subChapters = chapter.getChildChapters();

        for (Chapter subChapter : subChapters) {
            if (subChapter.getContainsChapters()) {
                allLessons.addAll(getLessonsFromChapterWithSubChapters(subChapter));
            } else {
                allLessons.addAll(subChapter.getLessons());
            }
        }
        return allLessons;
    }


    private List<ChapterResponse> mapChaptersToChapterResponses(List<Chapter> chapters,CourseEnrollment courseEnrollment) {
        List<ChapterResponse> chapterResponses = new ArrayList<>();

        for (Chapter chapter : chapters) {
            ChapterResponse chapterResponse = new ChapterResponse();
            chapterResponse.setId(chapter.getId());
            chapterResponse.setTitle(chapter.getTitle());
            chapterResponse.setContainsChapters(chapter.getContainsChapters());

            if (chapter.getContainsChapters()) {
                chapterResponse.setChapterResponses(mapChaptersToChapterResponses(chapter.getChildChapters(),courseEnrollment));
            } else {
                List<StudentLessonStatusResponse> studentLessonStatusResponses = mapLessonsToStatusResponses(chapter.getLessons(),courseEnrollment);
                chapterResponse.setStudentLessonStatusResponses(studentLessonStatusResponses);
            }

            chapterResponses.add(chapterResponse);
        }

        return chapterResponses;
    }

    private List<StudentLessonStatusResponse> mapLessonsToStatusResponses(List<Lesson> lessons,CourseEnrollment courseEnrollment) {
        List<StudentLessonStatusResponse> responses = new ArrayList<>();
        for (Lesson lesson : lessons) {
            StudentLessonStatusResponse response = new StudentLessonStatusResponse();
             response.setTitle(lesson.getTitle());
             response.setLesson_id(lesson.getId());

             boolean hasLesson =true;

             for (StudentLesson studentLesson :courseEnrollment.getStudentLessons()){
                 if (studentLesson.getLesson()==lesson){
                     response.setId(studentLesson.getId());
                     response.setStudentLessonStatus(studentLesson.getStudentLessonStatus());
                 }else {
                     hasLesson = false;
                 }
             }
             if (hasLesson){
                 responses.add(response);
             }

        }
        return responses;
    }



    private Course findCourseById(Long id){
        return courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course not found with this id: "+id)
        );
    }

    private Student findStudentById(Long id){
        return studentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Student not found with this id: "+id)
        );
    }

    private CourseEnrollment findCourseEnrollmentById(Long id){
        return courseEnrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no course enrollment with this id: " +id)
        );
    }
}
