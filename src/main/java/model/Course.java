package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private String name;

    @JsonIgnore
    private Teacher teacher;
    private int maxEnrollment;
    private int credits;

    @JsonIgnore
    private List<Student> studentsEnrolled;

    public Course() {
    }

    public Course(String name, int maxEnrollment, int credits){
        this.name = name;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
        this.teacher = null;
        this.studentsEnrolled = new ArrayList<>();


    }

    public Course(String name, Teacher teacher, int maxEnrollment, List<Student> studentsEnrolled, int credits) {
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled = studentsEnrolled;
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Student> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Student> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return maxEnrollment == course.maxEnrollment && credits == course.credits && Objects.equals(name, course.name) && Objects.equals(teacher, course.teacher) && Objects.equals(studentsEnrolled, course.studentsEnrolled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teacher, maxEnrollment, studentsEnrolled, credits);
    }

    /**
     *
     * @return list with the names of the students
     */
    public List<String> getStudentsNamesAndIds(){
        List<String> studentsList = new ArrayList<>();
        for (Student s : studentsEnrolled){
            String name = s.obtainNameAndId();
            studentsList.add(name);
        }
        return studentsList;
    }

    public String getTeacherName(){
        if(teacher != null) {
            return teacher.getLastName() + " " + teacher.getFirstName();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", teacherName='" + getTeacherName() + '\'' +
                ", maxEnrollment=" + maxEnrollment +
                ", studentsEnrolled=" + getStudentsNamesAndIds() +
                ", credits=" + credits +
                '}';
    }

    public String showCourse(){
        return '\n' + name + '\n' + "Teacher: " + getTeacherName() + '\n' + "Maximum number of students: " +
                maxEnrollment + '\n' + "Enrolled Students: " + getStudentsNamesAndIds() + '\n' +
                "Credits: " + credits + '\n';
    }
}
