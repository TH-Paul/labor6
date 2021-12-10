package repository;

import model.Teacher;


public class TeacherRepository extends InMemoryRepository<Teacher>{

    public TeacherRepository() {
        super();
    }

    public Teacher update(Teacher obj){
        return null;
    }

    public Teacher findById(int id){
        for(Teacher teacher : this.repoList){
            if(teacher.getTeacherId() == id){
                return teacher;
            }
        }
        return null;
    }
}
