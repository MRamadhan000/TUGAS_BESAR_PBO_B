package org.example.com.main.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.com.main.UI.UIManager;
import org.example.com.main.books.*;
import org.example.com.main.util.IMenu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Student extends User implements IMenu {
    private String name,faculty,programStudi,NIM;
    private ArrayList<Book> borrowedBooks= new ArrayList<>();
    private static ArrayList<Book> studentBook = new ArrayList<>();
    private static String[][] tempBook = new String[10][10];
    private static int numberBorroewd = 0;
    public Student(String name, String NIM, String faculty, String programStudi){
        this.name = name;
        this.NIM = NIM;
        this.faculty = faculty;
        this.programStudi = programStudi;
    }
    public static void logIn(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Log In Student");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label userName = new Label("NIM:");
        grid.add(userName, 0, 1); // Kolom 0, Baris 1

        TextField fieldNIM = new TextField();
        fieldNIM.setPromptText("Enter your NIM");
        grid.add(fieldNIM, 1, 1); // Kolom 1, Baris 1

        Button btnSignIn = new Button("SIGN IN");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSignIn);
        grid.add(hBBtn, 1, 2);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 3);

        btnSignIn.setOnAction(actionEvent -> {
            if (fieldNIM.getText().isEmpty())
                UIManager.showError(actionTarget,"Field is empty" );
            else{
                Student student = null;
                student = searchStudent(fieldNIM.getText());
                if(student != null) {
                    Student.setStudentBook();
                    student.menu(stage);
                }else
                    UIManager.showError(actionTarget,"NIM not found");
            }
        });
        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });
        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("LOGIN STUDENT");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void menu(Stage stage){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Menu Admin");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = super.createTableView(Student.getStudentBook());
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        VBox hBBtn = new VBox(10);
        Button btnBukuT = new Button("Buku Terpinjam");
        Button btnPinjamB = new Button("Pinjam Buku");
        Button btnKembalikanB = new Button("Kembalikan Buku");
        Button btnOut = new Button("Pinjam Buku atau Logout");
        Button btnUpBook = new Button("Update Buku");
        hBBtn.setAlignment(Pos.CENTER);
        hBBtn.getChildren().addAll(btnBukuT,btnPinjamB,btnKembalikanB,btnOut,btnUpBook);
        grid.add(hBBtn,1,3);

        double buttonWidth = 170; // Tentukan lebar tombol
        double buttonHeight = 30; // Tentukan tinggi tombol
        btnBukuT.setPrefSize(buttonWidth, buttonHeight);
        btnPinjamB.setPrefSize(buttonWidth, buttonHeight);
        btnKembalikanB.setPrefSize(buttonWidth, buttonHeight);
        btnOut.setPrefSize(buttonWidth, buttonHeight);
        btnUpBook.setPrefSize(buttonWidth,buttonHeight);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        btnBukuT.setOnAction(actionEvent -> {
            this.displayBook(stage);
        });

        btnPinjamB.setOnAction(actionEvent -> {
          this.pinjamBuku(stage);
        });

        btnKembalikanB.setOnAction(actionEvent -> {
            if(this.borrowedBooks.isEmpty())
                alertEmptyBook(stage);
            else
               returnBooks(stage);
        });

        btnUpBook.setOnAction(actionEvent -> {
            if(this.borrowedBooks.isEmpty())
                alertEmptyBook(stage);
            else
                updateBooks(stage);
        });

        btnOut.setOnAction(actionEvent -> {
           if (numberBorroewd > 0){
               keepBooks(stage);
           }else {
               logOut(stage);
           }
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("STUDENT MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void alertEmptyBook(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("You dont have any book that have been borrowed");

        ButtonType btnBack = new ButtonType("Back");
        alert.getButtonTypes().setAll(btnBack);;

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnBack) {
            stage.close();
            menu(stage);
        }
    }

    @Override
    public void logOut(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("YOU ARE LOGOUT FROM STUDENT MENU");

        ButtonType yesButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(yesButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton){
            stage.close();
            try {
                clearArray();
                Main.menu(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void displayBook(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("MENU BUKU TERPINJAM");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
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
        stage.setTitle("BOOKS TABLE");
        stage.setScene(scene);
        stage.show();

    }

    public void pinjamBuku(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("PEMINJAMAN BUKU");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = super.createTableView(getStudentBook());
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

        Label duration = new Label("Duration");
        TextField fieldDuration = new TextField();
        fieldDuration.setPromptText("Enter duration");
        grid.add(duration,0,3);
        grid.add(fieldDuration,1,3);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        HBox hBBtn = new HBox(10);
        Button btnAdd = new Button("BORROW BOOK");
        Button btnBack = new Button("BACK");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack,btnAdd);
        grid.add(hBBtn,1,5);

        btnAdd.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty() || fieldDuration.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }

            Book book = this.searchBookAll(fieldId.getText());
            if (book == null) {
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
                return;
            }

            if (isBookAvailable(this, fieldId.getText())) {
                UIManager.showError(actionTarget, "BOOK HAS BEEN BORROWED");
                return;
            }

            try {
                if (Integer.parseInt(fieldDuration.getText()) >= 15) {
                    UIManager.showError(actionTarget, "DURATION MUST BE LOWER THAN 15");
                    return;
                }
                tempBook[numberBorroewd][0] = book.getBookId();
                tempBook[numberBorroewd][1] = fieldDuration.getText();
                numberBorroewd++;
                book.setStock(book.getStock() - 1);
                UIManager.showSuccess(actionTarget, "BOOK ADDED SUCCESSFULLY");
            } catch (NumberFormatException e) {
                UIManager.showError(actionTarget, "INPUT VALID NUMBER DURATION");
            }
        });
        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("BORRWED BOOK MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void keepBooks(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("APAKAH KAMU INGIN MEMINJAM BUKU INI");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        ArrayList<Book> selectionArr = new ArrayList<>();
        for (int i = 0; i < numberBorroewd; i++){
            for (Book book : getBookList()){
                if(book.getBookId().equals(tempBook[i][0])) {
                    Book newBook = new Book(book.getBookId(),book.getTitle(),book.getAuthor(),book.getStock());
                    newBook.setCategory(book.getCategory());
                    newBook.setDuration(Integer.parseInt(tempBook[i][1]));
                    selectionArr.add(newBook);
                    break;
                }
            }
        }
        TableView<PropertyBook> table = createTableView(selectionArr);
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        HBox hBBtn = new HBox(10);
        Button btnNo = new Button("NO");
        Button btnSave = new Button("SAVE");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnNo,btnSave);
        grid.add(hBBtn,1,2);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 3);

        btnSave.setOnAction(actionEvent -> {
            Main.addTempBook(this,numberBorroewd,tempBook);
            UIManager.showSuccess(actionTarget,"BOO HAS BEEN SAVE");
            logOut(stage);
        });

        btnNo.setOnAction(e -> {
            UIManager.showError(actionTarget,"BOOK DOESNT SAVED");
            logOut(stage);
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        stage.setTitle("VALIDATION MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void returnBooks(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("MENU KEMBALIKAN BUKU");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
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
        Button btnReturn = new Button("RETURN BOOK");
        Button btnBack = new Button("BACK");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack,btnReturn);
        grid.add(hBBtn,1,5);

        btnReturn.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            Book book = this.searchBookBorrowed(fieldId.getText());
            if (book == null) {
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
            }else {
                this.changeAmountBook(book,fieldId.getText());
                UIManager.showSuccess(actionTarget,"BOOK RETURNED SCCESSFULLY");
                stage.close();
                pinjamBuku(stage);
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("CHANGE BOOK MENU");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public TableView<PropertyBook> createTableView(ArrayList<Book> arr){
        TableView<PropertyBook> table = new TableView<>();
        table.setEditable(false);
        table.getColumns().clear();

        TableColumn<PropertyBook, String> idCol = new TableColumn<>("Id");
        TableColumn<PropertyBook, String> titleCol = new TableColumn<>("Title");
        TableColumn<PropertyBook, String> authorCol = new TableColumn<>("Author");
        TableColumn<PropertyBook, Integer> durationCol = new TableColumn<>("Duration");
        TableColumn<PropertyBook, String> categoryCol = new TableColumn<>("Category");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        table.getColumns().addAll(idCol, titleCol, authorCol, durationCol, categoryCol);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        final VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(20, 10, 10, 10));
        vbox.getChildren().addAll(table);
        gridPane.add(vbox, 0, 0);

        ArrayList<PropertyBook> convertBook = PropertyBook.bookToProperty(arr);
        final ObservableList<PropertyBook> data = FXCollections.observableArrayList(convertBook);
        table.setItems(data);
        return table;
    }

    @Override
    public void updateBooks(Stage stage){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("MENU TAMBAH DURASI BUKU BUKU");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
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

        HBox hBBtn = new HBox(10);
        Button btnReturn = new Button("EDIT BOOK");
        Button btnBack = new Button("BACK");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack,btnReturn);
        grid.add(hBBtn,1,3);


        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        btnReturn.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            Book book = this.searchBookBorrowed(fieldId.getText());
            if (book == null)
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
            else
                changeDuration(stage,book);

        });

        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("RETURN BOOK MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void changeDuration(Stage stage,Book book){
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        Text sceneTitle = new Text("EDIT BOOK");
        sceneTitle.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));
        grid.add(sceneTitle,0,0,2,1);

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1);

        Label duration = new Label("Duration");
        TextField fieldDuration = new TextField();
        fieldDuration.setPromptText("Enter book new duration");
        grid.add(duration,0,2);
        grid.add(fieldDuration,1,2);

        HBox hBBtn = new HBox(10);
        Button btnSave = new Button("SAVE");
        Button btnBack = new Button("BACK");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack,btnSave);
        grid.add(hBBtn,1,3);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200);
        grid.add(actionTarget,1,4);

        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("TAMBAH DURASI");
        stage.setScene(scene);
        stage.show();

        btnSave.setOnAction(actionEvent -> {
            if(fieldDuration.getText().isEmpty()){
                UIManager.showError(actionTarget,"FIELD CANNOT BE EMPTY");
                return;
            }
            try {
                int newDuration = Integer.parseInt(fieldDuration.getText());
                if (newDuration > 14)
                    UIManager.showError(actionTarget,"DURATION MUST BELOW 15");
                else {
                    book.setDuration(newDuration);
                    UIManager.showSuccess(actionTarget,"BOOK EDIT SUCCESFULLY");
                }

            }catch (Exception e){
                UIManager.showError(actionTarget,"PLEASE INPUT VALID NUMBER");
            }
        });
        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

    }

    public static void clearArray() {
        for (int i = 0; i < numberBorroewd; i++) {
            tempBook[i][0] = null;
            tempBook[i][1] = null;
        }
        numberBorroewd = 0;
    }
    public boolean isBookAvailable(Student student, String id) {
        boolean isFound = false;
        for (int i = 0; i < numberBorroewd; i++)
            if (tempBook[i][0].equals(id)) {
                isFound = true;
                break;
            }
        Book book = student.searchBookBorrowed(id);
        if (book == null && !isFound)
            return false;
        return true;
    }
    @Override
    public void addBook(Book book){
        borrowedBooks.add(book);
    }

    // MENYIMPAN BUKU YANG ADA DI KERANJANG KEK DALAM DATA BUKU MAHASISWA
    public void choiceBook(String bookId,int duration){
        Book book = Student.searchBookAll(bookId);
        Book borrowedBookCopy = new Book(book.getBookId(),book.getTitle(),book.getAuthor(),1);
        borrowedBookCopy.setDuration(duration);
        borrowedBookCopy.setCategory(book.getCategory());
        this.addBook(borrowedBookCopy);
        Book bookAdmin = Admin.searchBookAll(bookId);
        bookAdmin.setStock(bookAdmin.getStock()-1);
    }

    public static void setStudentBook() {
        studentBook.clear();
        for (Book book : Admin.getBookList()) {
            Book copiedBook = new Book(book.getBookId(), book.getTitle(), book.getAuthor(), book.getStock());
            copiedBook.setCategory(book.getCategory());
            studentBook.add(copiedBook);
        }
    }

    public void changeAmountBook(Book bookBorrowed,String inputId){
        Book bookAdmin = Admin.searchBookAll(inputId);
        bookAdmin.setStock(bookAdmin.getStock()+1);
        Book allBook = Student.searchBookAll(inputId);
        allBook.setStock(allBook.getStock() + 1);
        this.getBorrowedBooks().remove(bookBorrowed);
    }

    public static Book searchBookAll(String id){
        for (Book book : studentBook)
            if(book.getBookId().equals(id))
                return book;
        return null;
    }

    public Book searchBookBorrowed(String id){
        for (Book book : this.getBorrowedBooks())
            if(book.getBookId().equals(id))
                return book;
        return null;
    }
    public static Student searchStudent(String inputNIM){
        for (Student student : Admin.getStudentData())
            if(student.getNIM().equals(inputNIM))
                return student;
        return null;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getProgramStudi() {
        return programStudi;
    }

    public static ArrayList<Book> getStudentBook() {
        return studentBook;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public String getNIM(){
        return this.NIM;
    }
}