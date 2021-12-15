package map.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import map.model.Teacher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherFileRepository extends FileRepository<Teacher>{

    public TeacherFileRepository(File file) throws IOException{
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

    public Teacher findById(int id){
        for(Teacher teacher : this.repoList){
            if(teacher.getTeacherId() == id){
                return teacher;
            }
        }
        return null;
    }

    public void loadFromFile() throws IOException{
        Reader reader = new BufferedReader(new FileReader(this.file));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parser = objectMapper.readTree(reader);

        for(JsonNode node : parser){
            Teacher teacher = new Teacher();
            teacher.setFirstName(node.path("firstName").asText());
            teacher.setLastName(node.path("lastName").asText());
            teacher.setTeacherId(node.path("teacherId").asInt());

            teacher.setCourses(new ArrayList<>());
            JsonNode array = node.path("courses");
            if(array.size() > 0) {
                List<Integer> courseIds = obtainCourseIds(array);
                teacher.setCourses(courseIds);
            }

            repoList.add(teacher);
        }

        reader.close();
    }

}
