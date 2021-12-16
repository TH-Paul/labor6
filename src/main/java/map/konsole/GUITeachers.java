package map.konsole;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import map.Main;
import map.controller.CourseController;
import map.controller.StudentController;
import map.controller.TeacherController;
import map.model.Course;
import map.model.Student;
import map.model.Teacher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUITeachers implements Initializable {

    private static Teacher teacher;
    private static StudentController studentController;
    private static TeacherController teacherController;

    public static Teacher getTeacher() {
        return teacher;
    }

    public static void setTeacher(Teacher teacher) {
        GUITeachers.teacher = teacher;
    }

    public static StudentController getStudentController() {
        return studentController;
    }

    public static void setStudentController(StudentController studentController) {
        GUITeachers.studentController = studentController;
    }

    public static TeacherController getTeacherController() {
        return teacherController;
    }

    public static void setTeacherController(TeacherController teacherController) {
        GUITeachers.teacherController = teacherController;
    }

    @FXML
    private TableColumn<Student, Integer> studentId;

    @FXML
    private TableColumn<Student, String> studentName;

    @FXML
    private TableColumn<Student, String> studentVorname;

    @FXML
    private TableView<Student> students;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teacher = GUI.getTeacherController().findById((GUI.getThisTeacherId()));


        studentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentVorname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        ObservableList<Student> items = FXCollections.observableArrayList();
        items.addAll(studentsEnrolledToCourse(courseIdFromTeacher()));
        students.setItems(items);
    }

    public int courseIdFromTeacher(){
        for(Course c : GUI.getCourseController().obtainObjects()){
            if(c.getTeacherId() != null) {
                if (c.getTeacherId() == teacher.getTeacherId()) {
                    return c.getCourseId();
                }
            }
        }
        return 0;
    }

    public List<Student> studentsEnrolledToCourse(int id){
        List<Student> students = new ArrayList<>();
        for(Student s : GUI.getStudentController().obtainObjects()){
            if(s.getEnrolledCourses().contains(id)){
                students.add(s);
            }
        }
        return students;
    }

    @FXML
    void refresh() {
        ObservableList<Student> items = FXCollections.observableArrayList();
        items.addAll(studentsEnrolledToCourse(courseIdFromTeacher()));
        students.setItems(items);
    }

    @FXML
    void returnToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startmenu-view.fxml"));
        Parent root = loader.load();
        Main.getStage().getScene().setRoot(root);
    }

}
