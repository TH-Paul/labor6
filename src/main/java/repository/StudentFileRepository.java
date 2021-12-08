package repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Student;

import java.io.*;
import java.util.ArrayList;

public class StudentFileRepository extends FileRepository<Student>{

    public StudentFileRepository(File file) throws IOException {
        super();
        this.file = file;
        loadFromFile();
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


            repoList.add(student);
        }

        reader.close();
    }

}
