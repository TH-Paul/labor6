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

    private final TeacherFileRepository teacherRepo;
    private final StudentFileRepository studentRepo;

    public CourseFileRepository(File file, TeacherFileRepository teacherRepo, StudentFileRepository studentRepo) throws IOException{
        super();
        this.file = file;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        loadFromFile();
    }

    /**
     * finds the Teacher Object with the given name in the teacher repo
     * @param teacherName1 - String
     * @return Teacher Object
     */
    public Teacher findTeacherWithName(String teacherName1){
        for(Teacher teacher : teacherRepo.getAll()){
            String teacherName2 = teacher.getLastName() + " " + teacher.getFirstName();
            if(teacherName2.equals(teacherName1)){
                return teacher;
            }
        }
        return null;
    }


    /**
     * finds the Student Object with the given name in the student repo
     * @param studentNameAndId1 - String
     * @return Student Object
     */
    public Student findStudentWithNameAndId(String studentNameAndId1){
        for(Student student : studentRepo.getAll()){
            String studentNameAndId2 = student.obtainNameAndId();
            if(studentNameAndId2.equals(studentNameAndId1)){
                return student;
            }
        }
        return null;
    }


    /**
     *
     * @param array - from parsing the json nodes
     * @return list of strings with student names and ids
     */
    public List<String> obtainStudentNamesAndIds(JsonNode array){
        List<String> studentNames = new ArrayList<>();
        for(int i = 0; i < array.size(); i++){
            studentNames.add(array.get(i).asText());
        }
        return studentNames;
    }



    public void loadFromFile() throws IOException{
        Reader reader = new BufferedReader(new FileReader(this.file));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for(JsonNode node : parser){
            Course course = new Course();
            course.setName(node.path("name").asText());
            course.setMaxEnrollment(node.path("maxEnrollment").asInt());
            course.setCredits(node.path("credits").asInt());


            Teacher teacher = findTeacherWithName(node.path("teacherName").asText());
            course.setTeacher(teacher);

            course.setStudentsEnrolled(new ArrayList<>());

            JsonNode array = node.path("studentsNamesAndIds");

            if(array.size() > 0) {
                List<String> studentNamesAndIds = obtainStudentNamesAndIds(array);

                for (String studentNameAndId : studentNamesAndIds) {
                    Student student = findStudentWithNameAndId(studentNameAndId);
                    course.getStudentsEnrolled().add(student);
                    student.getEnrolledCourses().add(course);
                }
            }

            if(teacher != null){
                teacher.getCourses().add(course);
            }

            repoList.add(course);
        }

        reader.close();
    }

}
