package repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Course;
import model.Student;
import model.Teacher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CourseFileRepository extends FileRepository<Course>{

    private final ICrudRepository<Teacher> teacherRepo;
    private final ICrudRepository<Student> studentRepo;

    public CourseFileRepository(File file, ICrudRepository<Teacher> teacherRepo, ICrudRepository<Student> studentRepo) throws IOException{
        super();
        this.file = file;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        loadFromFile();
    }

    /**
     * finds the Student Object with the given id in the student repo
     * @param studentId - String
     * @return Student Object
     */
    public Student findStudentWithId(int studentId){
        for(Student student : studentRepo.getAll()){
            if(student.getStudentId() == studentId){
                return student;
            }
        }
        return null;
    }

    /**
     * finds the Teacher Object with the given id in the student repo
     * @param teacherId - String
     * @return Teacher Object
     */
    public Teacher findTeacherWithId(int teacherId){
        for(Teacher teacher : teacherRepo.getAll()){
            if(teacher.getTeacherId() == teacherId){
                return teacher;
            }
        }
        return null;
    }


    /**
     *
     * @param array - from parsing the json nodes
     * @return list of strings with student ids
     */
    public List<Integer> obtainStudentIds(JsonNode array){
        List<Integer> studentIds = new ArrayList<>();
        for(int i = 0; i < array.size(); i++){
            studentIds.add(array.get(i).asInt());
        }
        return studentIds;
    }

    public Course findById(int id){
        for(Course course : this.repoList){
            if(course.getCourseId() == id){
                return course;
            }
        }
        return null;
    }

    public void loadFromFile() throws IOException{
        Reader reader = new BufferedReader(new FileReader(this.file));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for(JsonNode node : parser){
            Course course = new Course();
            course.setCourseId(node.path("courseId").asInt());
            course.setName(node.path("name").asText());
            course.setMaxEnrollment(node.path("maxEnrollment").asInt());
            course.setCredits(node.path("credits").asInt());

            course.setTeacherId(node.path("teacherId").asInt());
            if(course.getTeacherId() == 0){
                course.setTeacherId(null);
            }


            course.setStudentsEnrolled(new ArrayList<>());
            JsonNode array = node.path("studentsEnrolled");
            if(array.size() > 0) {
                List<Integer> studentIds = obtainStudentIds(array);
                course.setStudentsEnrolled(studentIds);
            }
            repoList.add(course);
        }

        reader.close();
    }

}
