package map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import map.model.Student;
import map.model.Teacher;

import java.io.IOException;

public class Main extends Application {

//    public static void main(String[] args) throws IOException{
//        ICrudRepository<Teacher> teacherRepo = new TeacherFileRepository(new File("teachers.json"));
//        ICrudRepository<Student> studentRepo = new StudentFileRepository(new File("students.json"));
//        ICrudRepository<Course> courseRepo = new CourseFileRepository(new File("courses.json"));
//
//        TeacherDBRepository teacherRepo1 = new TeacherDBRepository();
//        StudentDBRepository studentRepo1 = new StudentDBRepository();
//        CourseDBRepository courseRepo1 = new CourseDBRepository();
//        CommunicationDBRepository communicationDBRepository = new CommunicationDBRepository(courseRepo1
//                , studentRepo1, teacherRepo1);
//
//        StudentController studentController = new StudentController(studentRepo, courseRepo);
//        TeacherController teacherController = new TeacherController(teacherRepo, courseRepo);
//        CourseController courseController = new CourseController(courseRepo, teacherRepo, studentRepo);
//
//        StudentController studentController1 = new StudentController(studentRepo1, communicationDBRepository);
//        TeacherController teacherController1 = new TeacherController(teacherRepo1, communicationDBRepository);
//        CourseController courseController1 = new CourseController(courseRepo1, communicationDBRepository);
//
//        UserInterface userInterface = new UserInterface(courseController1, studentController1, teacherController1);
//
//        userInterface.runMenu();
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("startmenu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 746, 560);
        primaryStage.setTitle("University");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(pane);

    }

    public static void main(String[] args){

        launch();
    }

}
