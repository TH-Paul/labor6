package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Person{

    @JsonIgnore
    private List<Course> courses;


    public Teacher() {
        super();
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
        this.courses = new ArrayList<>();
    }

    public Teacher(String firstName, String lastName, List<Course> courses) {
        super(firstName, lastName);
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }


    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher teacher)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(courses, teacher.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), courses);
    }


    /**
     *
     * @return list with the names of the courses
     */
    public List<String> getCoursesNames(){
        List<String> coursesList = new ArrayList<>();
        for (Course c : courses){
            String name = c.getName();
            coursesList.add(name);
        }
        return coursesList;
    }

    @Override
    public String toString() {

        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", courses=" + getCoursesNames() +
                '}';
    }

    public String showTeacher(){
        return '\n' + this.wholeName() + '\n' + "Courses: " + getCoursesNames() + '\n';
    }
}
