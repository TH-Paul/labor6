package repository;

import model.Teacher;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TeacherDBRepository extends DBRepository<Teacher>{
    public TeacherDBRepository() {
        super();
    }

    @Override
    public Teacher create(Teacher obj){
        Connection con = openConnection();

        int teacherId = obj.getTeacherId();
        String firstName = obj.getFirstName();
        String lastName = obj.getLastName();

        String insertSql = "INSERT INTO teacher"
                + " VALUES('" + teacherId + "', '" + firstName + "', '" + lastName + "')";

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
    public List<Teacher> getAll() {
        List<Teacher> teachers = new ArrayList<>();

        Connection con = openConnection();

        try {
            Statement stmt = con.createStatement();
            String selectSql = "SELECT * FROM teacher";
            ResultSet resultSet = stmt.executeQuery(selectSql);

            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(resultSet.getInt("teacherId"));
                teacher.setFirstName(resultSet.getString("firstName"));
                teacher.setLastName(resultSet.getString("lastName"));
                teacher.setCourses(new ArrayList<>());
                String selectSql2 = "SELECT * FROM course WHERE teacherId='" + teacher.getTeacherId() + "'";

                Statement stmt2 = con.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery(selectSql2);
                while(resultSet2.next()){
                    teacher.getCourses().add(resultSet2.getInt("courseId"));
                }

                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

        return teachers;
    }

    @Override
    public Teacher update(Teacher obj){
        return null;
    }

    @Override
    public void delete(Teacher obj){
        Connection con = openConnection();

        int teacher = obj.getTeacherId();

        String deleteSql = "delete from teacher"
                + " where teacherId='" + teacher + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(deleteSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);
    }

    public Teacher getTeacher(int id){
        Connection con = openConnection();

        String selectSql = "SELECT * FROM teacher where teacherId='" + id + "'";

        Teacher returnedTeacher = null;

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectSql);

            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(resultSet.getInt("teacherId"));
                teacher.setFirstName(resultSet.getString("firstName"));
                teacher.setLastName(resultSet.getString("lastName"));
                teacher.setCourses(new ArrayList<>());
                String selectSql2 = "SELECT * FROM course WHERE teacherId='" + teacher.getTeacherId() + "'";

                Statement stmt2 = con.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery(selectSql2);
                while(resultSet2.next()){
                    teacher.getCourses().add(resultSet2.getInt("courseId"));
                }
                returnedTeacher = teacher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

        return returnedTeacher;
    }
}
