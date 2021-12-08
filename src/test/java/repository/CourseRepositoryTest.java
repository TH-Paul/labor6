package repository;

import org.junit.jupiter.api.Test;
import model.Course;
import model.Teacher;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CourseRepositoryTest {
    Teacher teacher1 = new Teacher("Catalin", "Rusu", new ArrayList<>());

    Teacher teacher2 = new Teacher("Diana", "Cristea", new ArrayList<>());

    Course course1 = new Course("MAP", teacher1, 70, new ArrayList<>(), 6);

    Course course2 = new Course("BD", teacher2, 80, new ArrayList<>(), 6);


    @Test
    void update() throws IOException {
        CourseRepository courseRepo = new CourseRepository();
        courseRepo.create(course1);
        courseRepo.create(course2);

        assertEquals(courseRepo.getAll().get(1).getMaxEnrollment(), 80);
        assertEquals(courseRepo.getAll().get(1).getCredits(), 6);
        Course course3 = new Course("BD", teacher2, 70, new ArrayList<>(), 5);
        courseRepo.update(course3);
        assertEquals(courseRepo.getAll().get(1).getMaxEnrollment(), 70);
        assertEquals(courseRepo.getAll().get(1).getCredits(), 5);

    }
}