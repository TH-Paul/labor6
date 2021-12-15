package map.controller;

import map.model.Course;
import map.model.Teacher;
import map.repository.*;

import java.io.IOException;

public class TeacherController extends AbstractController<Teacher>{
    private ICrudRepository<Course> courseRepo;

    public TeacherController(ICrudRepository<Teacher> repository, ICrudRepository<Course> courseRepo) {
        super(repository);
        this.courseRepo = courseRepo;
    }

    public TeacherController(ICrudRepository<Teacher> repository, CommunicationDBRepository communicationDBRepository) {
        super(repository, communicationDBRepository);
    }

    /**
     * adds a teacher to the repo
     * @param object - Teacher
     */
    @Override
    public void create(Teacher object) throws IOException{
        this.repository.create(object);
    }


    /**
     * deletes the teacher -> all his courses will have no teacher
     * @param object - to be deleted
     */
    @Override
    public void delete(Teacher object) throws IOException{
        if(repository instanceof InMemoryRepository) {
            for (int course1 : object.getCourses()) {
                for (Course course2 : courseRepo.getAll()) {
                    if (course1 == course2.getCourseId()) {
                        course2.setTeacherId(null);
                        break;
                    }
                }
            }
        }

        if(repository instanceof DBRepository){
            communicationDBRepository.deleteTeacherEffect(object);
        }

        this.repository.delete(object);

        if(courseRepo instanceof FileRepository) {
            ((FileRepository<Course>) courseRepo).writeToFile();
        }
    }

    @Override
    public Teacher findById(int id) {

        if(repository instanceof DBRepository){
            return communicationDBRepository.getTeacherDBRepository().getTeacher(id);
        }

        if(repository instanceof InMemoryRepository) {
            return ((InMemoryRepository<Teacher>) this.repository).findById(id);
        }

        return null;
    }
}
