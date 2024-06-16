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
import java.lang.reflect.Array;
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
    private static String editTime;
    private static final ArrayList<Student> studentData = new ArrayList<>();
    private static final ArrayList<String> studentList = new ArrayList<>();
    private static ArrayList<String> visitorList = new ArrayList<>();



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
        Button btnPredictVisitor = new Button("Predict Visitor");
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
            displayPredictVisitor(stage);
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("ADMIN MENU");
        stage.setScene(scene);
        stage.show();
    }

    private void displayPredictVisitor(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());
        // Step 1: Get predicted visitor data using getPredictVisitor method
        String[][] predictedData = getPredictVisitor();

        // Step 2: Set up JavaFX stage and scene
        stage.setTitle("Predicted Visitor Bar Chart");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        VBox hbboxBtn = new VBox(10);

        // Step 3: Prepare data for the chart
        ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();

        for (int i = 0; i < predictedData.length; i++) {
            String date = predictedData[i][0];
            int visitors = Integer.parseInt(predictedData[i][1]);
            barChartData.add(new XYChart.Data<>(date, visitors));
        }

        // Step 4: Set up the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Visitor Count");

        // Step 5: Set up the bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Predicted Visitor Count per Date");

        // Step 6: Add data to the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Visitors");
        series.setData(barChartData);

        barChart.getData().add(series);
        grid.add(barChart, 0, 0);

        // Step 7: Create a back button (optional)
        Button btnBack = new Button("BACK");
        hbboxBtn.getChildren().addAll(btnBack);
        hbboxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbboxBtn, 0, 1);
        btnBack.setOnAction(event -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        // Step 8: Display the scene
        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight()); // Adjust size as needed
        stage.setScene(scene);
        stage.show();
    }

    private String[][] getPredictVisitor() {
        Map<String, Integer> visitorCountMap = new TreeMap<>(); // TreeMap will sort keys (dates) naturally

        for (String entry : visitorList) {
            String[] parts = entry.split(" ");
            String date = parts[0];
            visitorCountMap.put(date, visitorCountMap.getOrDefault(date, 0) + 1);
        }
        // Save the visitor count data to an array
        String[][] visitorDataArray = saveVisitorDataToArray(visitorCountMap);
        double[] numVisitor = new double[visitorDataArray.length+1];
        double[] days = new double[visitorDataArray.length+1];

        String[][] newArray = new String[6][2];

        for (int i = 0; i < visitorDataArray.length; i++) {
            String[] row = visitorDataArray[i];
            int visitors = Integer.parseInt(row[1]);
            numVisitor[i] = visitors;
            days[i] = i+1;
            if (i == visitorDataArray.length-1) {
                newArray[0][0] = visitorDataArray[i][0]; // Salin tanggal
                newArray[0][1] = visitorDataArray[i][1]; // Salin jumlah pengunjung
            }
        }

        // Menghitung rata-rata dari days dan visitors
        double meanDays = mean(days);
        double meanVisitors = mean(numVisitor);

        // Menghitung koefisien m (slope) dari regresi linear
        double m = calculateSlope(days, numVisitor, meanDays, meanVisitors);

        // Menghitung intercept c dari regresi linear
        double c = calculateIntercept(meanDays, meanVisitors, m);

        double dayToPredict,predictedVisitors;

        editTime = getDate();
        for(int i = 0; i < 5;i++){
            incrementDate();
            dayToPredict = visitorDataArray.length+i+1;
            predictedVisitors = predict(m, c, dayToPredict);
            int convert = (int) predictedVisitors;
            newArray[i+1][0] = getEditTime(); // Salin jumlah pengunjung
            newArray[i+1][1] = String.valueOf(convert); // Salin jumlah pengunjun


        }
        return newArray;
    }
    public static void incrementDate(){
        LocalDate convertDate = LocalDate.parse(getEditTime());
        // Menambahkan satu hari
        LocalDate newDate = convertDate.plusDays(1);
        setEditTime(newDate.toString());
    }

    // Fungsi untuk menghitung rata-rata
    private static double mean(double[] data) {
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        return sum / data.length;
    }

    // Fungsi untuk menghitung koefisien m (slope)
    private static double calculateSlope(double[] days, double[] visitors, double meanDays, double meanVisitors) {
        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < days.length; i++) {
            numerator += (days[i] - meanDays) * (visitors[i] - meanVisitors);
            denominator += Math.pow((days[i] - meanDays), 2);
        }
        return numerator / denominator;
    }

    // Fungsi untuk menghitung intercept c
    private static double calculateIntercept(double meanDays, double meanVisitors, double m) {
        return meanVisitors - (m * meanDays);
    }

    // Fungsi untuk melakukan prediksi
    private static double predict(double m, double c, double day) {
        return m * day + c;
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

    public static String[][] saveVisitorDataToArray(Map<String, Integer> visitorCountMap) {
        // Membuat array dua dimensi untuk menyimpan data (baris x 2 kolom)
        String[][] visitorDataArray = new String[visitorCountMap.size()][2];
        int i = 0;

        for (Map.Entry<String, Integer> entry : visitorCountMap.entrySet()) {
            visitorDataArray[i][0] = entry.getKey(); // Tanggal
            visitorDataArray[i][1] = String.valueOf(entry.getValue()); // Jumlah pengunjung
            i++;
        }
        return visitorDataArray;
    }

    public void displayVisitor(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene()); // SAVE PREVIOUS SCENE
        stage.setTitle("Visitor Bar Chart");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        VBox hbboxBtn = new VBox(10);

        // Counting visitors per date using a map
        Map<String, Integer> visitorCountMap = new TreeMap<>(); // TreeMap will sort keys (dates) naturally

        for (String entry : visitorList) {
            String[] parts = entry.split(" ");
            String date = parts[0];
            visitorCountMap.put(date, visitorCountMap.getOrDefault(date, 0) + 1);
        }

        // Save the visitor count data to an array
        String[][] visitorDataArray = saveVisitorDataToArray(visitorCountMap);

        // Prepare data for the chart, already sorted by date
        ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();

        for (String[] entry : visitorDataArray) {
            barChartData.add(new XYChart.Data<>(entry[0], Integer.parseInt(entry[1])));
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
        grid.add(barChart, 0, 0);

        // Create a back button
        Button btnBack = new Button("BACK");
        hbboxBtn.getChildren().addAll(btnBack);
        hbboxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbboxBtn, 0, 1);
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
    public static void updateDate(){
        LocalDate currentDate = LocalDate.parse(getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate nextDay = currentDate.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        setDate(nextDay.format(formatter));
    }

    public static String getEditTime() {
        return editTime;
    }
    public static void setEditTime(String editTime) {
        Admin.editTime = editTime;
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