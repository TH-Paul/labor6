package konsole;

import controller.CourseController;
import controller.StudentController;
import controller.TeacherController;
import exceptions.CreditsLimit;
import exceptions.FullCourse;
import exceptions.InvalidCourseChange;
import exceptions.InvalidValue;
import model.Course;
import model.Student;
import model.Teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final CourseController courseController;
    private final StudentController studentController;
    private final TeacherController teacherController;

    public UserInterface(CourseController courseController, StudentController studentController, TeacherController teacherController) {
        this.courseController = courseController;
        this.studentController = studentController;
        this.teacherController = teacherController;
    }

    public static String readString() {
        Scanner in = new Scanner(System.in);
        return in.next();
    }

    public static int readInt(String toBeRead){
        Scanner in = new Scanner(System.in);
        boolean inputValid = false;
        while(!inputValid){
            System.out.print("Enter " + toBeRead + ": ");

            String input = in.next();

            try{
                int intInput = Integer.parseInt(input);

                if(intInput <= 0){
                    throw new InvalidValue("Value must be greater than 0!");
                }

                inputValid = true;
                return intInput;
            }
            catch(NumberFormatException e){
                System.out.println("You must enter an integer!\n");
            }

            catch (InvalidValue e){
                System.out.println("The value must be greater than 0. Try again!\n");
            }
        }
        return -1;
    }

    public static int readIntOption(String toBeRead){
        Scanner in = new Scanner(System.in);
        boolean inputValid = false;
        while(!inputValid){
            System.out.print("Enter " + toBeRead + ": ");

            String input = in.next();

            try{
                int intInput = Integer.parseInt(input);

                if(intInput < 0){
                    throw new InvalidValue("The value must be greater than or equal to 0!");
                }

                inputValid = true;
                return intInput;
            }
            catch(NumberFormatException e){
                System.out.println("You must enter an integer!\n");
            }

            catch (InvalidValue e){
                System.out.println("The value must be greater than or equal to 0. Try again!\n");
            }
        }
        return -1;
    }


    public List<String> getCoursesNames(List<Integer> coursesIds){
        List<String> coursesNames = new ArrayList<>();
        if(coursesIds != null) {
            for (int courseId : coursesIds) {
                Course course = courseController.findById(courseId);
                String courseName = course.getName() + "-" + course.getCourseId();
                coursesNames.add(courseName);
            }
        }

        return coursesNames;
    }

    public String showStudent(Student student){
        return '\n' + student.getLastName() + " " + student.getFirstName() + '\n' + "ID: "
                + student.getStudentId() + '\n' + "Total credits: " + student.getTotalCredits() + '\n'
                + "Enrolled Courses: " + getCoursesNames(student.getEnrolledCourses()) + '\n';
    }

    public String getTeacherName(int teacherId){
        return teacherController.findById(teacherId).getLastName() + " "
                + teacherController.findById(teacherId).getFirstName();
    }

    public List<String> getStudentsNames(List<Integer> studentsIds){
        List<String> studentsNames = new ArrayList<>();

        if(studentsIds != null) {
            for (int studentId : studentsIds) {
                Student student = studentController.findById(studentId);
                String studentName = student.getLastName() + " " + student.getFirstName() + "-" + student.getStudentId();
                studentsNames.add(studentName);
            }
        }

        return studentsNames;
    }

    public String showCourse(Course course){
        if(course.getTeacherId() == null) {
            return '\n' + course.getName() + '\n' + "ID: " + course.getCourseId() + '\n'
                    + "No teacher assigned!" +  '\n' + "Maximum number of students: " +
                    course.getMaxEnrollment() + '\n'
                    + "Enrolled Students: " + getStudentsNames(course.getStudentsEnrolled()) + '\n' +
                    "Credits: " + course.getCredits() + '\n';
        }

        return '\n' + course.getName() + '\n' + "ID: " + course.getCourseId() + '\n'
                + "Teacher: " + getTeacherName(course.getTeacherId()) + '\n' + "Maximum number of students: " +
                course.getMaxEnrollment() + '\n'
                + "Enrolled Students: " + getStudentsNames(course.getStudentsEnrolled()) + '\n' +
                "Credits: " + course.getCredits() + '\n';

    }

    public String showTeacher(Teacher teacher){
        return '\n' + teacher.getLastName() + " " + teacher.getFirstName() + '\n' + "ID: " + teacher.getTeacherId() + '\n'
                + "Courses: " + getCoursesNames(teacher.getCourses()) + '\n';
    }

    public void showStudents(){
        if(studentController.obtainObjects().size() == 0){
            System.out.println("\nNo students yet!\n");
            return;
        }

        System.out.println("\nStudents: ");
        for(int i = 0; i < studentController.obtainObjects().size(); i++){
            System.out.println((i+1) + ". " + showStudent(studentController.obtainObjects().get(i)));
        }

    }

    public void showTeachers(){
        if(teacherController.obtainObjects().size() == 0){
            System.out.println("\nNo teachers yet!\n");
            return;
        }

        System.out.println("\nTeachers: ");
        for(int i = 0; i < teacherController.obtainObjects().size(); i++){
            System.out.println((i+1) + ". " + showTeacher(teacherController.obtainObjects().get(i)));
        }
    }

    public void showCourses(){
        if(courseController.obtainObjects().size() == 0){
            System.out.println("\nNo courses yet!\n");
            return;
        }

        System.out.println("\nCourses: ");
        for(int i = 0; i < courseController.obtainObjects().size(); i++){
            System.out.println((i+1) + ". " + showCourse(courseController.obtainObjects().get(i)));
        }
    }

    public void showAvailableCourses(){
        List<Course> availableCourses = courseController.retrieveCoursesWithFreePlaces();

        if(availableCourses.size() == 0){
            System.out.println("\nThere are no available courses!\n");
            return;
        }

        System.out.println("\nAvailable Courses: ");
        for(int i = 0; i < availableCourses.size(); i++){
            System.out.println((i+1) + ". " + showCourse(availableCourses.get(i)));
        }
    }

    public void showStudentsEnrolledToACourse(){
        showCourses();

        int courseId = readInt("course ID");
        Course course = new Course();
        boolean found = false;
        for(Course c : courseController.obtainObjects()){
            if(c.getCourseId()==courseId){
                course = c;
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no course with the given id!\n");
            return;
        }

        if(course.getStudentsEnrolled().size() == 0){
            System.out.println("\nThere are no students enrolled to this course!\n");
            return;
        }

        for(int i = 0; i < course.getStudentsEnrolled().size(); i++){

            Student student = studentController.findById(course.getStudentsEnrolled().get(i));
            String studentNameAndId = student.getLastName() + " " + student.getFirstName() +
                    "-" + student.getStudentId();
            System.out.println((i+1) + ". " + studentNameAndId);
        }
    }

    public void addStudent() throws IOException{
        System.out.print("\nEnter student's first name: ");
        String firstName = readString();
        System.out.print("Enter student's last name: ");
        String lastName = readString();
        int studentId = readInt("student id");

        Student student = new Student(firstName, lastName, studentId);
        studentController.create(student);

        System.out.println("\nStudent added with success!\n");
    }

    public void addCourse() throws IOException{
        int courseId = readInt("course ID");
        System.out.print("\nEnter course's name: ");
        String courseName = readString();
        int maxEnrollment = readInt("max enrollment");
        int credits = readInt("credits");

        Course course = new Course(courseId, courseName, maxEnrollment, credits);
        courseController.create(course);

        System.out.println("\nCourse added with success!\n");
    }

    public void addTeacher() throws IOException{
        int teacherId = readInt("teacher ID");
        System.out.print("\nEnter teacher's first name: ");
        String firstName = readString();
        System.out.print("Enter teacher's last name: ");
        String lastName = readString();

        Teacher teacher = new Teacher(firstName, lastName, teacherId);
        teacherController.create(teacher);

        System.out.println("\nTeacher added with success!\n");
    }

    public void deleteStudent() throws IOException{
        showStudents();
        int studentId = readInt("student id");
        boolean found = false;
        int index = 0;
        for(int i = 0; i < studentController.obtainObjects().size(); i++){
            if(studentController.obtainObjects().get(i).getStudentId() == studentId){
                found = true;
                index = i;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no student with this id!\n");
        }
        else{
            studentController.delete(studentController.obtainObjects().get(index));
            System.out.println("\nStudent deleted with success!\n");
        }
    }

    public void deleteTeacher() throws IOException{
        showTeachers();
        int id = readInt("teacher id");
        boolean found = false;
        int index = 0;
        for(int i = 0; i < teacherController.obtainObjects().size(); i++){
            if(teacherController.obtainObjects().get(i).getTeacherId()==id){
                found = true;
                index = i;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no teacher with this id!\n");
        }
        else{
            teacherController.delete(teacherController.obtainObjects().get(index));
            System.out.println("\nTeacher deleted with success!\n");
        }
    }

    public void deleteCourse() throws IOException{
        showCourses();
        int id = readInt("course id");
        boolean found = false;
        int index = 0;
        for(int i = 0; i < courseController.obtainObjects().size(); i++){
            if(courseController.obtainObjects().get(i).getCourseId()==id){
                found = true;
                index = i;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no course with this id!\n");
        }
        else{
            courseController.delete(courseController.obtainObjects().get(index));
            System.out.println("\nCourse deleted with success!\n");
        }
    }

    public void registerStudent() throws IOException{

        showStudents();

        int studentId = readInt("student id");
        boolean found = false;
        for(int i = 0; i < studentController.obtainObjects().size(); i++){
            if(studentController.obtainObjects().get(i).getStudentId() == studentId){
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no student with this id!");
            System.out.println("The registration could not take place!\n");
            return;
        }

        showCourses();

        int courseId = readInt("course ID");
        found = false;
        for(int i = 0; i < courseController.obtainObjects().size(); i++){
            if(courseController.obtainObjects().get(i).getCourseId()==courseId){
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no course with this id!");
            System.out.println("The registration could not take place!\n");
            return;
        }

        try{
            boolean success = studentController.register(courseId, studentId);

            if(!success){
                System.out.println("\nThe student is already enrolled in the course!\n");
                return;
            }

            System.out.println("\nStudent registered with success!\n");
        }
        catch(FullCourse e){
            System.out.println("\nThe course already has the maximum number of participants\n");
        }
        catch(CreditsLimit e){
            System.out.println("\nThe student cannot have more than 30 credits\n");
        }

    }

    public void assignTeacher() throws IOException{
        showTeachers();

        int teacherId = readInt("teacher ID");

        boolean found = false;
        for(int i = 0; i < teacherController.obtainObjects().size(); i++){
            if(teacherController.obtainObjects().get(i).getTeacherId()==teacherId){
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no teacher with this id!");
            System.out.println("The assignment could not take place!\n");
            return;
        }

        showCourses();

        int courseId = readInt("course ID");
        found = false;
        for(int i = 0; i < courseController.obtainObjects().size(); i++){
            if(courseController.obtainObjects().get(i).getCourseId()==courseId){
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no course with this id!");
            System.out.println("The assignment could not take place!\n");
            return;
        }

        courseController.assignTeacherForCourse(teacherId, courseId);

        System.out.println("\nAssignment was successful!\n");
    }

    public void modifyCourseCredits() throws IOException{
        showCourses();

        int courseId = readInt("course ID");

        int newCredits = readInt("new credits value");

        boolean found = false;
        for(int i = 0; i < courseController.obtainObjects().size(); i++){
            if(courseController.obtainObjects().get(i).getCourseId()==courseId){
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("\nThere is no course with this id!");
            System.out.println("The operation could not take place!\n");
            return;
        }

        courseController.modifyCourseCredits(courseId, newCredits);
        System.out.println("\nOperation was successful!\n");
    }

    public void modifyCourseMaxEnrollment(){
        showCourses();

        int courseId = readInt("course ID");

        int newMaxEnrollment = readInt("new max enrollment value");

        boolean found = false;
        for(int i = 0; i < courseController.obtainObjects().size(); i++){
            if(courseController.obtainObjects().get(i).getCourseId()==courseId){
                found = true;
                break;
            }
        }

        if(!found) {
            System.out.println("\nThere is no course with this id!");
            System.out.println("The operation could not take place!\n");
            return;
        }

        try{
            courseController.modifyCourseMaxEnrolled(courseId, newMaxEnrollment);
            System.out.println("\nOperation was successful!\n");
        }
        catch(InvalidCourseChange | IOException e){
            System.out.println("\nThere are already "
                    + courseController.findById(courseId).getStudentsEnrolled().size() + " students enrolled!");
        }

    }

    public void sortStudentsByName(){
        List<Student> sortedStudents = studentController.sortStudentsByName();
        for(int i = 0; i < sortedStudents.size(); i++){
            System.out.println((i+1) + ". " + showStudent(sortedStudents.get(i)));
        }
    }

    public void filterStudentsByCredits(){
        System.out.println("\nShow students who have fewer credits than the entered value!");
        int filter = readInt("the credits");

        List<Student> filtered = studentController.filterStudentsByCredits(filter);

        if(filtered.size() == 0){
            System.out.println("\nThere are no students who meet the criteria\n");
            return;
        }

        for(int i = 0; i < filtered.size(); i++){
            System.out.println((i+1) + ". " + showStudent(filtered.get(i)));
        }
    }

    public void sortCoursesByCredits(){
        List<Course> sortedCourses = courseController.sortCoursesByCredits();
        for(int i = 0; i < sortedCourses.size(); i++){
            System.out.println((i+1) + ". " + showCourse(sortedCourses.get(i)));
        }
    }

    public void filterCoursesByEnrolledStudents(){
        System.out.println("\nShow courses who have fewer students than the entered value!");
        int filter = readInt("the number of students");

        List<Course> filtered = courseController.filterCoursesByEnrolledStudents(filter);

        if(filtered.size() == 0){
            System.out.println("\nThere are no courses who meet the criteria\n");
            return;
        }

        for(int i = 0; i < filtered.size(); i++){
            System.out.println((i+1) + ". " + showCourse(filtered.get(i)));
        }
    }

    public static void showMenu(){
        System.out.println("\nMenu: ");
        System.out.println(" 0. Exit" + "\t\t\t\t\t\t\t\t\t\t\t\t8. Register student");
        System.out.println(" 1. Show  all students" + "\t\t\t\t\t\t\t\t\t9. Assign teacher");
        System.out.println(" 2. Show all teachers" + "\t\t\t\t\t\t\t\t\t10. Modify course credits");
        System.out.println(" 3. Show all courses" + "\t\t\t\t\t\t\t\t\t11. Modify course maximum attendance");
        System.out.println(" 4. Show available courses" + "\t\t\t\t\t\t\t\t12. Sort students by name");
        System.out.println(" 5. Show students enrolled to a course" + "\t\t\t\t\t13. Filter students by credits");
        System.out.println(" 6. Add entity" + "\t\t\t\t\t\t\t\t\t\t\t14. Sort courses by credits");
        System.out.println(" 7. Delete entity" + "\t\t\t\t\t\t\t\t\t\t15. Filter courses by number of enrolled students");
    }

    public static void showAddMenu(){
        System.out.println("\n 0. Go back");
        System.out.println(" 1. Add student");
        System.out.println(" 2. Add teacher");
        System.out.println(" 3. Add course\n");
    }

    public static void showDeleteMenu(){
        System.out.println("\n 0. Go back");
        System.out.println(" 1. Delete student");
        System.out.println(" 2. Delete teacher");
        System.out.println(" 3. Delete course\n");
    }


    public void runMenu() throws IOException{
        boolean done = false;
        int option;

        while(!done){
            showMenu();

            option = readIntOption("option");
            if(option >15){
                System.out.println("\nSelect a valid option!\n");
            }

            else if(option == 0){
                done = true;
            }

            else if(option == 1){
                showStudents();
            }

            else if(option == 2){
                showTeachers();
            }

            else if(option == 3){
                showCourses();
            }

            else if(option == 4){
                showAvailableCourses();
            }

            else if(option == 5){
                showStudentsEnrolledToACourse();
            }

            else if(option == 6){
                boolean doneAdd = false;
                int optionAdd;

                while(!doneAdd){
                    showAddMenu();
                    optionAdd = readIntOption("option");

                    if(optionAdd > 3){
                        System.out.println("\nSelect a valid option!\n");
                    }

                    else if(optionAdd == 0){
                        doneAdd = true;
                    }

                    else if(optionAdd == 1){
                        addStudent();
                    }

                    else if(optionAdd == 2){
                        addTeacher();
                    }

                    else if(optionAdd == 3){
                        addCourse();
                    }
                }
            }

            else if(option == 7) {
                boolean doneDelete = false;
                int optionDelete;

                while (!doneDelete) {
                    showDeleteMenu();
                    optionDelete = readIntOption("option");

                    if (optionDelete > 3) {
                        System.out.println("\nSelect a valid option!\n");
                    }

                    else if (optionDelete == 0) {
                        doneDelete = true;
                    }

                    else if (optionDelete == 1) {
                        deleteStudent();
                    }

                    else if (optionDelete == 2) {
                        deleteTeacher();
                    }

                    else if (optionDelete == 3) {
                        deleteCourse();
                    }
                }
            }

            else if(option == 8){
                registerStudent();
            }

            else if(option == 9){
                assignTeacher();
            }

            else if(option == 10){
                modifyCourseCredits();
            }

            else if(option == 11){
                modifyCourseMaxEnrollment();
            }

            else if(option == 12){
                sortStudentsByName();
            }

            else if(option == 13){
                filterStudentsByCredits();
            }

            else if(option == 14){
                sortCoursesByCredits();
            }

            else if(option == 15){
                filterCoursesByEnrolledStudents();
            }
        }
    }
}
