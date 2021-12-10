package repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Course;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CourseFileRepository extends FileRepository<Course>{

    public CourseFileRepository(File file) throws IOException{
        super();
        this.file = file;
        loadFromFile();
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
