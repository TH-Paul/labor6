package map.repository;

import map.model.Course;


public class CourseRepository extends InMemoryRepository<Course>{

    public CourseRepository() {
        super();
    }

    /**
     *
     * @param obj - information that we want to store in the object with the same name
     * @return the course with the updated information if it was found
     */
    public Course update(Course obj){
        Course courseToUpdate = this.repoList.stream()
                .filter(course -> course.getName().equals(obj.getName()))
                .findFirst()
                .orElseThrow();

        courseToUpdate.setCredits(obj.getCredits());
        courseToUpdate.setMaxEnrollment(obj.getMaxEnrollment());
        courseToUpdate.setTeacherId(obj.getTeacherId());

        return courseToUpdate;
    }

    public Course findById(int id){
        for(Course course : this.repoList){
            if(course.getCourseId() == id){
                return course;
            }
        }
        return null;
    }
}
