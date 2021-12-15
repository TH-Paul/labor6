package map.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private int courseId;
    private String name;
    private Integer teacherId;
    private int maxEnrollment;
    private int credits;
    private List<Integer> studentsEnrolled;

    public Course() {
    }

    public Course(int courseId, String name, int maxEnrollment, int credits){
        this.courseId = courseId;
        this.name = name;
        this.teacherId = null;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
        this.studentsEnrolled = new ArrayList<>();


    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Integer> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Integer> studentsEnrolled) {
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
        return courseId == course.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                ", maxEnrollment=" + maxEnrollment +
                ", credits=" + credits +
                ", studentsEnrolled=" + studentsEnrolled +
                '}';
    }

}
