package com.jichuangsi.school.statistics.model.Report;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassStudentModel {

    private String classId;
    private String className;
    private List<Student> student=new ArrayList<Student>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }
/* private Map<String,List<Student>> classStudentModel=new HashMap<String,List<Student>>();

    public Map<String, List<Student>> getClassStudentModel() {
        return classStudentModel;
    }

    public void setClassStudentModel(Map<String, List<Student>> classStudentModel) {
        this.classStudentModel = classStudentModel;
    }*/
}
