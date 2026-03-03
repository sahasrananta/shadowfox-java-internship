package com.shadowfox.library.dao;

import com.shadowfox.library.db.DatabaseConnection;
import com.shadowfox.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private Connection conn = DatabaseConnection.getInstance().getConnection();

    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, 1)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error adding book: " + e.getMessage());
            return false;
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("available") == 1
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching books: " + e.getMessage());
        }
        return books;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        // PreparedStatement prevents SQL Injection
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("available") == 1
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching books: " + e.getMessage());
        }
        return books;
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("available") == 1
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching book: " + e.getMessage());
        }
        return null;
    }

    public boolean updateAvailability(int bookId, boolean available) {
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, available ? 1 : 0);
            ps.setInt(2, bookId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error updating book: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error deleting book: " + e.getMessage());
            return false;
        }
    }
}