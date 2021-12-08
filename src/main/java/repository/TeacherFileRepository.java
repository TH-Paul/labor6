package repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Teacher;

import java.io.*;
import java.util.ArrayList;

public class TeacherFileRepository extends FileRepository<Teacher>{

    public TeacherFileRepository(File file) throws IOException{
        super();
        this.file = file;
        loadFromFile();
    }



    public void loadFromFile() throws IOException{
        Reader reader = new BufferedReader(new FileReader(this.file));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for(JsonNode node : parser){
            Teacher teacher = new Teacher();
            teacher.setFirstName(node.path("firstName").asText());
            teacher.setLastName(node.path("lastName").asText());
            teacher.setCourses(new ArrayList<>());

            repoList.add(teacher);
        }

        reader.close();
    }

}
