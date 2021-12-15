package map.repository;

import map.model.Course;
import map.model.Student;
import map.model.Teacher;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CommunicationDBRepository{
    private CourseDBRepository courseDBRepository;
    private StudentDBRepository studentDBRepository;
    private TeacherDBRepository teacherDBRepository;

    public CommunicationDBRepository() {
    }

    public CommunicationDBRepository(CourseDBRepository courseDBRepository, StudentDBRepository studentDBRepository, TeacherDBRepository teacherDBRepository) {
        this.courseDBRepository = courseDBRepository;
        this.studentDBRepository = studentDBRepository;
        this.teacherDBRepository = teacherDBRepository;
    }

    public CourseDBRepository getCourseDBRepository() {
        return courseDBRepository;
    }

    public void setCourseDBRepository(CourseDBRepository courseDBRepository) {
        this.courseDBRepository = courseDBRepository;
    }

    public StudentDBRepository getStudentDBRepository() {
        return studentDBRepository;
    }

    public void setStudentDBRepository(StudentDBRepository studentDBRepository) {
        this.studentDBRepository = studentDBRepository;
    }

    public TeacherDBRepository getTeacherDBRepository() {
        return teacherDBRepository;
    }

    public void setTeacherDBRepository(TeacherDBRepository teacherDBRepository) {
        this.teacherDBRepository = teacherDBRepository;
    }

    public void deleteStudentEffect(Student student){
        Connection con = courseDBRepository.openConnection();

        String deleteSql = "DELETE FROM registered_students where studentId='" + student.getStudentId() + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(deleteSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        courseDBRepository.closeConnection(con);
    }

    public void deleteTeacherEffect(Teacher teacher){
        Connection con = courseDBRepository.openConnection();

        String updateSql = "UPDATE course SET teacherId = null where teacherId='" + teacher.getTeacherId() + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        courseDBRepository.closeConnection(con);
    }

    public void deleteCourseEffect(Course course){
        Connection con = courseDBRepository.openConnection();

        String updateSql = "UPDATE student SET totalCredits=totalCredits - '" + course.getCredits()
                + "' WHERE studentId IN (SELECT studentId FROM registered_students WHERE courseId='"
                + course.getCourseId() + "')";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String deleteSql = "DELETE FROM registered_students where courseId='" + course.getCourseId() + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(deleteSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        courseDBRepository.closeConnection(con);
    }

    public void registerStudent(int courseId, int studentId){
        Connection con = courseDBRepository.openConnection();

        Course course = courseDBRepository.getCourse(courseId);

        String insertSql = "INSERT INTO registered_students"
                + " VALUES('" + studentId + "', '" + courseId + "')";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String updateSql = "UPDATE student SET totalCredits=totalCredits + '" + course.getCredits()
                + "' WHERE studentId='" + studentId + "'";

        try {
            Statement stmt2 = con.createStatement();
            stmt2.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        courseDBRepository.closeConnection(con);
    }

    public void assignTeacher(int teacherId, int courseId){
        Connection con = courseDBRepository.openConnection();

        String updateSql = "UPDATE course SET teacherId='" + teacherId
                + "' WHERE courseId='" + courseId + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        courseDBRepository.closeConnection(con);
    }

    public void modifyCourseCredits(int courseId, int newCredits){
        Connection con = courseDBRepository.openConnection();

        int oldCredits = courseDBRepository.getCourse(courseId).getCredits();

        String updateSql = "UPDATE course SET credits='" + newCredits
                + "' WHERE courseId='" + courseId + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String updateSql2 = "UPDATE student SET totalCredits=totalCredits - '" + oldCredits + "'+'" + newCredits
                + "' WHERE studentId IN (SELECT studentId FROM registered_students WHERE courseId='"
                + courseId + "')";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        courseDBRepository.closeConnection(con);
    }

    public void modifyCourseMaxEnrolled(int courseId, int newMaxEnrolled){
        Connection con = courseDBRepository.openConnection();

        String updateSql = "UPDATE course SET maxEnrollment='" + newMaxEnrolled
                + "' WHERE courseId='" + courseId + "'";

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        courseDBRepository.closeConnection(con);
    }

}
