package org.example.com.main.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Admin extends User implements IMenu {
    private TableView tableStudent = new TableView<>();
    private final String adminUserName = "admin";
    private final String adminPassword = "admin";
    private static String date;
    private static String time;
    private static final ArrayList<Student> studentData = new ArrayList<>();
    private static final ArrayList<String> studentList = new ArrayList<>();
    private static ArrayList<String> visitorList = new ArrayList<>();

    public static void updateDate(){
        LocalDate currentDate = LocalDate.parse(getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate nextDay = currentDate.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        setDate(nextDay.format(formatter));
    }

    public static String getTime() {
        return time;
    }

    public static void setDate(String newDate){
        date = newDate;
    }


    public static void firstDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        setDate(now.format(formatter));
    }

    public static void setTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        time = now.format(formatter);
    }

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
        Button btnDisplayVisitor = new Button("Display Visitor");
        Button btnDisplayEachFaculty = new Button("Display Visitor faculty");
        Button btnPredictVisitor = new Button("PREDICT VISITOR");
        Button btnClose = new Button("Close Library");
        Button btnLogOut = new Button("Log Out");
        hBBtn.setAlignment(Pos.CENTER);
        hBBtn.getChildren().addAll(btnAddStudent,btnAddBook,btnDisplayStudent,btnDisplayBook,btnEditBook,btnDisplayVisitor,btnDisplayEachFaculty,btnLogOut,btnPredictVisitor,btnClose);
        grid.add(hBBtn,1,3);

        double buttonWidth = 170; // Tentukan lebar tombol
        double buttonHeight = 30; // Tentukan tinggi tombol
        btnAddStudent.setPrefSize(buttonWidth, buttonHeight);
        btnAddBook.setPrefSize(buttonWidth, buttonHeight);
        btnDisplayBook.setPrefSize(buttonWidth, buttonHeight);
        btnDisplayStudent.setPrefSize(buttonWidth, buttonHeight);
        btnEditBook.setPrefSize(buttonWidth,buttonHeight);
        btnLogOut.setPrefSize(buttonWidth, buttonHeight);
        btnDisplayVisitor.setPrefSize(buttonWidth,buttonHeight);
        btnDisplayEachFaculty.setPrefSize(buttonWidth,buttonHeight);
        btnPredictVisitor.setPrefSize(buttonWidth,buttonHeight);
        btnClose.setPrefSize(buttonWidth,buttonHeight);

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

        btnClose.setOnAction(actionEvent -> {
            Admin.updateDate();
            logOut(stage);
        });

        btnLogOut.setOnAction(actionEvent -> {
            logOut(stage);
        });

        btnEditBook.setOnAction(actionEvent -> {
            updateBooks(stage);
        });

        btnDisplayVisitor.setOnAction(actionEvent -> {
            displayVisitor(stage);

        });

        btnDisplayEachFaculty.setOnAction(actionEvent -> {
            displayEachFaculty(stage);
        });

        btnPredictVisitor.setOnAction(actionEvent -> {
            predictVisitor(stage);
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("ADMIN MENU");
        stage.setScene(scene);
        stage.show();
    }

    private void predictVisitor(Stage stage) {
        ArrayList<String> visitorList = new ArrayList<>();
        visitorList.add("2024-06-15 18:16:37 100"); // 100 is the visitor ID
        visitorList.add("2024-06-16 18:20:37 101");
        visitorList.add("2024-06-17 18:16:37 102"); // 102 is the visitor ID
        visitorList.add("2024-06-18 18:20:37 103");
        visitorList.add("2024-06-19 18:25:37 104");

        // Counting visitors per date using a map
        Map<LocalDate, Integer> visitorCountMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String entry : visitorList) {
            String[] parts = entry.split(" ");
            String dateString = parts[0] + " " + parts[1];
            LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            visitorCountMap.put(date, visitorCountMap.getOrDefault(date, 0) + 1);
        }

        // Prepare data for regression
        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : visitorCountMap.entrySet()) {
            double x = entry.getKey().toEpochDay(); // Use epoch day as x value (ordinal date)
            double y = entry.getValue();
            xValues.add(x);
            yValues.add(y);
        }

        // Perform linear regression
        double[] regressionParameters = calculateLinearRegression(xValues, yValues);

        // Calculate predicted values for plotting the regression line
        double minX = xValues.get(0);
        double maxX = xValues.get(xValues.size() - 1);
        double minY = regressionParameters[0] + regressionParameters[1] * minX;
        double maxY = regressionParameters[0] + regressionParameters[1] * maxX;

        // Create chart
        NumberAxis xAxis = new NumberAxis("Date (Epoch Day)", minX, maxX, 1);
        NumberAxis yAxis = new NumberAxis("Visitor Count", minY, maxY, 1);
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Visitor Count Regression");

        // Add historical data as scatter points
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Historical Data");
        for (int i = 0; i < xValues.size(); i++) {
            series.getData().add(new XYChart.Data<>(xValues.get(i), yValues.get(i)));
        }

        // Add regression line
        XYChart.Series<Number, Number> regressionLine = new XYChart.Series<>();
        regressionLine.setName("Regression Line");
        regressionLine.getData().add(new XYChart.Data<>(minX, minY));
        regressionLine.getData().add(new XYChart.Data<>(maxX, maxY));

        scatterChart.getData().addAll(series, regressionLine);

        // Display chart in a VBox
        VBox vbox = new VBox(scatterChart);
        Scene scene = new Scene(vbox, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    private double[] calculateLinearRegression(List<Double> xValues, List<Double> yValues) {
        int n = xValues.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;

        for (int i = 0; i < n; i++) {
            double x = xValues.get(i);
            double y = yValues.get(i);
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXX += x * x;
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;

        return new double[]{intercept, slope};
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

    public void displayEachFaculty(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        VBox hbboxBtn = new VBox(10);

        // Counting students per faculty using a map
        Map<String, Integer> facultyCountMap = new HashMap<>();

        //int totalStudents = studentList.size(); // Total number of students
        int totalVisitor = visitorList.size();
        for (String student : visitorList){
            System.out.println(student);
            String[] parts = student.split(" ");
            for (Student findX : studentData){;
                if (parts[2].equals(findX.getNIM())){
                    facultyCountMap.put(findX.getFaculty(),facultyCountMap.getOrDefault(findX.getFaculty(),0)+1);
                    break;
                }
            }
        }
        // Prepare data for the pie chart including percentages
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : facultyCountMap.entrySet()) {
            String faculty = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalVisitor;
            pieChartData.add(new PieChart.Data(faculty + " (" + String.format("%.2f", percentage) + "%)", count));
        }

        // Set up the pie chart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Percentage of Students by Faculty");
        Button btnBack = new Button("BACK");
        grid.add(pieChart,0,0);

        hbboxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbboxBtn.getChildren().addAll(btnBack);
        grid.add(hbboxBtn,0,1);


        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        stage.setScene(scene);
        stage.show();

    }

    public void displayVisitor(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene()); // SAVE PREVIOUS SCENE
        stage.setTitle("Visitor Bar Chart");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        VBox hbboxBtn = new VBox(10);

        // Counting visitors per date using a map
        Map<String, Integer> visitorCountMap = new TreeMap<>(); // TreeMap will sort keys (dates) naturally

        for (String entry : visitorList) {
            String[] parts = entry.split(" ");
            String date = parts[0];
            visitorCountMap.put(date, visitorCountMap.getOrDefault(date, 0) + 1);
        }

        // Prepare data for the chart, already sorted by date
        ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : visitorCountMap.entrySet()) {
            barChartData.add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Set up the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Visitor Count");

        // Set up the bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Visitor Count per Date");

        // Add data to the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Visitors");
        series.setData(barChartData);

        barChart.getData().add(series);
        grid.add(barChart,0,0);

        // Create a back button
        Button btnBack = new Button("BACK");
        hbboxBtn.getChildren().addAll(btnBack);
        hbboxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbboxBtn,0,1);
        btnBack.setOnAction(event -> {
            stage.setScene(UIManager.getPreviousLayout());

        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());

        stage.setScene(scene);
        stage.show();
    }

    public void addStudent(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());
        // Sample faculty to program mapping
        Map<String, String[]> facultyToPrograms = new HashMap<>();
        facultyToPrograms.put("FEB", new String[]{"MANAJEMEN", "AKUNTANSI", "EKONOMI PEMBANGUNAN"});
        facultyToPrograms.put("FISIP", new String[]{"ILMU KOMUNIKASI", "HUBUNGAN INTERNASIONAL", "ILMU PEMERINTAHAN"});
        facultyToPrograms.put("FKIP", new String[]{"PENDIDIKAN MATEMATIKA", "PENDIDIKAN BIOLOGI", "PENDIDIKANN BAHASA INDONESIA"});
        facultyToPrograms.put("FT",new String[]{"SIPIL,INFORMATIKA","INDUSTRI","MESIN","ELEKTRO"});
        // Set up the UI
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Add Student");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label name = new Label("Name:");
        grid.add(name, 0, 1);
        TextField inputName = new TextField();
        inputName.setPromptText("Enter student name");
        grid.add(inputName, 1, 1);

        Label NIM = new Label("NIM:");
        grid.add(NIM, 0, 2);
        TextField inputNIM = new TextField();
        inputNIM.setPromptText("Enter student NIM");
        grid.add(inputNIM, 1, 2);

        Label faculty = new Label("Faculty:");
        grid.add(faculty, 0, 3);
        ComboBox<String> facultyComboBox = new ComboBox<>();
        facultyComboBox.getItems().addAll(facultyToPrograms.keySet());
        grid.add(facultyComboBox, 1, 3);

        Label program = new Label("Program:");
        grid.add(program, 0, 4);
        ComboBox<String> programComboBox = new ComboBox<>();
        grid.add(programComboBox, 1, 4);

        facultyComboBox.setOnAction(e -> {
            String selectedFaculty = facultyComboBox.getValue();
            programComboBox.getItems().clear();
            if (selectedFaculty != null) {
                programComboBox.getItems().addAll(facultyToPrograms.get(selectedFaculty));
            }
        });

        Button btnSubmit = new Button("SUBMIT");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200);
        grid.add(actionTarget, 1, 6);

        Scene scene = new Scene(grid, 400, 400);
        stage.setScene(scene);
        stage.show();

        btnBack.setOnAction(event -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        btnSubmit.setOnAction(event -> {
            Student student = checkNIM(inputName.getText(),inputNIM.getText(),facultyComboBox.getValue(),programComboBox.getValue());
            if (inputName.getText().isEmpty() || inputNIM.getText().isEmpty() || facultyComboBox.getValue().isEmpty()||programComboBox.getValue().isEmpty())
                UIManager.showError(actionTarget,"PLEASE FILL ALL BLANKS");
            else if(inputNIM.getText().length()< 15)
                UIManager.showError(actionTarget,"NIM MUST 15 CHARACTERS");
            else if(student == null)
                UIManager.showError(actionTarget,"NIM SAME");
            else {
                addTempStudent(this,inputName.getText(),inputNIM.getText(),facultyComboBox.getValue(),programComboBox.getValue());
                UIManager.showSuccess(actionTarget,"STUDENT ADDED SUCCESSFULY");
            }
        });
    }

//    public void addStudent(Stage stage){
//        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10); // Jarak horizontal antar kolom
//        grid.setVgap(10); // Jarak vertikal antar baris
//        grid.setPadding(new Insets(25, 25, 25, 25));
//        Text sceneTitle = new Text("Add Student");
//        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1
//
//        Label name = new Label("Name :");
//        grid.add(name, 0, 1); // Kolom 0, Baris 1
//        TextField inputName = new TextField();
//        inputName.setPromptText("Enter student name");
//        grid.add(inputName, 1, 1); // Kolom 1, Baris 1
//
//        Label NIM = new Label("NIM : ");
//        grid.add(NIM, 0, 2);
//        TextField inputNIM = new TextField();
//        inputNIM.setPromptText("Enter student NIM");
//        grid.add(inputNIM, 1, 2);
//
//        Label faculty = new Label("Faculty");
//        grid.add(faculty,0,3);
//        TextField inputFaculty = new TextField();
//        inputFaculty.setPromptText("Enter student faculty");
//        grid.add(inputFaculty,1,3);
//
//        Label program = new Label("Program");
//        grid.add(program,0,4);
//        TextField inputProgram = new TextField();
//        inputProgram.setPromptText("Enter student program");
//        grid.add(inputProgram,1,4);
//
//        Button btnSubmit = new Button("SUBMIT");
//        Button btnBack = new Button("BACK");
//        HBox hBBtn = new HBox(10);
//        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
//        hBBtn.getChildren().addAll(btnBack, btnSubmit);
//        grid.add(hBBtn, 1, 5);
//
//        final Text actionTarget = new Text();
//        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
//        grid.add(actionTarget, 1, 6);
//
//        btnSubmit.setOnAction(actionEvent -> {
//            Student student = Main.checkNIM(inputName.getText(),inputNIM.getText(),inputFaculty.getText(),inputProgram.getText());
//            if (inputName.getText().isEmpty() || inputNIM.getText().isEmpty() || inputFaculty.getText().isEmpty()||inputProgram.getText().isEmpty())
//                UIManager.showError(actionTarget,"PLEASE FILL ALL BLANKS");
//            else if(inputNIM.getText().length()< 15)
//                UIManager.showError(actionTarget,"NIM MUST 15 CHARACTERS");
//            else if(student == null)
//                UIManager.showError(actionTarget,"NIM SAME");
//            else {
//                Main.addTempStudent(this,inputName.getText(),inputNIM.getText(),inputFaculty.getText(),inputProgram.getText());
//                UIManager.showSuccess(actionTarget,"STUDENT ADDED SUCCESSFULY");
//            }
//        });
//
//        btnBack.setOnAction(actionEvent -> {
//            stage.setScene(UIManager.getPreviousLayout());
//        });
//
//        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
//        stage.setTitle("ADD STUDENT MENU");
//        stage.setScene(scene);
//        stage.show();
//    }

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

    public static void addVisitor(String student){
        visitorList.add(student);
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

    public static String getDate() {
        return date;
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