package org.example.com.main.UI;
import javafx.beans.property.SimpleStringProperty;
import org.example.com.main.data.Student;
import java.util.ArrayList;
public class PropertyStudent {
    private final SimpleStringProperty name;
    private final SimpleStringProperty nim;
    private final SimpleStringProperty faculty;
    private final SimpleStringProperty programStudi;

    public PropertyStudent(String name, String nim, String faculty, String programStudi) {
        this.name = new SimpleStringProperty(name);
        this.nim = new SimpleStringProperty(nim);
        this.faculty = new SimpleStringProperty(faculty);
        this.programStudi = new SimpleStringProperty(programStudi);
    }

    public static ArrayList<PropertyStudent> studentToProperty(ArrayList<Student> arr) {
        ArrayList<PropertyStudent> temp = new ArrayList<>();
        for (Student student : arr) {
            PropertyStudent obj = new PropertyStudent(student.getName(), student.getNIM(), student.getFaculty(), student.getProgramStudi());
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
}

