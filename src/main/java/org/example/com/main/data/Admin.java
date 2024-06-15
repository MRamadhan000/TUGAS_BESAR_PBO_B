package org.example.com.main.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.com.main.Main;
import org.example.com.main.UI.PropertyBook;
import org.example.com.main.UI.PropertyStudent;
import org.example.com.main.UI.UIManager;
import org.example.com.main.books.*;
import org.example.com.main.util.IMenu;
import org.example.com.main.exception.custom.illegalAdminAcces;

import java.io.IOException;
import java.util.*;

public class Admin extends User implements IMenu {
    private TableView tableStudent = new TableView<>();
    private final String adminUserName = "admin";
    private final String adminPassword = "admin";
    private static final ArrayList<Student> studentData = new ArrayList<>();
    private static final ArrayList<String> studentList = new ArrayList<>();
    public static void logIn(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Log In Admin");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label userName = new Label("Username:");
        grid.add(userName, 0, 1); // Kolom 0, Baris 1

        TextField inputUserName = new TextField();
        inputUserName.setPromptText("Enter your username");
        grid.add(inputUserName, 1, 1); // Kolom 1, Baris 1

        Label password = new Label("Password : ");
        grid.add(password, 0, 2);

        PasswordField inputPassword = new PasswordField();
        inputPassword.setPromptText("Enter your password");
        grid.add(inputPassword, 1, 2);

        Button btnSignIn = new Button("SIGN IN");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSignIn);
        grid.add(hBBtn, 1, 3);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSignIn.setOnAction(actionEvent -> {
            if (inputUserName.getText().isEmpty() || inputPassword.getText().isEmpty()) {
                UIManager.showError(actionTarget, "Username or password cannot be empty");
            } else {
                Admin admin = new Admin();
                try {
                    boolean isValid = admin.isAdmin(inputUserName.getText(), inputPassword.getText());
                    admin.menu(stage);
                } catch (illegalAdminAcces e) {
                    UIManager.showError(actionTarget, e.getMessage());
                }
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getWidth());
        stage.setTitle("LOGIN ADMIN");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void menu(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Admin Menu");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        VBox hBBtn = new VBox(10);
        Button btnAddStudent = new Button("Add Student");
        Button btnAddBook = new Button("Add Book");
        Button btnDisplayStudent = new Button("Display Registered Student");
        Button btnDisplayBook = new Button("Display Book");
        Button btnEditBook = new Button("Edit Book");
        Button btnLogOut = new Button("Log Out");
        hBBtn.setAlignment(Pos.CENTER);
        hBBtn.getChildren().addAll(btnAddStudent,btnAddBook,btnDisplayStudent,btnDisplayBook,btnEditBook,btnLogOut);
        grid.add(hBBtn,1,3);

        double buttonWidth = 170; // Tentukan lebar tombol
        double buttonHeight = 30; // Tentukan tinggi tombol
        btnAddStudent.setPrefSize(buttonWidth, buttonHeight);
        btnAddBook.setPrefSize(buttonWidth, buttonHeight);
        btnDisplayBook.setPrefSize(buttonWidth, buttonHeight);
        btnDisplayStudent.setPrefSize(buttonWidth, buttonHeight);
        btnEditBook.setPrefSize(buttonWidth,buttonHeight);
        btnLogOut.setPrefSize(buttonWidth, buttonHeight);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        btnAddStudent.setOnAction(actionEvent -> {
            try {
                addStudent(stage);
            }catch (Exception e){
                UIManager.showError(actionTarget,e.getMessage());
            };
        });

        btnAddBook.setOnAction(actionEvent -> {
            try {
               inputBook(stage);
            }catch (Exception e){
                UIManager.showError(actionTarget,e.getMessage());
            }
        });

        btnDisplayBook.setOnAction(actionEvent -> {
            try {
                displayBook(stage);
            }catch (Exception e){
                UIManager.showError(actionTarget,e.getMessage());
            }
        });

        btnDisplayStudent.setOnAction(actionEvent -> {
            try {
               displayStudent(stage);
            }catch (Exception e){
                UIManager.showError(actionTarget,e.getMessage());
            }
        });

        btnLogOut.setOnAction(actionEvent -> {
            logOut(stage);
        });

        btnEditBook.setOnAction(actionEvent -> {
            updateBooks(stage);
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("ADMIN MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void addStudent(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Add Student");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label name = new Label("Name :");
        grid.add(name, 0, 1); // Kolom 0, Baris 1
        TextField inputName = new TextField();
        inputName.setPromptText("Enter student name");
        grid.add(inputName, 1, 1); // Kolom 1, Baris 1

        Label NIM = new Label("NIM : ");
        grid.add(NIM, 0, 2);
        TextField inputNIM = new TextField();
        inputNIM.setPromptText("Enter student NIM");
        grid.add(inputNIM, 1, 2);

        Label faculty = new Label("Faculty");
        grid.add(faculty,0,3);
        TextField inputFaculty = new TextField();
        inputFaculty.setPromptText("Enter student faculty");
        grid.add(inputFaculty,1,3);

        Label program = new Label("Program");
        grid.add(program,0,4);
        TextField inputProgram = new TextField();
        inputProgram.setPromptText("Enter student program");
        grid.add(inputProgram,1,4);

        Button btnSubmit = new Button("SUBMIT");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSubmit.setOnAction(actionEvent -> {
            Student student = Main.checkNIM(inputName.getText(),inputNIM.getText(),inputFaculty.getText(),inputProgram.getText());
            if (inputName.getText().isEmpty() || inputNIM.getText().isEmpty() || inputFaculty.getText().isEmpty()||inputProgram.getText().isEmpty())
                UIManager.showError(actionTarget,"PLEASE FILL ALL BLANKS");
            else if(inputNIM.getText().length()< 15)
                UIManager.showError(actionTarget,"NIM MUST 15 CHARACTERS");
            else if(student == null)
                UIManager.showError(actionTarget,"NIM SAME");
            else {
                Main.addTempStudent(this,inputName.getText(),inputNIM.getText(),inputFaculty.getText(),inputProgram.getText());
                UIManager.showSuccess(actionTarget,"STUDENT ADDED SUCCESSFULY");
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("ADD STUDENT MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void displayStudent(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        final Label label = new Label("REGISTERED STUDENTS");
        label.setFont(new Font("Arial", 30));
        tableStudent.setEditable(true);

        tableStudent.getColumns().clear();
        TableColumn<Student,String> nameCol = new TableColumn<>("Name");
        TableColumn<Student,String> nimCol = new TableColumn<>("NIM");
        TableColumn<Student,String> facultyCol = new TableColumn<>("Faculty");
        TableColumn<Student,String> prodiCol = new TableColumn<>("Program Studi");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty") );
        prodiCol.setCellValueFactory(new PropertyValueFactory<>("programStudi"));
        tableStudent.getColumns().addAll(nameCol,nimCol,facultyCol,prodiCol);

        Button backBtn = new Button("Back");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        final VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(20, 10, 10, 10));
        vbox.getChildren().addAll(label, tableStudent);

        // Add VBox with table and label to the GridPane
        gridPane.add(vbox, 0, 0);

        // Add button to GridPane at bottom right
        GridPane.setHalignment(backBtn, HPos.RIGHT);
        gridPane.add(backBtn, 0, 1);

        // Create and set the scene
        Scene scene = new Scene(gridPane, UIManager.getWidth(), UIManager.getHeight());
        ArrayList<PropertyStudent> convertStudent = PropertyStudent.studentToProperty(Admin.getStudentData());
        final ObservableList<PropertyStudent> data = FXCollections.observableArrayList(convertStudent);

        backBtn.setOnAction(e -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        tableStudent.setItems(data);
        stage.setScene(scene);
        stage.setTitle("TABLE REGISTERED STUDENT");
        stage.show();
    }

    public void inputBook(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Add Book");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label categoryLabel = new Label("Category:");
        grid.add(categoryLabel, 0, 1);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("History", "Text", "Story");
        categoryComboBox.setPromptText("Select category");
        grid.add(categoryComboBox, 1, 1);

        Label title = new Label("Title :");
        grid.add(title, 0, 2); // Kolom 0, Baris 1
        TextField fieldTitle = new TextField();
        fieldTitle.setPromptText("Enter book title");
        grid.add(fieldTitle, 1, 2); // Kolom 1, Baris 1

        Label author = new Label("Author : ");
        grid.add(author, 0, 3);
        TextField fieldAuthor = new TextField();
        fieldAuthor.setPromptText("Enter book author");
        grid.add(fieldAuthor, 1, 3);

        Label stock = new Label("Stock");
        grid.add(stock,0,4);
        TextField fieldStock = new TextField();
        fieldStock.setPromptText("Enter book stock");
        grid.add(fieldStock,1,4);

        Button btnSubmit = new Button("SUBMIT");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSubmit.setOnAction(actionEvent -> {
            String categoryValue = categoryComboBox.getValue();
            if (categoryValue == null || fieldAuthor.getText().isEmpty() || fieldTitle.getText().isEmpty()|| fieldStock.getText().isEmpty())
                UIManager.showError(actionTarget,"PLEASE FILL ALL BLANKS");
            else {
                try {
                    Book book;
                    if(categoryValue.equals("Text"))
                        book = new TextBook(this.generateId(),fieldTitle.getText(),fieldAuthor.getText(),Integer.parseInt(fieldStock.getText()));
                    else if(categoryValue.equals("History"))
                        book = new HistoryBook(this.generateId(),fieldTitle.getText(),fieldAuthor.getText(),Integer.parseInt(fieldStock.getText()));
                    else
                        book = new StoryBook(this.generateId(),fieldTitle.getText(),fieldAuthor.getText(),Integer.parseInt(fieldStock.getText()));
                    super.addBook(book);
                    UIManager.showSuccess(actionTarget,"BOOK SUCESSFLY ADDED");
                }catch (Exception e){
                    UIManager.showError(actionTarget,e.getMessage());
                }
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("ADD BOOK MENU");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void updateBooks(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("EDIT BOOK");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(getBookList());
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        Label id = new Label("ID");
        TextField fieldId = new TextField();
        fieldId.setPromptText("Enter book Id");
        grid.add(id,0,2);
        grid.add(fieldId,1,2);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        HBox hBBtn = new HBox(10);
        Button btnReturn = new Button("EDIT BOOK INFORMATION");
        Button btnBack = new Button("BACK");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack,btnReturn);
        grid.add(hBBtn,1,5);

        btnReturn.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            Book book = this.searchBookAll(fieldId.getText());
            if (book == null) {
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
            }else {
                changeBook(stage,book);
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("EDIT BOOK MEMU");
        stage.setScene(scene);
        stage.show();
    }

    public void displayBook(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("AVAILABLE BOOKS");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = super.createTableView(getBookList());
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        HBox hBBtn = new HBox(10);
        Button btnBack = new Button("BACK");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack);
        grid.add(hBBtn,1,2);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 3);

        btnBack.setOnAction(e -> {
            stage.close();
            menu(stage);
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        stage.setTitle("DISPLAY AVAILABE BOOKS");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void logOut(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("YOU ARE LOGOUT FROM ADMIN");

        ButtonType yesButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(yesButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton){
            stage.close();
            try {
                Main.menu(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changeBook(Stage stage,Book book){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Add Book");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label categoryLabel = new Label("Category:");
        grid.add(categoryLabel, 0, 1);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("History", "Text", "Story");
        categoryComboBox.setPromptText("Select category");
        grid.add(categoryComboBox, 1, 1);

        Label title = new Label("Title :");
        grid.add(title, 0, 2); // Kolom 0, Baris 1
        TextField fieldTitle = new TextField();;
        fieldTitle.setText(book.getTitle());
        grid.add(fieldTitle, 1, 2); // Kolom 1, Baris 1

        Label author = new Label("Author : ");
        grid.add(author, 0, 3);
        TextField fieldAuthor = new TextField();
        fieldAuthor.setText(book.getAuthor());
        grid.add(fieldAuthor, 1, 3);

        Label stock = new Label("Stock");
        grid.add(stock,0,4);
        TextField fieldStock = new TextField();
        fieldStock.setText(String.valueOf(book.getStock()));
        //fieldStock.setPromptText("Enter book stock");
        grid.add(fieldStock,1,4);

        Button btnSubmit = new Button("SAVE");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSubmit.setOnAction(actionEvent -> {
            String categoryValue = categoryComboBox.getValue();
            if (categoryValue == null || fieldAuthor.getText().isEmpty() || fieldTitle.getText().isEmpty()|| fieldStock.getText().isEmpty())
                UIManager.showError(actionTarget,"PLEASE FILL ALL BLANKS");
            else {
                try {
                    book.setTitle(fieldTitle.getText());
                    book.setAuthor(fieldAuthor.getText());
                    book.setStock(Integer.parseInt(fieldStock.getText()));
                    book.setCategory(fieldAuthor.getText());
                    UIManager.showSuccess(actionTarget,"BOOK SUCESSFLY ADDED");
                    stage.close();
                    menu(stage);
                }catch (Exception e){
                    UIManager.showError(actionTarget,e.getMessage());
                }
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("CHANGE BOOK INFORMATION MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void addStudent(String name,String NIM, String faculy, String program){
        Student student = new Student(name,NIM,faculy,program);
        studentData.add(student);
        studentList.add(NIM);
    }
    public boolean isAdmin(String username, String pass) throws illegalAdminAcces {
        if (username.equals(getAdminUserName()) && pass.equals(getAdminPassword()))
            return true;
         else
            throw new illegalAdminAcces("Invalid Credentials");
    }
    public String generateId(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int part = random.nextInt(0xFFFF + 1);
            sb.append(String.format("%04x", part));
            if (i < 3) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
    public String getAdminUserName() {
        return adminUserName;
    }
    public static ArrayList<Student> getStudentData() {
        return studentData;
    }
    public static Book searchBookAll(String id){
        for (Book book : Admin.getBookList())
            if(book.getBookId().equals(id))
                return book;
        return null;
    }
    public String getAdminPassword() {
        return adminPassword;
    }
}