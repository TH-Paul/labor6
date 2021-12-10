package controller;

import exceptions.CreditsLimit;
import exceptions.FullCourse;
import model.Course;
import model.Person;
import model.Student;
import repository.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentController extends AbstractController<Student>{

    private ICrudRepository<Course> courseRepo;

    public StudentController(ICrudRepository<Student> repository, ICrudRepository<Course> courseRepo) {
        super(repository);
        this.courseRepo = courseRepo;
    }

    public StudentController(ICrudRepository<Student> repository, CommunicationDBRepository communicationDBRepository) {
        super(repository, communicationDBRepository);
    }

    public List<Student> sortStudentsByName(){
        List<Student> students = new ArrayList<>(obtainObjects());
        students.sort(Comparator.comparing(Person::getLastName));
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
    public void create(Student object) throws IOException{
        this.repository.create(object);
    }

    /**
     * deletes the student -> the student will be deleted from the course's list of students
     * @param object - to be deleted
     */
    @Override
    public void delete(Student object) throws IOException{

        if(this.repository instanceof DBRepository){
            communicationDBRepository.deleteStudentEffect(object);
            this.repository.delete(object);
        }

        if(this.repository instanceof InMemoryRepository) {
            this.repository.delete(object);

            for (Course course : courseRepo.getAll()) {
                course.getStudentsEnrolled().removeIf(studentId -> studentId == object.getStudentId());
            }

            if (courseRepo instanceof FileRepository) {
                ((FileRepository<Course>) courseRepo).writeToFile();
            }
        }
    }

    public boolean register(int courseId, int studentId) throws IOException {

        if(repository instanceof InMemoryRepository) {
            Course course = ((InMemoryRepository<Course>) courseRepo).findById(courseId);
            Student registeredStudent = ((InMemoryRepository<Student>) this.repository).findById(studentId);
            if (registeredStudent.getTotalCredits() + course.getCredits() > 30) {
                throw new CreditsLimit("Too much credits");
            }
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() == 0) {
                throw new FullCourse("No more places!");
            }

            for (int c : registeredStudent.getEnrolledCourses()) {
                if (c == course.getCourseId()) {
                    return false;
                }
            }

            course.getStudentsEnrolled().add(studentId);
            registeredStudent.getEnrolledCourses().add(courseId);
            registeredStudent.setTotalCredits(registeredStudent.getTotalCredits() + course.getCredits());

            if (courseRepo instanceof FileRepository && this.repository instanceof FileRepository) {
                ((FileRepository<Course>) courseRepo).writeToFile();
                ((FileRepository<Student>) this.repository).writeToFile();
            }
        }

        else{
            Course course = communicationDBRepository.getCourseDBRepository().getCourse(courseId);
            Student registeredStudent = communicationDBRepository.getStudentDBRepository().getStudent(studentId);

            if (registeredStudent.getTotalCredits() + course.getCredits() > 30) {
                throw new CreditsLimit("Too much credits");
            }
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() == 0) {
                throw new FullCourse("No more places!");
            }

            for (int c : registeredStudent.getEnrolledCourses()) {
                if (c == course.getCourseId()) {
                    return false;
                }
            }

            communicationDBRepository.registerStudent(courseId, studentId);

        }
        return true;
    }

    @Override
    public Student findById(int id) {

        if(repository instanceof DBRepository){
            return communicationDBRepository.getStudentDBRepository().getStudent(id);
        }

        if(repository instanceof InMemoryRepository) {
            return ((InMemoryRepository<Student>) this.repository).findById(id);
        }
        return null;
    }
}
