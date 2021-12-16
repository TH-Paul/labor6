package map.konsole;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.Main;
import map.controller.CourseController;
import map.controller.StudentController;
import map.controller.TeacherController;
import map.model.Student;
import map.model.Teacher;
import map.repository.CommunicationDBRepository;
import map.repository.CourseDBRepository;
import map.repository.StudentDBRepository;
import map.repository.TeacherDBRepository;

import java.io.IOException;

public class GUI {
    private final static TeacherDBRepository teacherRepo = new TeacherDBRepository();
    private final static StudentDBRepository studentRepo = new StudentDBRepository();
    private final static CourseDBRepository courseRepo = new CourseDBRepository();
    private final static CommunicationDBRepository communicationDBRepository = new CommunicationDBRepository(courseRepo
            , studentRepo, teacherRepo);
    private final static StudentController studentController = new StudentController(studentRepo, communicationDBRepository);
    private final static TeacherController teacherController = new TeacherController(teacherRepo, communicationDBRepository);
    private final static CourseController courseController = new CourseController(courseRepo, communicationDBRepository);


    @FXML
    private TextField id;

    @FXML
    private Label logInInfo;

    @FXML
    private TextField userType;

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @FXML
    void userLogIn() throws IOException {
        Main main = new Main();

        if(userType.getText().isEmpty() || id.getText().isEmpty()){
            logInInfo.setText("Please enter your data!");
        }
        else if(!isNumeric(id.getText())){
            logInInfo.setText("Wrong ID! Try again!");
        }
        else if(userType.getText().equals("s") && !id.getText().isEmpty()){
            Student student = studentController.findById(Integer.parseInt(id.getText()));
            if(student == null){
                logInInfo.setText("Wrong ID! Try again!");
            }
            else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentmenu-view.fxml"));
                Parent root = loader.load();
                GUIStudents gui = loader.getController();
                gui.setStudent(student);
                GUIStudents.setStudentController(studentController);
                GUIStudents.setTeacherController(teacherController);
                GUIStudents.setCourseController(courseController);
                Main.getStage().getScene().setRoot(root);
            }
        }

        else if(userType.getText().equals("t") && !id.getText().isEmpty()){
            Teacher teacher = teacherController.findById(Integer.parseInt(id.getText()));
            if(teacher == null){
                logInInfo.setText("Wrong ID! Try again!");
            }
            else{

                main.changeScene("/teachermenu-view.fxml");
            }
        }
        else if(!userType.getText().equals("t") && !userType.getText().equals("s")){
            logInInfo.setText("Select a correct type!");
        }
    }

}
