package org.example.com.main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class Main extends Application{
    public static void main(String[] args) {
        Admin.firstDate();
        Student.setJadwalkonsultasi();
        addUser2();

        launch(args);
    }

    public static void addUser2(){
        Student student1 = new Student("AGUS",String.valueOf(2023),"FT","INFORMATIKA","agus@gmail.com","senin");
        Admin.getStudentData().add(student1);
        Student student2= new Student("Mehmed",String.valueOf(2022),"FT","INFORMATIKA","mehmed@gmail.com");
        Admin.getStudentData().add(student2);

        for (int i = 0; i < 10; i++){
            String bookId = "A0"+i;
            Book book = new Book(bookId,"A","RANDOMA",i+2);
            book.setCategory("Story");
            User.getBookList().add(book);

            Student student = new Student("Fulan",String.valueOf(i+100),"FT","INFORMATIKA","email@gmail.com");
            Admin.getStudentData().add(student);
            if(i%2 == 0 && i > 4) {
                Admin.updateDate();
                Student.getFavoriteBooks().add(bookId);
            }

            if (i == 8){
                for (int x = 0; x < 20; x++){
                    student.addVisitor();
                }
            }
            if (i %2 == 0 ) {
                Student.getFavoriteBooks().add(bookId);
                student.addVisitor();
            }
            Student.getFavoriteBooks().add(bookId);
            student.addVisitor();
        }
    }

    public static void addUser(){
        for (int i = 0; i < 10; i++){
            String bookId = "A0"+i;
            Book book = new Book(bookId,"A","RANDOMA",i+2);
            book.setCategory("Story");
            User.getBookList().add(book);

            Student student = new Student("AGUS",String.valueOf(i+100),"FT","INFORMATIKA","email@gmail.com");
            Admin.getStudentData().add(student);
            if(i%2 == 0 && i >4) {
                Admin.updateDate();
                Student.getFavoriteBooks().add(bookId);
                student.addVisitor();
                student.addVisitor();
            }
            if (i %2 == 0 ) {
                Student.getFavoriteBooks().add(bookId);
                student.addVisitor();
            }
            Student.getFavoriteBooks().add(bookId);
            student.addVisitor();
            student.addVisitor();
            student.addVisitor();
        }
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
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add image at the top
        ImageView imageView = new ImageView(new Image(Main.class.getResource("/Image/logos.jpg").toString()));
        imageView.setFitWidth(200); // Set desired width
        imageView.setPreserveRatio(true); // Preserve the aspect ratio
        grid.add(imageView, 0, 0, 2, 1); // Span across 2 columns

        grid.setStyle("-fx-background-color: #686D76;");

        VBox hboxBtn = new VBox(10);
        Button btnLogAdmin = new Button("Login As Admin");
        Button btnLogStudent = new Button("Login As Student");
        Button btnExit = new Button("EXIT");

        btnLogAdmin.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnLogStudent.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnExit.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());

        hboxBtn.setAlignment(Pos.CENTER);
        hboxBtn.getChildren().addAll(btnLogAdmin, btnLogStudent, btnExit);
        grid.add(hboxBtn, 1, 3);

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
            } catch (Exception e) {
                actionTarget.setText("An error occurred: " + e.getMessage());
            }
        });

        btnExit.setOnAction(actionEvent -> {
            try {
                stage.close();
            } catch (Exception e) {
                actionTarget.setText("An error occurred: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        stage.setTitle("MENU");
        stage.setScene(scene);
        stage.show();
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setVgap(10);
//        grid.setHgap(10);
//        grid.setPadding(new Insets(25,25,25,25));
//
//        VBox hboxBtn = new VBox(10);
//        Button btnLogAdmin = new Button("Login As Admin");
//        Button btnLogStudent = new Button("Login As Student");
//        Button btnExit = new Button("EXIT");
//
//        btnLogAdmin.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
//        btnLogStudent.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
//        btnExit.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
//
//        hboxBtn.setAlignment(Pos.CENTER);
//        hboxBtn.getChildren().addAll(btnLogAdmin,btnLogStudent,btnExit);
//        grid.add(hboxBtn,1,3);
//
//        final Text actionTarget = new Text();
//        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
//        grid.add(actionTarget, 1, 6);
//
//        btnLogAdmin.setOnAction(actionEvent -> {
//            try {
//                Admin.logIn(stage);
//            } catch (Exception e) {
//                actionTarget.setText("An error occurred: " + e.getMessage());
//            }
//        });
//
//        btnLogStudent.setOnAction(actionEvent -> {
//            try {
//                Student.logIn(stage);
//            }catch (Exception e){
//                actionTarget.setText("An error occured " + e.getMessage());
//            }
//        });
//
//        btnExit.setOnAction(actionEvent -> {
//            try {
//                stage.close();
//            }catch (Exception e){
//                actionTarget.setText("An error occured " + e.getMessage());
//            };
//        });
//        Scene scene = new Scene(grid, UIManager.getWidth(),UIManager.getHeight());
//        stage.setTitle("MENU");
//        stage.setScene(scene);
//        stage.show();
    }
}