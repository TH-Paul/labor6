package controller;

import exceptions.InvalidCourseChange;
import model.Course;
import model.Student;
import model.Teacher;
import repository.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CourseController extends AbstractController<Course>{

    private ICrudRepository<Teacher> teacherRepo;
    private ICrudRepository<Student> studentRepo;

    public CourseController(ICrudRepository<Course> repository, ICrudRepository<Teacher> teacherRepo, ICrudRepository<Student> studentRepo) {
        super(repository);
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
    }

    public CourseController(ICrudRepository<Course> repository, CommunicationDBRepository communicationDBRepository) {
        super(repository, communicationDBRepository);
    }

    public List<Course> sortCoursesByCredits(){
        List<Course> courses = new ArrayList<>(obtainObjects());
        courses.sort(Comparator.comparing(Course::getCredits));
        return courses;
    }

    public List<Course> filterCoursesByEnrolledStudents(int enrolledStudents){
        List<Course> filteredList = new ArrayList<>();
        for(Course course : obtainObjects()){
            if(course.getStudentsEnrolled().size() < enrolledStudents){
                filteredList.add(course);
            }
        }
        return filteredList;
    }

    /**
     * adds a course to the repo
     * @param object - Course
     */
    @Override
    public void create(Course object) throws IOException{
        this.repository.create(object);
    }

    /**
     * deletes the course -> the course will be removed from teacher and student courses list too
     * @param object - to be deleted
     */
    @Override
    public void delete(Course object) throws IOException{

        if(this.repository instanceof DBRepository){
            communicationDBRepository.deleteCourseEffect(object);
            this.repository.delete(object);
        }

        if(this.repository instanceof InMemoryRepository) {
            this.repository.delete(object);
            for (Teacher teacher : teacherRepo.getAll()) {
                if (teacher.getTeacherId() == object.getTeacherId()) {
                    teacher.getCourses().remove(object.getCourseId());
                }
            }

            for (Student student : studentRepo.getAll()) {
//            for(Course course : student.getEnrolledCourses()){
//                if (course == object){
//                    student.getEnrolledCourses().remove(object);
//                    student.setTotalCredits(student.getTotalCredits() - object.getCredits());
//                }
//            }
                student.setTotalCredits(student.getTotalCredits() - object.getCredits());
                student.getEnrolledCourses().removeIf(courseId -> courseId == object.getCourseId());
            }
        }

        if(studentRepo instanceof FileRepository && teacherRepo instanceof FileRepository) {
            ((FileRepository<Student>) studentRepo).writeToFile();
            ((FileRepository<Teacher>) teacherRepo).writeToFile();
        }
    }

    @Override
    public Course findById(int id) {
        if(repository instanceof DBRepository){
            return communicationDBRepository.getCourseDBRepository().getCourse(id);
        }

        if(repository instanceof InMemoryRepository) {
            return ((InMemoryRepository<Course>) this.repository).findById(id);
        }

        return null;
    }


    public List<Course> retrieveCoursesWithFreePlaces(){
        List<Course> availableCourses = new ArrayList<>();
        for(Course course : this.repository.getAll()){
            if(course.getMaxEnrollment() - course.getStudentsEnrolled().size() > 0){
                availableCourses.add(course);
            }
        }

        return availableCourses;
    }

    public void assignTeacherForCourse(int teacherId, int courseId) throws IOException{
        if(repository instanceof InMemoryRepository) {
            for (Course c : this.repository.getAll()) {
                if (c.getCourseId() == courseId) {
                    if (c.getTeacherId() != null) {
                        ((InMemoryRepository<Teacher>) teacherRepo).findById(c.getTeacherId())
                                .getCourses().remove(courseId);
                    }

                    c.setTeacherId(teacherId);

                    ((InMemoryRepository<Teacher>) teacherRepo).findById(teacherId)
                            .getCourses().add(courseId);
                }
            }

            if (this.repository instanceof FileRepository && teacherRepo instanceof FileRepository) {
                ((FileRepository<Course>) this.repository).writeToFile();
                ((FileRepository<Teacher>) teacherRepo).writeToFile();
            }
        }

        if(repository instanceof DBRepository){
            communicationDBRepository.assignTeacher(teacherId, courseId);
        }
    }

    public void modifyCourseCredits(int courseId, int newCredits) throws IOException{

        if(repository instanceof InMemoryRepository) {
            int oldCredits = ((InMemoryRepository<Course>) this.repository).findById(courseId).getCredits();

            for (Student student : studentRepo.getAll()) {
                for (int course : student.getEnrolledCourses()) {
                    if (course == courseId) {
                        student.setTotalCredits(student.getTotalCredits() - oldCredits + newCredits);
                        break;
                    }
                }
            }

            ((InMemoryRepository<Course>) this.repository).findById(courseId).setCredits(newCredits);

            if (this.repository instanceof FileRepository && teacherRepo instanceof FileRepository) {
                ((FileRepository<Course>) this.repository).writeToFile();
                ((FileRepository<Teacher>) teacherRepo).writeToFile();
            }
        }

        if(repository instanceof DBRepository){
            communicationDBRepository.modifyCourseCredits(courseId, newCredits);
        }

    }

    public void modifyCourseMaxEnrolled(int courseId, int newMaxEnrolled) throws IOException {

        if(repository instanceof InMemoryRepository) {
            Course modifiedCourse = ((InMemoryRepository<Course>) this.repository).findById(courseId);

            if (newMaxEnrolled < modifiedCourse.getStudentsEnrolled().size()) {
                throw new InvalidCourseChange("There are already more students enrolled!");
            }

            modifiedCourse.setMaxEnrollment(newMaxEnrolled);

            if (this.repository instanceof FileRepository) {
                ((FileRepository<Course>) this.repository).writeToFile();
            }
        }

        if(repository instanceof DBRepository){
            communicationDBRepository.modifyCourseMaxEnrolled(courseId, newMaxEnrolled);
        }

    }



}
