package com.shadowfox.library.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    public Book(int id, String title, String author, String isbn, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
    }

    public Book(String title, String author, String isbn) {
        this(0, title, author, isbn, true);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-30s | %-20s | %-15s | %-9s |",
                id, title, author, isbn, available ? "✅ Yes" : "❌ No");
    }
}