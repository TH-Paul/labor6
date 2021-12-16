package map.konsole;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import map.Main;
import map.controller.CourseController;
import map.controller.StudentController;
import map.exceptions.CreditsLimit;
import map.exceptions.FullCourse;
import map.model.Course;
import map.model.Student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIRegisterStudent implements Initializable {
    private Student student;
    private CourseController courseController;
    private StudentController studentController;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentController getStudentController() {
        return studentController;
    }

    public void setStudentController(StudentController studentController) {
        this.studentController = studentController;
    }

    public CourseController getCourseController() {
        return courseController;
    }

    public void setCourseController(CourseController courseController) {
        this.courseController = courseController;
    }

    @FXML
    private TableColumn<Course, Integer> courseCredits;

    @FXML
    private TableColumn<Course, Integer> courseId;

    @FXML
    private TableColumn<Course, Integer> courseMaxEnrollment;

    @FXML
    private TableColumn<Course, String> courseName;

    @FXML
    private TableView<Course> courses;

    @FXML
    private TextField registerCourseId;

    @FXML
    private Label registerInfo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        courseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseMaxEnrollment.setCellValueFactory(new PropertyValueFactory<>("maxEnrollment"));
        courseCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        ObservableList<Course> items = FXCollections.observableArrayList();
        items.addAll(GUIStudents.getCourseController().obtainObjects());
        courses.setItems(items);

    }

    @FXML
    void goToStudentsMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentmenu-view.fxml"));
        Parent root = loader.load();
        Main.getStage().getScene().setRoot(root);
    }

    @FXML
    void register() throws IOException {
        studentController = GUIStudents.getStudentController();
        courseController = GUIStudents.getCourseController();

        if(!GUI.isNumeric(registerCourseId.getText())){
            registerInfo.setText("Enter a valid ID!");
            return;
        }

        if(courseController.findById(Integer.parseInt(registerCourseId.getText())) == null){
            registerInfo.setText("There is no course with the given ID!");
            return;
        }

        try{
            boolean registered = studentController.register(Integer.parseInt(registerCourseId.getText()), (int) student.getStudentId());
            if(!registered){
                registerInfo.setText("You are already enrolled to this course!");
            }
            else{
                registerInfo.setText("Succes!");
            }
        }
        catch(FullCourse e){
            registerInfo.setText("The course already has the maximum number of participants!");
        }
        catch(CreditsLimit e){
            registerInfo.setText("You cannot have more than 30 credits!");
        }
    }

}
