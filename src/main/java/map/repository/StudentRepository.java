package map.repository;

import map.model.Student;


public class StudentRepository extends InMemoryRepository<Student>{

    public StudentRepository() {
        super();
    }

    public Student update(Student obj){
        return null;
    }

    public Student findById(int id){
        for(Student student : this.repoList){
            if(student.getStudentId() == id){
                return student;
            }
        }
        return null;
    }
}
