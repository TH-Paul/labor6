package controller;

import model.Course;
import model.Teacher;
import repository.CourseFileRepository;
import repository.FileRepository;

import java.io.IOException;

public class TeacherController extends AbstractController<Teacher>{
    private final CourseFileRepository courseRepo;

    public TeacherController(FileRepository<Teacher> repository, CourseFileRepository courseRepo) {
        super(repository);
        this.courseRepo = courseRepo;
    }

    /**
     * adds a teacher to the repo
     * @param object - Teacher
     */
    @Override
    public void create(Teacher object) throws IOException {
        this.repository.create(object);
    }


    /**
     * deletes the teacher -> all his courses will have no teacher
     * @param object - to be deleted
     */
    @Override
    public void delete(Teacher object) throws IOException {
        for(Course course1 : object.getCourses()){
            for (Course course2 : courseRepo.getAll()){
                if(course1 == course2){
                    course2.setTeacher(null);
                }
            }
        }
        this.repository.delete(object);

        courseRepo.writeToFile();
    }
}
