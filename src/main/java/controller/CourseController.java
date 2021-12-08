package controller;

import exceptions.InvalidCourseChange;
import model.Course;
import model.Student;
import model.Teacher;
import repository.FileRepository;
import repository.StudentFileRepository;
import repository.TeacherFileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CourseController extends AbstractController<Course>{

    private final TeacherFileRepository teacherRepo;
    private final StudentFileRepository studentRepo;

    public CourseController(FileRepository<Course> repository, TeacherFileRepository teacherRepo, StudentFileRepository studentRepo) {
        super(repository);
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
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
    public void create(Course object) throws IOException {
        this.repository.create(object);
    }

    /**
     * deletes the course -> the course will be removed from teacher and student courses list too
     * @param object - to be deleted
     */
    @Override
    public void delete(Course object) throws IOException {
        this.repository.delete(object);

        for(Teacher teacher : teacherRepo.getAll()){
           if(teacher == object.getTeacher()){
               teacher.getCourses().remove(object);
           }
        }

        for(Student student : studentRepo.getAll()){
//            for(Course course : student.getEnrolledCourses()){
//                if (course == object){
//                    student.getEnrolledCourses().remove(object);
//                    student.setTotalCredits(student.getTotalCredits() - object.getCredits());
//                }
//            }
            student.setTotalCredits(student.getTotalCredits() - object.getCredits());
            student.getEnrolledCourses().removeIf(course -> course.getName().equals(object.getName()));
        }

        studentRepo.writeToFile();
        teacherRepo.writeToFile();
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

    public void assignTeacherForCourse(Teacher teacher, Course course) throws IOException {
        Teacher oldTeacher = new Teacher();
        for(Course c : this.repository.getAll()){
            if(c == course){
                if(c.getTeacher() != null){
                    oldTeacher = c.getTeacher();
                }
                course.setTeacher(teacher);
                break;
            }
        }
        teacher.getCourses().add(course);

        for(Teacher teacher1 : teacherRepo.getAll()){
            if(teacher1 == oldTeacher){
                teacher1.getCourses().remove(course);
                break;
            }
        }

        this.repository.writeToFile();
        teacherRepo.writeToFile();
    }

    public void modifyCourseCredits(Course modifiedCourse, int newCredits) throws IOException {
        for(Student student : studentRepo.getAll()){
            for(Course course : student.getEnrolledCourses()){
                if (course == modifiedCourse){
                    student.setTotalCredits(student.getTotalCredits() - modifiedCourse.getCredits() + newCredits);
                    break;
                }
            }
        }

//        for(Course course : this.obtainObjects()){
//            if(course == modifiedCourse){
//                course.setCredits(newCredits);
//                break;
//            }
//        }

        modifiedCourse.setCredits(newCredits);
        this.repository.writeToFile();
        studentRepo.writeToFile();
    }

    public void modifyCourseMaxEnrolled(Course modifiedCourse, int newMaxEnrolled) throws IOException {
//            for(Course course : this.obtainObjects()){
//                if(course == modifiedCourse){
//                    course.setMaxEnrollment(newMaxEnrolled);
//                    break;
//                }
//            }
        if(newMaxEnrolled < modifiedCourse.getStudentsEnrolled().size()){
            throw new InvalidCourseChange("There are already more students enrolled!");
        }
        modifiedCourse.setMaxEnrollment(newMaxEnrolled);
        this.repository.writeToFile();
    }

}
