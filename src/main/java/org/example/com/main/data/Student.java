package org.example.com.main.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;


public class Student extends User implements IMenu {
    private String name,faculty,programStudi,NIM,consultingClass,email,link;
    private ArrayList<Book> borrowedBooks= new ArrayList<>();
    private static ArrayList<String> favoriteBooks = new ArrayList<>();
    private static String arrhari[] = new String[6];
    private static String arrlink[] = new String[6];

    public Student(String name, String NIM, String faculty, String programStudi,String email){
        this.name = name;
        this.NIM = NIM;
        this.faculty = faculty;
        this.programStudi = programStudi;
        this.email = email;
        this.consultingClass = "none";

    }

    public Student(String name, String NIM, String faculty, String programStudi,String email, String consultingClass){
        this.name = name;
        this.NIM = NIM;
        this.faculty = faculty;
        this.programStudi = programStudi;
        this.email = email;
        this.consultingClass = consultingClass;
        this.setLink("link1");
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
                    student.addVisitor();
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

    public static ArrayList<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    @Override
    public void menu(Stage stage){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("STUDENT MENU");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        VBox hBBtn = new VBox(10);
        Button btnBukuT = new Button("Buku Terpinjam");
        Button btnPinjamB = new Button("Pinjam Buku");
        Button btnKembalikanB = new Button("Kembalikan Buku");
        Button btnOut = new Button("Logout");
        Button btnDisplayFavorite = new Button("Tampilkan Rekomendasi Buku");
        Button btnUpBook = new Button("Update Buku");
        Button btnDaftarKonsul = new Button("Daftar Konsultasi");
        Button btntampilJadwalKonsul = new Button("Tampilkan Jadwal");
        hBBtn.setAlignment(Pos.CENTER);
        hBBtn.getChildren().addAll(btnBukuT,btnPinjamB,btnKembalikanB,btnDisplayFavorite,btnUpBook,btnDaftarKonsul,btntampilJadwalKonsul,btnOut);
        grid.add(hBBtn,1,1);

        btnBukuT.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnPinjamB.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnKembalikanB.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnOut.setPrefSize(UIManager.getButtonWidth(), UIManager.getButtonHeight());
        btnDisplayFavorite.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());
        btnUpBook.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());
        btnDaftarKonsul.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());
        btntampilJadwalKonsul.setPrefSize(UIManager.getButtonWidth(),UIManager.getButtonHeight());

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 2);

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
            logOut(stage);
        });

        btnDisplayFavorite.setOnAction(actionEvent -> {
            displayFavoriteBook(stage);
        });

        btnDaftarKonsul.setOnAction(actionEvent -> {
            try {
                String tahunStr = this.getNIM().substring(0, 4);
                // Mengambil 4 karakter pertama
                int tahun = Integer.parseInt(tahunStr); // Mengubah string menjadi integer
                if (tahun == 2023) {
                    // Tampilkan pesan bahwa pendaftaran tidak diperbolehkan
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pendaftaran Ditolak");
                    alert.setHeaderText(null);
                    alert.setContentText("Anda tidak dapat mendaftar karena NIM Anda adalah tahun 2023.");
                    alert.showAndWait();
                } else  {
                    // Tampilkan jadwal=
                    displayJadwalKonsultasi(stage);
                }
            }catch (IndexOutOfBoundsException e){
                System.err.println(e.getMessage());
            }
        });

        btntampilJadwalKonsul.setOnAction(actionEvent -> {
            if (this.getConsultingClass().equals("none")|| this.getConsultingClass().equals("") ){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pendaftaran Ditolak");
                alert.setHeaderText(null);
                alert.setContentText("anda belum mendaftar kelas ");
                alert.showAndWait();
            } else  {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pendaftaran Ditolak");
                alert.setHeaderText(null);
                alert.setContentText("jadwal kelas anda " + this.getConsultingClass()+" " +this.getLink());
                alert.showAndWait();
            }
        });
        Scene scene = new Scene(grid,UIManager.getWidth(),UIManager.getHeight());
        stage.setTitle("STUDENT MENU");
        stage.setScene(scene);
        stage.show();
    }
    public void borrowedEmptyBook(Book book, String duration, String timeWait,Stage stage) {
        // Membuat dialog kustom
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Borrow Book Confirmation");

        // Menambahkan tombol Yes dan No
        ButtonType yesButtonType = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButtonType = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(yesButtonType, noButtonType);

        // Membuat label dan text field untuk jumlah hari peminjaman
        Label label = new Label("Number of days to borrow: " + duration);

        // Mengatur layout dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Book with id " + book.getBookId() + " is empty"), 0, 0);
        grid.add(new Label("Do you want to borrow this book once stock is available maybe after " + timeWait + "?"), 0, 1);
        grid.add(label, 0, 2);

        dialog.getDialogPane().setContent(grid);

        // Menampilkan dialog dan menunggu respons pengguna
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == yesButtonType) {
            String format = this.getNIM() + "," + book.getBookId() + "," + duration;
            Admin.addToLoadBook(format);
        }
    }

    public void displayJadwalKonsultasi(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene()); // SAVE PREVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));

        String[] targetDay = new String[6];
        String[] targetKouta = new String[6];
        for (int i = 0; i < 6; i++){
            String[] split = arrhari[i].split(",");
            targetDay[i] = split[0];
            targetKouta[i] = split[1];
        }

        // Convert arrays to ObservableList
        ObservableList<String> dayList = FXCollections.observableArrayList(targetDay);
        ObservableList<String> koutaList = FXCollections.observableArrayList(targetKouta);
        ObservableList<String> headerDay = FXCollections.observableArrayList("DAY");
        ObservableList<String> headerkouta = FXCollections.observableArrayList("KOUTA");
        // Create ListViews
        ListView<String> dayListView = new ListView<>(dayList);
        ListView<String> linkListView = new ListView<>(koutaList);
        ListView<String> headerDayListView = new ListView<>(headerDay);
        ListView<String> headerKoutaListView = new ListView<>(headerkouta);

        // Set ListView sizes to fit content
        dayListView.setPrefHeight(dayList.size() * 24 + 2);
        linkListView.setPrefHeight(koutaList.size() * 24 + 2);
        headerDayListView.setPrefHeight(30);
        headerKoutaListView.setPrefHeight(30);

        // Create a layout
        HBox hboxHeader = new HBox(10); // spacing between elements
        hboxHeader.getChildren().addAll(headerDayListView,headerKoutaListView);
        grid.add(hboxHeader,1,0);
        // Create a layout
        HBox hbox = new HBox(10); // spacing between elements
        hbox.getChildren().addAll(dayListView, linkListView);
        grid.add(hbox,1,1);

        Text sceneTitle = new Text("Inputkan hari konsultasi");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 2, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label hari = new Label("Hari :");
        grid.add(hari, 0, 3); // Kolom 0, Baris 1

        TextField fieldHari = new TextField();
        fieldHari.setPromptText("hari");
        grid.add(fieldHari, 1, 4); // Kolom 1, Baris 1

        Button btnSignIn = new Button("Daftar");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSignIn);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSignIn.setOnAction(actionEvent -> {
            String isvalid = kurangKouta(fieldHari.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hasil Konsultasi");
            alert.setHeaderText(null);
            if (isvalid != null) {
                alert.setContentText("Jadwal Konsultasi: " + isvalid);
                this.setConsultingClass(fieldHari.getText());
                this.setLink(isvalid);
            } else {
                alert.setContentText("Jadwal Kelas Tidak Tersedia atau Anda Telah Mendaftar Kelas");
            }
            alert.showAndWait();
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        stage.setTitle("LOGIN STUDENT");
        stage.setScene(scene);
        stage.show();
    }
    public static void setJadwalkonsultasi() {
        String[] hari = {"Senin,3", "Selasa,3", "Rabu,2", "Kamis,3", "Jum'at,3", "Sabtu,3"};
        String[] link = {"https://bit.ly/konsultasiperpus1", "https://bit.ly/konsultasiperpus2", "https://bit.ly/Konsultasiperpus3", "https://bit.ly/konsultasiperpus1", "https://bit.ly/konsultasiperpus2", "https://bit.ly/Konsultasiperpus3"};
        for (int i = 0; i < 6; i++) {
            arrhari [i] = hari[i];
            arrlink [i] = link[i];
        }
    }

    public String kurangKouta (String targetDay){
        for (int i = 0; i < 6; i++) {
            String[] splitHari = arrhari[i].split(",");
            String hari = splitHari[0];
            int kouta = Integer.parseInt(splitHari[1]);
            if (hari.equalsIgnoreCase(targetDay) && this.getConsultingClass().equals("none")) {
                if (kouta > 0) {
                    kouta--;
                    arrhari[i] = hari + "," + kouta;
                    System.out.println("Kuota untuk hari " + hari + " telah dikurangi. Kuota sekarang: " + kouta);
                    return arrlink[i];
                }else
                    return null;
            }
        }
        System.out.println("Hari " + targetDay + " tidak ditemukan.");
        return null;
    }

    public ArrayList<Book> getFavoriteBookArr(){
        // Menggunakan HashMap untuk menyimpan jumlah setiap id buku
        HashMap<String, Integer> bukuMap = new HashMap<>();

        for (String idBuku : favoriteBooks) {
            if (bukuMap.containsKey(idBuku)) {
                bukuMap.put(idBuku, bukuMap.get(idBuku) + 1);
            } else {
                bukuMap.put(idBuku, 1);
            }
        }

        // Mengonversi HashMap ke ArrayList
        ArrayList<Map.Entry<String, Integer>> bukuList = new ArrayList<>(bukuMap.entrySet());

        // Melakukan sorting ArrayList berdasarkan jumlah buku dari yang banyak ke sedikit
        Collections.sort(bukuList, new Comparator<>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        // Mengonversi hasil sorting ke array 2D
        String[][] bukuArray = new String[bukuList.size()][2];
        int index = 0;
        for (Map.Entry<String, Integer> entry : bukuList) {
            bukuArray[index][0] = entry.getKey();
            bukuArray[index][1] = String.valueOf(entry.getValue());
            index++;
        }

        int indeks = 0;
        ArrayList<Book> sortedBooks = new ArrayList<>();
        for (Book book : getBookList()){
            for (String seacrhId : favoriteBooks){
                if (book.getBookId().equals(seacrhId)){
                    Book newBook = new Book(book.getBookId(),book.getTitle(),book.getAuthor(),book.getStock());
                    newBook.setCategory(book.getCategory());
                    newBook.setStock(Integer.parseInt(bukuArray[indeks][1]));
                    sortedBooks.add(newBook);
                    indeks++;
                    break;
                }
            }
        }
        return sortedBooks;
    }

    public void displayFavoriteBook(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("FAVORITE BOOKS");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = super.createTableViewFavorite(getFavoriteBookArr());
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
                //clearArray();
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

        TableView<PropertyBook> table = super.createTableView(User.getBookList());
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
            if (isBookBorrowed(fieldId.getText())) {
                UIManager.showError(actionTarget, "BOOK HAS BEEN BORROWED");
                return;
            }
            try {
                if (Integer.parseInt(fieldDuration.getText()) >= 15) {
                    UIManager.showError(actionTarget, "DURATION MUST BE LOWER THAN 15");
                    return;
                }
            }catch (NumberFormatException e){
                System.err.println(e.getMessage());
            }
            if(book.getStock()< 1){
                if (!Admin.isHaveBookLoad(fieldId.getText()))
                    borrowedEmptyBook(book,fieldDuration.getText(),Admin.getWaitBorrowedBook(fieldId.getText()),stage);
                else
                    UIManager.showError(actionTarget, "BOOK HAS BEEN BEEN ORDERED");
                return;
            }
            try {
                savedBorrowedBooks(book,Integer.parseInt(fieldDuration.getText()));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("PEMINJAMAN BUKU");
                alert.setHeaderText(null);
                alert.setContentText("ANDA BERHASIL MEMINJAM BUKU DENGAN ID BUKU ADALAH " + book.getBookId()); // Ganti "12345" dengan book.getBookId()

                // Mengganti ikon default dengan ikon centang
                ImageView icon = new ImageView(new Image(Main.class.getResource("/Image/centang.png").toString()));
                icon.setFitHeight(48); // Sesuaikan ukuran ikon
                icon.setFitWidth(48);  // Sesuaikan ukuran ikon
                alert.setGraphic(icon);
                alert.showAndWait();
                stage.close();
                pinjamBuku(stage);
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

    public void savedBorrowedBooks(Book book, int duration){
        Book newBook = new Book(book.getBookId(),book.getTitle(),book.getAuthor(),1);
        newBook.setDuration(duration);
        newBook.setCategory(book.getCategory());
        this.addBook(newBook);
        book.setStock(book.getStock()-1);
        favoriteBooks.add(newBook.getBookId());
        Admin.addToListBorrowed(formatTimeBorrowed(book.getBookId(),duration));
    }

    public String formatTimeBorrowed(String bookId,int days){
        LocalDate convertDate = LocalDate.parse(Admin.getDate());
        LocalDate newDate = convertDate.plusDays(days);
        System.out.println("PEMINJAMAN BUKU : "+ this.getNIM() +"," + bookId + "," + Admin.getDate() + "," + newDate.toString());
        return this.getNIM() +"," + bookId + "," + Admin.getDate() + "," + newDate.toString();
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
                Admin.removeListBorrowed(this.getNIM(),book.getBookId());
                UIManager.showSuccess(actionTarget,"BOOK RETURNED SCCESSFULLY");
                stage.close();
                menu(stage);
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
    public boolean isBookBorrowed( String id) {
        Book book = this.searchBookBorrowed(id);
        if (book == null)
            return false;
        return true;
    }
    @Override
    public void addBook(Book book){
        borrowedBooks.add(book);
    }

//    // MENYIMPAN BUKU YANG ADA DI KERANJANG KEK DALAM DATA BUKU MAHASISWA
//    public void choiceBook(String bookId,int duration){
//        Book book = Student.searchBookAll(bookId);
//        Book borrowedBookCopy = new Book(book.getBookId(),book.getTitle(),book.getAuthor(),1);
//        borrowedBookCopy.setDuration(duration);
//        borrowedBookCopy.setCategory(book.getCategory());
//        this.addBook(borrowedBookCopy);
//        Book bookAdmin = Admin.searchBookAll(bookId);
//        bookAdmin.setStock(bookAdmin.getStock()-1);
//    }

//    public static void setStudentBook() {
//        studentBook.clear();
//        for (Book book : Admin.getBookList()) {
//            Book copiedBook = new Book(book.getBookId(), book.getTitle(), book.getAuthor(), book.getStock());
//            copiedBook.setCategory(book.getCategory());
//            studentBook.add(copiedBook);
//        }
//    }

    public void changeAmountBook(Book bookBorrowed,String inputId){
        Book bookAdmin = Admin.searchBookAll(inputId);
        bookAdmin.setStock(bookAdmin.getStock()+1);
//        Book allBook = Student.searchBookAll(inputId);
//        allBook.setStock(allBook.getStock() + 1);
        this.getBorrowedBooks().remove(bookBorrowed);
    }

    public static Book searchBookAll(String id){
        for (Book book : Admin.getBookList())
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
    public static Student searchStudent(String inputNIM){ //
        for (Student student : Admin.getStudentData())
            if(student.getNIM().equals(inputNIM))
                return student;
        return null;
    }

    public void addVisitor(){
        Admin.setTime();
        String format = Admin.getDate() +" " + Admin.getTime() + " "+ this.getNIM();
        //2024-06-29 10:39:28 100
        Admin.addVisitor(format);
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getConsultingClass() {
        return consultingClass;
    }

    public void setConsultingClass(String consultingClass) {
        this.consultingClass = consultingClass;
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
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    public String getNIM(){
        return this.NIM;
    }
}