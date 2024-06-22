package org.example.com.main.books;

public class Book {
    private String bookId, title, author, category,jadwal;
    private int stock, duration;
    public Book(String bookId, String title, String author, int stock,String J) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.duration = 0;
        this.jadwal = jadwal;
    }

    public Book(String bookId, String title, String author, int stock) {
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getJadwal() {
        return this.jadwal;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    public int getStock() {
        return this.stock;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}