package repository;

import model.Course;
import model.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDBRepository extends DBRepository<Course>{

    public CourseDBRepository() {
        super();
    }

    @Override
    public Course create(Course obj){
        Connection con = openConnection();

        int courseId = obj.getCourseId();
        String name = obj.getName();
        int maxEnrollment = obj.getMaxEnrollment();
        int credits = obj.getCredits();

        String insertSql = "INSERT INTO course(courseId, name, maxEnrollment, credits)"
                + " VALUES('" + courseId + "', '" + name + "', '" + maxEnrollment + "', "
                + credits + ")";

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
    public List<Course> getAll() {
        List<Course> courses = new ArrayList<>();

        Connection con = openConnection();

        String selectSql = "SELECT * FROM course";

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectSql);

            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("courseId"));
                course.setName(resultSet.getString("name"));
                course.setTeacherId(resultSet.getObject("teacherId", Integer.class));
                course.setMaxEnrollment(resultSet.getInt("maxEnrollment"));
                course.setCredits(resultSet.getInt("credits"));
                course.setStudentsEnrolled(new ArrayList<>());
                String selectSql2 = "SELECT * FROM registered_students WHERE courseId='" + course.getCourseId() + "'";

                Statement stmt2 = con.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery(selectSql2);
                while(resultSet2.next()){
                    course.getStudentsEnrolled().add(resultSet2.getInt("studentId"));
                }
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

        return courses;
    }

    @Override
    public Course update(Course obj) {
        return null;
    }

    @Override
    public void delete(Course obj){

        Connection con = openConnection();

        int id = obj.getCourseId();

        String deleteSql = "delete from course"
                + " where courseId='" + id + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(deleteSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

    }

    public Course getCourse(int id){
        Connection con = openConnection();

        String selectSql = "SELECT * FROM course where courseId='" + id + "'";

        Course returnedCourse = null;

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectSql);

            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("courseId"));
                course.setName(resultSet.getString("name"));
                course.setTeacherId(resultSet.getInt("teacherId"));
                course.setMaxEnrollment(resultSet.getInt("maxEnrollment"));
                course.setCredits(resultSet.getInt("credits"));
                course.setStudentsEnrolled(new ArrayList<>());
                String selectSql2 = "SELECT * FROM registered_students WHERE courseId='" + course.getCourseId() + "'";

                Statement stmt2 = con.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery(selectSql2);
                while(resultSet2.next()){
                    course.getStudentsEnrolled().add(resultSet2.getInt("studentId"));
                }
                returnedCourse = course;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

        return returnedCourse;
    }
}
