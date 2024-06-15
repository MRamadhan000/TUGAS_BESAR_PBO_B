package org.example.com.main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.com.main.UI.UIManager;
import org.example.com.main.books.Book;
import org.example.com.main.data.Admin;
import org.example.com.main.data.Student;
import org.example.com.main.data.User;
import java.io.IOException;
import java.util.Scanner;

public class Main extends Application{
    public static void main(String[] args) {
        addUser();
        launch(args);
    }
    public static void addUser(){
        Student student1 = new Student("AGUS","123","TEKNIK","INFORMATIKA");
        Student student2 = new Student("AHMAD","234","KEDOKTERAN","DOKTER");
        Admin.getStudentData().add(student1);
        Admin.getStudentData().add(student2);
        Book book1 = new Book("A01","Shingeki No Kyoujin", "HAJIME ISAYAMAN",10);
        Book book2 = new Book("B01","Naruto","MASASHI KISHIMOTO",12);
        book1.setCategory("History");
        book2.setCategory("Story");
        User.getBookList().add(book1);
        User.getBookList().add(book2);
        book1.setDuration(9);
        student1.getBorrowedBooks().add(book1);
    }

    @Override
    public void start(Stage stage) throws Exception {
        menu(stage);
    }
    public static void menu(Stage stage)throws IOException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        VBox hboxBtn = new VBox(10);
        Button btnLogAdmin = new Button("Login As Admin");
        Button btnLogStudent = new Button("Login As Student");
        Button btnExit = new Button("EXIT");
        Button btnTemp  = new Button(("Temp"));

        double buttonWidth = 150; // Tentukan lebar tombol
        double buttonHeight = 30; // Tentukan tinggi tombol
        btnLogAdmin.setPrefSize(buttonWidth, buttonHeight);
        btnLogStudent.setPrefSize(buttonWidth, buttonHeight);
        btnExit.setPrefSize(buttonWidth, buttonHeight);

        hboxBtn.setAlignment(Pos.CENTER);
        hboxBtn.getChildren().addAll(btnLogAdmin,btnLogStudent,btnExit,btnTemp);
        grid.add(hboxBtn,1,3);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);


        btnLogAdmin.setOnAction(actionEvent -> {
            try {
                Admin.logIn(stage);
            } catch (Exception e) {
                actionTarget.setText("An error occurred: " + e.getMessage());
            }
        });

        btnLogStudent.setOnAction(actionEvent -> {
            try {
                Student.logIn(stage);
            }catch (Exception e){
                actionTarget.setText("An error occured " + e.getMessage());
            }
        });


        btnExit.setOnAction(actionEvent -> {
            try {
                stage.close();
            }catch (Exception e){
                actionTarget.setText("An error occured " + e.getMessage());
            };
        });
        Scene scene = new Scene(grid, UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("MENU");
        stage.setScene(scene);
        stage.show();
    }
    public static String inputNIM(){
        Scanner inputObj = new Scanner(System.in);
        return inputObj.nextLine();
    }
    public static void addTempStudent(Admin admin,String name,String NIM, String faculty, String program) {
        admin.addStudent(name,NIM,faculty,program);
    }

    public static Student checkNIM(String name, String NIM, String faculty, String program) {
        for (Student x : Admin.getStudentData()) {
            if (x.getNIM().equals(NIM)) {
                return null;
            }
        }
        return new Student(name, NIM, faculty, program);
    }
    public static void addTempBook(Student student,int numberBorrowed, String[][] arr) {
        for (int i = 0; i < numberBorrowed; i++)
            student.choiceBook(arr[i][0],Integer.parseInt(arr[i][1]));
    }

}