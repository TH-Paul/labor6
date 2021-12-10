package repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentFileRepository extends FileRepository<Student>{

    public StudentFileRepository(File file) throws IOException {
        super();
        this.file = file;
        loadFromFile();
    }

    /**
     *
     * @param array - from parsing the json nodes
     * @return list of strings with course ids
     */
    public List<Integer> obtainCourseIds(JsonNode array){
        List<Integer> CourseIds = new ArrayList<>();
        for(int i = 0; i < array.size(); i++){
            CourseIds.add(array.get(i).asInt());
        }
        return CourseIds;
    }

    public Student findById(int id){
        for(Student student : this.repoList){
            if(student.getStudentId() == id){
                return student;
            }
        }
        return null;
    }

    public void loadFromFile() throws IOException{
        Reader reader = new BufferedReader(new FileReader(this.file));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for(JsonNode node : parser){
            Student student = new Student();
            student.setFirstName(node.path("firstName").asText());
            student.setLastName(node.path("lastName").asText());
            student.setStudentId(node.path("studentId").asInt());
            student.setTotalCredits(node.path("totalCredits").asInt());

            student.setEnrolledCourses(new ArrayList<>());
            JsonNode array = node.path("enrolledCourses");
            if(array.size() > 0) {
                List<Integer> courseIds = obtainCourseIds(array);
                student.setEnrolledCourses(courseIds);
            }


            repoList.add(student);
        }

        reader.close();
    }

}
