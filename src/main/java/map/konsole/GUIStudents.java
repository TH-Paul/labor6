package map.konsole;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import map.Main;
import map.controller.CourseController;
import map.controller.StudentController;
import map.controller.TeacherController;
import map.model.Course;
import map.model.Student;

import java.io.IOException;

public class GUIStudents {

    private Student student;
    private static StudentController studentController;
    private static CourseController courseController;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public static StudentController getStudentController() {
        return studentController;
    }

    public static void setStudentController(StudentController studentController) {
        GUIStudents.studentController = studentController;
    }

    public static CourseController getCourseController() {
        return courseController;
    }

    public static void setCourseController(CourseController courseController) {
        GUIStudents.courseController = courseController;
    }

    @FXML
    private Label creditsNumber;

    @FXML
    void goToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startmenu-view.fxml"));
        Parent root = loader.load();
        Main.getStage().getScene().setRoot(root);
    }

    @FXML
    void registerStudent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/registerstudent-view.fxml"));
        Parent root = loader.load();
        GUIRegisterStudent gui = loader.getController();
        gui.setStudent(student);
        Main.getStage().getScene().setRoot(root);

    }

    @FXML
    void showCredits() {
        creditsNumber.setText("Credits: " + student.getTotalCredits());
    }
}
