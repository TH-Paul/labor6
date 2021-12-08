package repository;

import org.junit.jupiter.api.Test;
import model.Course;
import model.Student;
import model.Teacher;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRepositoryTest {

    Teacher teacher1 = new Teacher("Catalin", "Rusu", new ArrayList<>());

    Teacher teacher2 = new Teacher("Diana", "Cristea", new ArrayList<>());

    Course course1 = new Course("MAP", teacher1, 70, new ArrayList<>(), 6);

    Course course2 = new Course("BD", teacher2, 80, new ArrayList<>(), 6);

    Student student1 = new Student("Paul", "Tarta", 456, 0, new ArrayList<>());

    Student student2 = new Student("Andrei", "Silion", 457, 0,new ArrayList<>());

    @Test
    void create() throws IOException {
        TeacherRepository teacherRepo = new TeacherRepository();
        assertEquals(teacherRepo.getAll().size(), 0);

        StudentRepository studentsRepo = new StudentRepository();
        assertEquals(studentsRepo.getAll().size(), 0);

        CourseRepository courseRepo = new CourseRepository();
        assertEquals(courseRepo.getAll().size(), 0);

        teacherRepo.create(teacher1);
        assertEquals(teacherRepo.getAll().size(), 1);
        assertEquals(teacherRepo.getAll().get(0).getFirstName(), "Catalin");
        assertEquals(teacherRepo.getAll().get(0).getLastName(), "Rusu");
        teacherRepo.create(teacher2);
        assertEquals(teacherRepo.getAll().size(), 2);
        assertEquals(teacherRepo.getAll().get(1).getFirstName(), "Diana");
        assertEquals(teacherRepo.getAll().get(1).getLastName(), "Cristea");

        studentsRepo.create(student1);
        assertEquals(studentsRepo.getAll().size(), 1);
        assertEquals(studentsRepo.getAll().get(0).getFirstName(), "Paul");
        assertEquals(studentsRepo.getAll().get(0).getLastName(), "Tarta");
        studentsRepo.create(student2);
        assertEquals(studentsRepo.getAll().size(), 2);
        assertEquals(studentsRepo.getAll().get(1).getFirstName(), "Andrei");
        assertEquals(studentsRepo.getAll().get(1).getLastName(), "Silion");

        courseRepo.create(course1);
        assertEquals(courseRepo.getAll().size(), 1);
        assertEquals(courseRepo.getAll().get(0).getName(), "MAP");
        courseRepo.create(course2);
        assertEquals(courseRepo.getAll().size(), 2);
        assertEquals(courseRepo.getAll().get(1).getName(), "BD");

    }

    @Test
    void delete() throws IOException {
        TeacherRepository teacherRepo = new TeacherRepository();

        StudentRepository studentsRepo = new StudentRepository();

        CourseRepository courseRepo = new CourseRepository();

        teacherRepo.create(teacher1);
        teacherRepo.create(teacher2);
        teacherRepo.delete(teacher1);
        assertEquals(teacherRepo.getAll().size(), 1);
        teacherRepo.delete(teacher2);
        assertEquals(teacherRepo.getAll().size(), 0);


        studentsRepo.create(student1);
        studentsRepo.create(student2);
        studentsRepo.delete(student1);
        assertEquals(studentsRepo.getAll().size(), 1);
        studentsRepo.delete(student2);
        assertEquals(studentsRepo.getAll().size(), 0);

        courseRepo.create(course1);
        courseRepo.create(course2);
        courseRepo.delete(course1);
        assertEquals(courseRepo.getAll().size(), 1);
        courseRepo.delete(course2);
        assertEquals(courseRepo.getAll().size(), 0);


    }
}