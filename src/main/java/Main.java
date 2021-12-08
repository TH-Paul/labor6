import controller.CourseController;
import controller.StudentController;
import controller.TeacherController;
import konsole.UserInterface;
import repository.CourseFileRepository;
import repository.StudentFileRepository;
import repository.TeacherFileRepository;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TeacherFileRepository teacherRepo = new TeacherFileRepository(new File("teachers.json"));
        StudentFileRepository studentRepo = new StudentFileRepository(new File("students.json"));
        CourseFileRepository courseRepo = new CourseFileRepository(new File("courses.json"), teacherRepo, studentRepo);


        StudentController studentController = new StudentController(studentRepo, courseRepo);
        TeacherController teacherController = new TeacherController(teacherRepo, courseRepo);
        CourseController courseController = new CourseController(courseRepo, teacherRepo, studentRepo);

        UserInterface userInterface = new UserInterface(courseController, studentController, teacherController);

        userInterface.runMenu();

    }
}
