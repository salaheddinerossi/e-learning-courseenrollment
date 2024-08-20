package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.Enums.StudentLessonStatus;
import com.example.enrollingservice.exception.BadRequestException;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.mapper.CourseMapper;
import com.example.enrollingservice.mapper.LessonMapper;
import com.example.enrollingservice.model.*;
import com.example.enrollingservice.model.Quizzes.Quiz;
import com.example.enrollingservice.repository.CourseEnrollmentRepository;
import com.example.enrollingservice.repository.CourseRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.repository.StudentRepository;
import com.example.enrollingservice.response.*;
import com.example.enrollingservice.service.CourseEnrollmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    final
    CourseRepository courseRepository;

    final
    StudentRepository studentRepository;

    final
    CourseEnrollmentRepository courseEnrollmentRepository;

    final
    StudentLessonRepository studentLessonRepository;

    final
    LessonMapper lessonMapper;

    final CourseMapper courseMapper;


    public CourseEnrollmentServiceImpl(CourseEnrollmentRepository courseEnrollmentRepository, CourseRepository courseRepository, StudentRepository studentRepository, LessonMapper lessonMapper, StudentLessonRepository studentLessonRepository,CourseMapper courseMapper) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.lessonMapper = lessonMapper;
        this.studentLessonRepository = studentLessonRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional
    public Long enrollCourse(Long courseId, String email) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();

        Course course = findCourseById(courseId);
        Student student = findStudentByEmail(email);

        if (courseEnrollmentRepository.findByCourseIdAndStudent(courseId, student).isPresent()) {
            throw new BadRequestException("You have already enrolled in this course");
        }

        courseEnrollment.setCourse(course);
        courseEnrollment.setStudent(student);

        for (Lesson lesson : getAllLessons(course)) {
            if (lesson.getIsDeleted()) {
                continue;
            }

            StudentLesson studentLesson = new StudentLesson();
            studentLesson.setCourseEnrollment(courseEnrollment);
            studentLesson.setLesson(lesson);
            studentLesson.setStudentLessonStatus(StudentLessonStatus.INITIAL);

            studentLesson = studentLessonRepository.save(studentLesson);

            if (courseEnrollment.getCurrentLessonId() == null) {
                courseEnrollment.setCurrentLessonId(studentLesson.getId());
            } else {
                if (courseEnrollment.getCurrentLessonId() > studentLesson.getId()) {
                    courseEnrollment.setCurrentLessonId(studentLesson.getId());
                }
            }

            Boolean hasQuiz = false;
            for (Quiz quiz : lesson.getQuizzes()) {
                hasQuiz = true;

                if (quiz.getIsDeleted()) {
                    continue;
                }

                StudentQuiz studentQuiz = new StudentQuiz();
                studentQuiz.setQuiz(quiz);
                studentQuiz.setStudentLesson(studentLesson);
                studentLesson.getStudentQuizzes().add(studentQuiz);
            }

            if (!hasQuiz) {
                studentLesson.setStudentLessonStatus(StudentLessonStatus.COMPLETED);
            }

            courseEnrollment.getStudentLessons().add(studentLesson);
        }

        CourseEnrollment savedCourseEnrollment = courseEnrollmentRepository.save(courseEnrollment);

        return savedCourseEnrollment.getId();
    }
    @Override
    public CourseEnrollmentResponse getCourseEnrollmentResponse(Long id) {

        CourseEnrollment courseEnrollment = findCourseEnrollmentById(id);
        Course course = courseEnrollment.getCourse();



        CourseEnrollmentResponse courseEnrollmentResponse = new CourseEnrollmentResponse();
        courseEnrollmentResponse.setId(courseEnrollment.getId());
        courseEnrollmentResponse.setCurrentLessonId(courseEnrollment.getCurrentLessonId());
        courseEnrollmentResponse.setCategoryId(courseEnrollment.getCourse().getCategory().getId());
        courseEnrollmentResponse.setCategoryName(courseEnrollment.getCourse().getCategory().getTitle());
        courseEnrollmentResponse.setIsReviewed(courseEnrollment.getReview() != null);
        courseEnrollmentResponse.setIsCourseCompleted(courseEnrollment.getIsCompleted());
        List<ChapterResponse> chapterResponses = mapChaptersToChapterResponses(course.getChapters(),courseEnrollment);
        courseEnrollmentResponse.setChapterResponses(chapterResponses);

        return courseEnrollmentResponse;
    }

    @Override
    public List<CourseResponse> getEnrolledCourses(String email) {

        List<Course> courses = courseRepository.findByCourseEnrollmentsStudentEmail(email);
        return courseMapper.courseListToCourseResponseList(courses);
    }

    @Override
    public List<CourseEnrollmentIds> getEnrolledCoursesWithIds(String email) {
        List<CourseEnrollment> courseEnrollments = courseEnrollmentRepository.findByStudentEmail(email);

        List<CourseEnrollmentIds> courseEnrollmentIdsList = new ArrayList<>();
        for (CourseEnrollment courseEnrollment:courseEnrollments){
            CourseEnrollmentIds courseEnrollmentIds = new CourseEnrollmentIds();
            courseEnrollmentIds.setCourse_enrollment_id(courseEnrollment.getId());
            courseEnrollmentIds.setCourse_id(courseEnrollment.getCourse().getId());
            courseEnrollmentIdsList.add(courseEnrollmentIds);
        }

        return courseEnrollmentIdsList;
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


    private List<ChapterResponse> mapChaptersToChapterResponses(List<Chapter> chapters, CourseEnrollment courseEnrollment) {
        List<ChapterResponse> chapterResponses = new ArrayList<>();

        for (Chapter chapter : chapters) {
            if (!hasStudentLessons(chapter, courseEnrollment)) {
                continue;
            }

            ChapterResponse chapterResponse = new ChapterResponse();
            chapterResponse.setId(chapter.getId());
            chapterResponse.setTitle(chapter.getTitle());
            chapterResponse.setContainsChapters(chapter.getContainsChapters());

            if (chapter.getContainsChapters()) {
                List<ChapterResponse> subChapterResponses = mapChaptersToChapterResponses(chapter.getChildChapters(), courseEnrollment);
                if (!subChapterResponses.isEmpty()) {
                    chapterResponse.setChapterResponses(subChapterResponses);
                }
            } else {
                List<StudentLessonStatusResponse> studentLessonStatusResponses = studentLessonRepository
                        .findByLessonChapterAndCourseEnrollment(chapter, courseEnrollment)
                        .stream()
                        .map(lessonMapper::studentLessonToStudentLessonStatusResponse)
                        .collect(Collectors.toList());
                chapterResponse.setStudentLessonStatusResponses(studentLessonStatusResponses);
            }

            if (!chapterResponse.getStudentLessonStatusResponses().isEmpty() || chapterResponse.getChapterResponses() != null && !chapterResponse.getChapterResponses().isEmpty()) {
                chapterResponses.add(chapterResponse);
            }
        }

        return chapterResponses;
    }

    private boolean hasStudentLessons(Chapter chapter, CourseEnrollment courseEnrollment) {
        if (chapter.getContainsChapters()) {
            return chapter.getChildChapters().stream()
                    .anyMatch(subChapter -> hasStudentLessons(subChapter, courseEnrollment));
        } else {
            return !studentLessonRepository.findByLessonChapterAndCourseEnrollment(chapter, courseEnrollment).isEmpty();
        }
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

    private Student findStudentByEmail(String email){
        return studentRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Student not found with this email: "+email)
        );

    }



}