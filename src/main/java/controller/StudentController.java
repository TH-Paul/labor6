package controller;

import exceptions.CreditsLimit;
import exceptions.FullCourse;
import model.Course;
import model.Student;
import repository.CourseFileRepository;
import repository.FileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentController extends AbstractController<Student>{

    private final CourseFileRepository courseRepo;

    public StudentController(FileRepository<Student> repository, CourseFileRepository courseRepo) {
        super(repository);
        this.courseRepo = courseRepo;
    }



    public List<Student> sortStudentsById(){
        List<Student> students = new ArrayList<>(obtainObjects());
        students.sort(Comparator.comparing(Student::getStudentId));
        return students;
    }

    public List<Student> filterStudentsByCredits(int credits){
        List<Student> filteredList = new ArrayList<>();
        for(Student student : obtainObjects()){
            if(student.getTotalCredits() < credits){
                filteredList.add(student);
            }
        }
        return filteredList;
    }

    /**
     * adds a student to the repo
     * @param object - Student
     */
    @Override
    public void create(Student object) throws IOException {
        this.repository.create(object);
    }

    /**
     * deletes the student -> the student will be deleted from the course's list of students
     * @param object - to be deleted
     */
    @Override
    public void delete(Student object) throws IOException {
        this.repository.delete(object);

        for(Course course : courseRepo.getAll()){
//            for(Student student : course.getStudentsEnrolled()){
//                if(student.wholeName().equals(object.wholeName())){
//                    course.getStudentsEnrolled().remove(student);
//                }
//            }
            course.getStudentsEnrolled().removeIf(student -> student.getStudentId() == object.getStudentId());
        }

        courseRepo.writeToFile();
    }

    public boolean register(Course course, Student student) throws IOException {
        if(student.getTotalCredits() + course.getCredits() > 30){
            throw new CreditsLimit("Too much credits");
        }
        if(course.getMaxEnrollment() - course.getStudentsEnrolled().size() == 0){
            throw new FullCourse("No more places!");
        }

        for(Course c : student.getEnrolledCourses()){
            if(c.getName().equals(course.getName())){
                return false;
            }
        }

        course.getStudentsEnrolled().add(student);
        student.getEnrolledCourses().add(course);
        student.setTotalCredits(student.getTotalCredits() + course.getCredits());

        courseRepo.writeToFile();
        this.repository.writeToFile();


        return true;
    }
}
