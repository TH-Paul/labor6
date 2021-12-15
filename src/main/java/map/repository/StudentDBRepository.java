package map.repository;

import map.model.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDBRepository extends DBRepository<Student>{
    public StudentDBRepository() {
        super();
    }

    @Override
    public Student create(Student obj){
        Connection con = openConnection();

        String firstName = obj.getFirstName();
        String lastName = obj.getLastName();
        int matrNr = (int) obj.getStudentId();

        String insertSql = "INSERT INTO student"
                + " VALUES('" + matrNr + "', '" + firstName + "', '" + lastName + "', "
                + 0 + ")";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

        return obj;
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();

        Connection con = openConnection();

        String selectSql = "SELECT * FROM student";

        try(Statement stmt = con.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(selectSql);
            while (resultSet.next()) {
                Student student = new Student();
                student.setFirstName(resultSet.getString("firstName"));
                student.setLastName(resultSet.getString("lastName"));
                student.setStudentId(resultSet.getInt("studentId"));
                student.setTotalCredits(resultSet.getInt("totalCredits"));
                student.setEnrolledCourses(new ArrayList<>());
                String selectSql2 = "SELECT * FROM registered_students WHERE studentId='" + student.getStudentId() + "'";

                Statement stmt2 = con.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery(selectSql2);
                while(resultSet2.next()){
                    student.getEnrolledCourses().add(resultSet2.getInt("courseId"));
                }
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

        return students;
    }

    @Override
    public Student update(Student obj) {
        return null;
    }

    @Override
    public void delete(Student obj){
        Connection con = openConnection();

        int matrNr = (int) obj.getStudentId();

        String deleteSql = "delete from student"
                + " where studentId='" + matrNr + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(deleteSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);
    }

    public Student getStudent(int id){
        Connection con = openConnection();

        String selectSql = "SELECT * FROM student where studentId='" + id + "'";

        Student returnedStudent = null;

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectSql);

            while (resultSet.next()) {
                Student student = new Student();
                student.setStudentId(resultSet.getInt("studentId"));
                student.setFirstName(resultSet.getString("firstName"));
                student.setLastName(resultSet.getString("lastName"));
                student.setTotalCredits(resultSet.getInt("totalCredits"));
                student.setEnrolledCourses(new ArrayList<>());
                String selectSql2 = "SELECT * FROM registered_students WHERE studentId='" + student.getStudentId() + "'";

                Statement stmt2 = con.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery(selectSql2);
                while(resultSet2.next()){
                    student.getEnrolledCourses().add(resultSet2.getInt("courseId"));
                }
                returnedStudent = student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

        return returnedStudent;
    }
}
