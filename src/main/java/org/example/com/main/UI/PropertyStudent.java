package org.example.com.main.UI;
import javafx.beans.property.SimpleStringProperty;
import org.example.com.main.data.Student;
import java.util.ArrayList;
public class PropertyStudent {
    private final SimpleStringProperty name;
    private final SimpleStringProperty nim;
    private final SimpleStringProperty faculty;
    private final SimpleStringProperty programStudi;
    private final SimpleStringProperty email;
    private final SimpleStringProperty consultingClass;

    public PropertyStudent(String name, String nim, String faculty, String programStudi,String email, String consultingClass) {
        this.name = new SimpleStringProperty(name);
        this.nim = new SimpleStringProperty(nim);
        this.faculty = new SimpleStringProperty(faculty);
        this.programStudi = new SimpleStringProperty(programStudi);
        this.email = new SimpleStringProperty(email);
        this.consultingClass = new SimpleStringProperty(consultingClass);
    }

    public static ArrayList<PropertyStudent> studentToProperty(ArrayList<Student> arr) {
        ArrayList<PropertyStudent> temp = new ArrayList<>();
        for (Student student : arr) {
            PropertyStudent obj = new PropertyStudent(student.getName(), student.getNIM(), student.getFaculty(), student.getProgramStudi(),student.getEmail(), student.getConsultingClass());
            temp.add(obj);
        }
        return temp;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getNim() {
        return nim.get();
    }

    public SimpleStringProperty nimProperty() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim.set(nim);
    }

    public String getFaculty() {
        return faculty.get();
    }

    public SimpleStringProperty facultyProperty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty.set(faculty);
    }

    public String getProgramStudi() {
        return programStudi.get();
    }

    public SimpleStringProperty programStudiProperty() {
        return programStudi;
    }

    public void setProgramStudi(String programStudi) {
        this.programStudi.set(programStudi);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getConsultingClass() {
        return consultingClass.get();
    }

    public SimpleStringProperty consultingClassProperty() {
        return consultingClass;
    }

    public void setConsultingClass(String consultingClass) {
        this.consultingClass.set(consultingClass);
    }
}
