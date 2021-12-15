package map;

import map.controller.CourseController;
import map.controller.StudentController;
import map.controller.TeacherController;
import map.konsole.UserInterface;
import map.model.Course;
import map.model.Student;
import map.model.Teacher;
import map.repository.*;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        ICrudRepository<Teacher> teacherRepo = new TeacherFileRepository(new File("teachers.json"));
        ICrudRepository<Student> studentRepo = new StudentFileRepository(new File("students.json"));
        ICrudRepository<Course> courseRepo = new CourseFileRepository(new File("courses.json"));

        TeacherDBRepository teacherRepo1 = new TeacherDBRepository();
        StudentDBRepository studentRepo1 = new StudentDBRepository();
        CourseDBRepository courseRepo1 = new CourseDBRepository();
        CommunicationDBRepository communicationDBRepository = new CommunicationDBRepository(courseRepo1
                , studentRepo1, teacherRepo1);

        StudentController studentController = new StudentController(studentRepo, courseRepo);
        TeacherController teacherController = new TeacherController(teacherRepo, courseRepo);
        CourseController courseController = new CourseController(courseRepo, teacherRepo, studentRepo);

        StudentController studentController1 = new StudentController(studentRepo1, communicationDBRepository);
        TeacherController teacherController1 = new TeacherController(teacherRepo1, communicationDBRepository);
        CourseController courseController1 = new CourseController(courseRepo1, communicationDBRepository);

        UserInterface userInterface = new UserInterface(courseController1, studentController1, teacherController1);

        userInterface.runMenu();

    }
}
