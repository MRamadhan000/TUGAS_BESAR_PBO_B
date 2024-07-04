package org.example.com.main.data;
import com.itextpdf.text.*;
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
import javafx.scene.paint.Color;
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
import org.apache.commons.math3.stat.regression.SimpleRegression;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.itextpdf.text.pdf.PdfWriter;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Admin extends User implements IMenu {
    private TableView tableStudent = new TableView<>();
    private final String adminUserName = "admin";
    private final String adminPassword = "admin";
    private static String date;
    private static String time;
    private static String editTime;
    private static final ArrayList<Student> studentData = new ArrayList<>();
    private static final ArrayList<String> studentList = new ArrayList<>();
    private static ArrayList<String> visitorList = new ArrayList<>(); // date time NIM
    private static ArrayList<String> listBorrowedBook = new ArrayList<>();
    private static ArrayList<String> bookLoad = new ArrayList<>(); // NIM,bookId,duration
    public static ArrayList<String> getBookLoad() {
        return bookLoad;
    }

    public static void logIn(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Log In Admin");
        sceneTitle.setFont(Font.font("Sans-serif", FontWeight.BOLD, 25));
        sceneTitle.setFill(Color.web("#1b4332")); // Hexadecimal color
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label userName = new Label("Username:");
        userName.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        userName.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size

        Label password = new Label("Password : ");
        password.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        password.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(userName, 0, 1); // Kolom 0, Baris 1
        grid.add(password, 0, 2); // Kolom 0 Baris 2

        TextField inputUserName = new TextField();
        inputUserName.setPromptText("Enter your username");
        inputUserName.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        grid.add(inputUserName, 1, 1); // Kolom 1, Baris 1

        PasswordField inputPassword = new PasswordField();
        inputPassword.setPromptText("Enter your password");
        inputPassword.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        grid.add(inputPassword, 1, 2);

        Button btnSignIn = new Button("SIGN IN");
        Button btnBack = new Button("BACK");
        btnSignIn.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);

        btnSignIn.setStyle(UIManager.styleSecondary);
        btnBack.setStyle(UIManager.styleSecondary);

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

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        grid.setStyle(UIManager.primaryColour);
        stage.setTitle("LOGIN ADMIN");
        stage.setScene(scene);
        stage.show();
    }

    public static void addToLoadBook(String str){
        System.out.println("DAFTAR BUKU ANTRIAN " + str);
        bookLoad.add(str);
    }

    public static void addToListBorrowed(String str){
        listBorrowedBook.add(str);
        System.out.println("Buku berhasil ditambah dengan format " + str);
    }

    public static boolean isHaveBookLoad(String bookId){
        for(String x : bookLoad){
            String[] split = x.split(",");
            if (split[1].equals(bookId)){
                return true;
            }
        }
        return false;
    }

    public static void removeListBorrowed(String NIM, String bookId){
        for (String content : listBorrowedBook) {
            System.out.println("NILAI X SEBELUM DIUBAH " + content);
        }

        try {
            for (String x : listBorrowedBook){
                String[] str = x.split(",");
                if (str[0].equals(NIM) && str[1].equals(bookId)) {
                    System.out.println("Berhasil terhapus 1");
                    listBorrowedBook.remove(x);
                    System.out.println("Berhasil terhapus 2");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        for (String content1: listBorrowedBook) {
            System.out.println("NILAI X Sesudah DIUBAH " + content1);
        }
    }

    //MENGECEK APAKAH ADA SISA DURASI 3 HARI
    public void checkDurationBookBorrowed(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        try {
            for (String student : listBorrowedBook){
                String[] str = student.split(",");
                LocalDate currentTime = LocalDate.parse(Admin.getDate(), formatter);
                LocalDate deadlineDate = LocalDate.parse(str[3], formatter);
                long daysBetween = ChronoUnit.DAYS.between(currentTime, deadlineDate);
                if (daysBetween == 3 ){
                    loadAccount(str[0],str[1],str[3]);
                }
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    public static String getWaitBorrowedBook(String bookId){
        try {
            for (String student : listBorrowedBook){
                String[] str = student.split(",");
                if (str[1].equals(bookId) ){
                    return str[3];
                }
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }


    //MENCARI AKUN DAN KIRIM EMAIL
    public void loadAccount(String NIM, String bookId,String deadline){
        for (Student student : studentData){
            if (student.getNIM().equals(NIM)){
                String filePath = "src/main/java/org/example/com/main/attachment/letter.pdf";
                String message = setEmailMessage(student.getNIM(),student.getFaculty(),student.getProgramStudi(),bookId,deadline);
                try {
                    createPdf(filePath,deadline,student.getName(),student.getNIM(),student.getProgramStudi(),bookId);
                }catch (Exception e){
                    System.err.println(e.getMessage());
                }
                sendEmail(message,filePath);
            }
        }
    }
    public static void sendEmail(String message, String filePath) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        String mainEmail = "";
        String password = "";

        // Create a Session with the specified properties
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mainEmail, password);
            }
        });

        // Email details
        String toEmail = "";
        String subject = "PERPUSTAKAAN";
        String body = message;

        try {
            MimeMessage msg = new MimeMessage(session);
            // Set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(mainEmail, "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse(mainEmail, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            // Create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Second part is the attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("PemberitahuanPemainjaman.pdf");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            msg.setContent(multipart);

            // Send message
            Transport.send(msg);
            System.out.println("Email Sent Successfully with attachment!!");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPdf(String dest, String tanggal, String namaPeminjam, String nim, String jurusan, String bookId) throws DocumentException, IOException {
        if (!dest.toLowerCase().endsWith(".pdf")) {
            dest += ".pdf";
        }
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        // Add Logo
        Image logo = Image.getInstance("src/main/resources/Image/logo_umm_attachment.jpg");
        logo.scaleToFit(150, 150); // Sesuaikan ukuran logo sesuai kebutuhan
        float logoX = (PageSize.A4.getWidth() - logo.getScaledWidth()) / 2;
        float logoY = PageSize.A4.getHeight() - logo.getScaledHeight() - 50; // Sesuaikan posisi logo sesuai kebutuhan
        logo.setAbsolutePosition(logoX, logoY);
        document.add(logo);

        // Add blank lines
        document.add(new Paragraph("\n\n\n\n\n\n\n"));

        // Set Font
        com.itextpdf.text.Font fontJudul = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        com.itextpdf.text.Font fontIsi = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);

        // Judul Surat
        Paragraph judul = new Paragraph("Pemberitahuan Buku Akan Habis Tenggang", fontJudul);
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);

        // Tanggal
        Paragraph parTanggal = new Paragraph("Tanggal: " + tanggal, fontIsi);
        parTanggal.setAlignment(Element.ALIGN_RIGHT);
        document.add(parTanggal);

        // Alamat Institusi
        Paragraph parInstitusi = new Paragraph("PERPUSTKAAN UNIVERSITAS MUHAMMADIYAH MALANG" + "\n" + "Jl. Raya Tlogomas No. 246 Malang - Jawa Timur 65144", fontIsi);
        parInstitusi.setAlignment(Element.ALIGN_LEFT);
        document.add(parInstitusi);

        // Spasi
        document.add(new Paragraph("\n"));

        // Isi Surat
        Paragraph isiSurat = new Paragraph();
        isiSurat.add(new Phrase("Kepada,\n\n", fontIsi));
        isiSurat.add(new Phrase(namaPeminjam + "\n", fontIsi));
        isiSurat.add(new Phrase("NIM: " + nim + "\n", fontIsi));
        isiSurat.add(new Phrase("Jurusan: " + jurusan + "\n", fontIsi));
        isiSurat.add(new Phrase("Book ID: " + bookId + "\n\n", fontIsi));
        isiSurat.add(new Phrase("Dengan Hormat,\n\n", fontIsi));
        isiSurat.add(new Phrase("Kami menginformasikan bahwa buku yang Anda pinjam dengan detail sebagai berikut:\n\n", fontIsi));
        isiSurat.add(new Phrase("Nama Peminjam: " + namaPeminjam + "\n", fontIsi));
        isiSurat.add(new Phrase("NIM: " + nim + "\n", fontIsi));
        isiSurat.add(new Phrase("Jurusan: " + jurusan + "\n", fontIsi));
        isiSurat.add(new Phrase("Book ID: " + bookId + "\n", fontIsi));
        isiSurat.add(new Phrase("akan segera mencapai batas waktu tenggang peminjaman. Kami mohon untuk segera mengembalikan buku tersebut ke perpustakaan pada waktu yang telah ditentukan.\n\n", fontIsi));
        isiSurat.add(new Phrase("Terima kasih atas perhatian dan kerjasamanya.\n\n", fontIsi));
        isiSurat.add(new Phrase("Hormat kami,\n\n", fontIsi));
        isiSurat.add(new Phrase("[AGUS]\n", fontIsi));
        isiSurat.add(new Phrase("[Ketua Perpustakaan UMM]\n", fontIsi));
        document.add(isiSurat);
        document.close();
        System.out.println("PDF Created Successfully: " + dest);
    }

    public static String setEmailMessage(String NIM,String faculty,String programStudi,String bookId, String deadline){
        return String.format(
                "Peringatan:\n" + "Mahasiswa dengan NIM %s dari Fakultas %s, Program Studi %s,\n" + "peminjaman buku dengan ID %s akan habis pada tanggal %s.\n" + "Harap segera mengembalikan buku tersebut sebelum atau pada tanggal yang ditentukan untuk menghindari denda keterlambatan.", NIM, faculty, programStudi, bookId, deadline
        );
    }

    @Override
    public void menu(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Admin Menu");
        sceneTitle.setFont(Font.font("Sans-serif", FontWeight.EXTRA_BOLD, 22));
        //grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

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

        btnAddStudent.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnAddBook.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnDisplayBook.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnDisplayStudent.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnEditBook.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());
        btnLogOut.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnDisplayVisitor.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());
        btnDisplayEachFaculty.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());
        btnPredictVisitor.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());
        btnClose.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());

        btnAddStudent.setStyle(UIManager.stylePrimary);
        btnAddBook.setStyle(UIManager.stylePrimary);
        btnDisplayBook.setStyle(UIManager.stylePrimary);
        btnDisplayStudent.setStyle(UIManager.stylePrimary);
        btnEditBook.setStyle(UIManager.stylePrimary);
        btnLogOut.setStyle(UIManager.stylePrimary);
        btnDisplayVisitor.setStyle(UIManager.stylePrimary);
        btnDisplayEachFaculty.setStyle(UIManager.stylePrimary);
        btnPredictVisitor.setStyle(UIManager.stylePrimary);
        btnClose.setStyle(UIManager.stylePrimary);

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
            checkDurationBookBorrowed();
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
        grid.setStyle(UIManager.primaryColour);
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
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setStyle(UIManager.styleSecondary);
        hbboxBtn.getChildren().addAll(btnBack);
        hbboxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbboxBtn, 0, 1);
        btnBack.setOnAction(event -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        // Step 8: Display the scene
        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight()); // Adjust size as needed
        grid.setStyle(UIManager.primaryColour);
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

        SimpleRegression regression = new SimpleRegression();

        for (int i = 0; i < visitorDataArray.length; i++) {
            try {
                String[] row = visitorDataArray[i];
                int visitors = Integer.parseInt(row[1]);
                numVisitor[i] = visitors;
                days[i] = i + 1;

                regression.addData(i + 1, visitors);

                if (i == visitorDataArray.length - 1) {
                    newArray[0][0] = visitorDataArray[i][0]; // Copy the last date
                    newArray[0][1] = visitorDataArray[i][1]; // Copy the last visitor count
                }
                System.out.println("VISITOR HARI KE " + (i+1) + " berjumlah " +visitors + " dengan tanggal " + visitorDataArray[i][1]);

            }catch (IndexOutOfBoundsException e){
                System.err.println(e.getMessage());
            }
        }
        double dayToPredict, predictedVisitors;
        editTime = getDate();
        for (int i = 0; i < 5; i++) {
            incrementDate();
            dayToPredict = visitorDataArray.length + i + 1;
            predictedVisitors = regression.predict(dayToPredict);
            System.out.println(predictedVisitors);
            if (predictedVisitors < 0) {
                predictedVisitors = 0;
            }

            int convert = (int) Math.round(predictedVisitors); // Convert to integer

            newArray[i + 1][0] = getEditTime(); // Copy the predicted date
            newArray[i + 1][1] = String.valueOf(convert); // Copy the predicted visitor count
        }
        return newArray;
    }
    public static void incrementDate(){
        LocalDate convertDate = LocalDate.parse(getEditTime());
        // Menambahkan satu hari
        LocalDate newDate = convertDate.plusDays(1);
        setEditTime(newDate.toString());
    }




    public void addTempStudent(String NIM, Student student) {
        this.addStudent(NIM,student);
    }



    public static Student checkNIM(String name, String NIM, String faculty, String program, String email) {
        for (Student x : Admin.getStudentData()) {
            if (x.getNIM().equals(NIM)) {
                return null;
            }
        }
        return new Student(name, NIM, faculty, program,email);
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
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setStyle(UIManager.styleSecondary);
        grid.add(pieChart,0,0);

        hbboxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbboxBtn.getChildren().addAll(btnBack);
        grid.add(hbboxBtn,0,1);


        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        grid.setStyle(UIManager.primaryColour);
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
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setStyle(UIManager.styleSecondary);
        hbboxBtn.getChildren().addAll(btnBack);
        hbboxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbboxBtn, 0, 1);
        btnBack.setOnAction(event -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        grid.setStyle(UIManager.primaryColour);
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
        facultyToPrograms.put("FT",new String[]{"SIPIL","INFORMATIKA","INDUSTRI","MESIN","ELEKTRO"});
        // Set up the UI
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Add Student");
        sceneTitle.setFont(Font.font("Sans-serif", FontWeight.BOLD, 25));
        sceneTitle.setFill(Color.web("#1b4332")); // Hexadecimal color
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label name = new Label("Name:");
        name.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        name.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(name, 0, 1);

        TextField inputName = new TextField();
        inputName.setPromptText("Enter student name");
        inputName.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        grid.add(inputName, 1, 1);

        Label NIM = new Label("NIM:");
        NIM.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        NIM.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(NIM, 0, 2);

        TextField inputNIM = new TextField();
        inputNIM.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        inputNIM.setPromptText("Enter student NIM");
        grid.add(inputNIM, 1, 2);

        Label faculty = new Label("Faculty:");
        faculty.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        faculty.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(faculty, 0, 3);
        ComboBox<String> facultyComboBox = new ComboBox<>();
        facultyComboBox.getItems().addAll(facultyToPrograms.keySet());
        grid.add(facultyComboBox, 1, 3);

        Label program = new Label("Program:");
        program.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        program.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(program, 0, 4);
        ComboBox<String> programComboBox = new ComboBox<>();
        grid.add(programComboBox, 1, 4);

        Label email = new Label("Email:");
        email.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        email.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(email, 0, 5);
        TextField inputEmail = new TextField();
        inputEmail.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        inputEmail.setPromptText("Enter student email");
        grid.add(inputEmail, 1, 5);

        facultyComboBox.setOnAction(e -> {
            String selectedFaculty = facultyComboBox.getValue();
            programComboBox.getItems().clear();
            if (selectedFaculty != null) {
                programComboBox.getItems().addAll(facultyToPrograms.get(selectedFaculty));
            }
        });

        Button btnSubmit = new Button("SUBMIT");
        Button btnBack = new Button("BACK");

        btnSubmit.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnSubmit.setStyle(UIManager.styleSecondary);
        btnBack.setStyle(UIManager.styleSecondary);

        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 6);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200);
        grid.add(actionTarget, 1, 7);

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        grid.setStyle(UIManager.primaryColour);
        stage.setScene(scene);
        stage.show();

        btnBack.setOnAction(event -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        btnSubmit.setOnAction(event -> {
            Student student = checkNIM(inputName.getText(),inputNIM.getText(),facultyComboBox.getValue(),programComboBox.getValue(),inputEmail.getText());
            if (inputName.getText().isEmpty() || inputNIM.getText().isEmpty() || facultyComboBox.getValue().isEmpty()||programComboBox.getValue().isEmpty() || inputEmail.getText().isEmpty())
                UIManager.showError(actionTarget,"PLEASE FILL ALL BLANKS");
            else if(inputNIM.getText().length() != 15)
                UIManager.showError(actionTarget,"NIM MUST 15 CHARACTERS");
            else if(student == null)
                UIManager.showError(actionTarget,"NIM SAME");
            else {
                String batch = inputNIM.getText().substring(0, 4);
                if (batch.equals("2023")){
                    Student newStudent = new Student(inputName.getText(),inputNIM.getText(),facultyComboBox.getValue(),programComboBox.getValue(),inputEmail.getText(),"Selasa");
                    addTempStudent(inputNIM.getText(),newStudent);
                }else{
                    Student newStudent = new Student(inputName.getText(),inputNIM.getText(),facultyComboBox.getValue(),programComboBox.getValue(),inputEmail.getText());
                    addTempStudent(inputNIM.getText(),newStudent);
                }
                UIManager.showSuccess(actionTarget,"STUDENT ADDED SUCCESSFULY");
            }
        });
    }
    public void displayStudent(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        final Label label = new Label("REGISTERED STUDENTS");
        label.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        label.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        tableStudent.setEditable(true);

        tableStudent.getColumns().clear();
        TableColumn<Student,String> nameCol = new TableColumn<>("Name");
        TableColumn<Student,String> nimCol = new TableColumn<>("NIM");
        TableColumn<Student,String> facultyCol = new TableColumn<>("Faculty");
        TableColumn<Student,String> prodiCol = new TableColumn<>("Program Studi");
        TableColumn<Student,String> emailCol = new TableColumn<>("Email");
        TableColumn<Student,String> consultingClassCol = new TableColumn<>("Consulting Class");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty") );
        prodiCol.setCellValueFactory(new PropertyValueFactory<>("programStudi"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        consultingClassCol.setCellValueFactory(new PropertyValueFactory<>("consultingClass"));

        tableStudent.getColumns().addAll(nameCol,nimCol,facultyCol,prodiCol,emailCol,consultingClassCol);

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        backBtn.setStyle(UIManager.styleSecondary);

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
        gridPane.setStyle(UIManager.primaryColour);
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
        sceneTitle.setFont(Font.font("Sans-serif", FontWeight.BOLD, 25));
        sceneTitle.setFill(Color.web("#1b4332")); // Hexadecimal color
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label categoryLabel = new Label("Category:");
        categoryLabel.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        categoryLabel.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(categoryLabel, 0, 1);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("History", "Text", "Story");
        categoryComboBox.setPromptText("Select category");
        grid.add(categoryComboBox, 1, 1);

        Label title = new Label("Title :");
        title.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        title.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(title, 0, 2); // Kolom 0, Baris 1
        TextField fieldTitle = new TextField();
        fieldTitle.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        fieldTitle.setPromptText("Enter book title");
        grid.add(fieldTitle, 1, 2); // Kolom 1, Baris 1

        Label author = new Label("Author : ");
        author.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        author.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(author, 0, 3);
        TextField fieldAuthor = new TextField();
        fieldAuthor.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        fieldAuthor.setPromptText("Enter book author");
        grid.add(fieldAuthor, 1, 3);

        Label stock = new Label("Stock");
        stock.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        stock.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
        grid.add(stock,0,4);
        TextField fieldStock = new TextField();
        fieldStock.setPrefSize(UIManager.fieldInputWidth,UIManager.fieldInputHeight);
        fieldStock.setPromptText("Enter book stock");
        grid.add(fieldStock,1,4);

        Button btnSubmit = new Button("SUBMIT");
        Button btnBack = new Button("BACK");
        btnSubmit.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);

        btnSubmit.setStyle(UIManager.styleSecondary);
        btnBack.setStyle(UIManager.styleSecondary);
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
        grid.setStyle(UIManager.primaryColour);
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
        sceneTitle.setFont(Font.font("Sans-serif", FontWeight.BOLD, 25));
        sceneTitle.setFill(Color.web("#1b4332")); // Hexadecimal color
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(getBookList());
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        Label id = new Label("ID");
        id.setTextFill(javafx.scene.paint.Color.web("#1b4332")); // Set text color
        id.setFont(Font.font("Sans-serif", FontWeight.BOLD, 16)); // Set font family and size
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

        btnReturn.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnReturn.setStyle(UIManager.styleSecondary);
        btnBack.setStyle(UIManager.styleSecondary);

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
        grid.setStyle(UIManager.primaryColour);
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
        sceneTitle.setFont(Font.font("Sans-serif", FontWeight.BOLD, 25));
        sceneTitle.setFill(Color.web("#1b4332")); // Hexadecimal color
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = super.createTableView(getBookList());
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        HBox hBBtn = new HBox(10);
        Button btnBack = new Button("BACK");
        btnBack.setPrefSize(UIManager.buttonSeondaryWidth,UIManager.buttonSecondaryHeight);
        btnBack.setStyle(UIManager.styleSecondary);
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
        grid.setStyle(UIManager.primaryColour);
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
        grid.setStyle(UIManager.primaryColour);
        stage.setTitle("CHANGE BOOK INFORMATION MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void addStudent(String NIM, Student student){
        //Student student = new Student(name,NIM,faculy,program,program,email);
        //Student student = new Student(name,NIM,faculy,program,email);
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
        //System.out.println(nextDay);
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