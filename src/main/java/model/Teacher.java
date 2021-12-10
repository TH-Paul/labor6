package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Person{

    private int teacherId;
    private List<Integer> courses;


    public Teacher() {
        super();
    }

    public Teacher(String firstName, String lastName, int teacherId) {
        super(firstName, lastName);
        this.teacherId = teacherId;
        this.courses = new ArrayList<>();
    }

    public Teacher(String firstName, String lastName, int teacherId, List<Integer> courses) {
        super(firstName, lastName);
        this.teacherId = teacherId;
        this.courses = courses;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public List<Integer> getCourses() {
        return courses;
    }

    public void setCourses(List<Integer> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher teacher)) return false;
        if (!super.equals(o)) return false;
        return teacherId == teacher.teacherId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teacherId);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", teacherId=" + teacherId +
                ", courses=" + courses +
                '}';
    }
}
