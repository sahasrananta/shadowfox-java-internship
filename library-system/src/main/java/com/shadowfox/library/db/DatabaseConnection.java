package com.shadowfox.library.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:library.db";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            initializeTables();
            System.out.println("✅ Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    // Singleton pattern — only one DB connection
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() { return connection; }

    private void initializeTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Books table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    isbn TEXT UNIQUE NOT NULL,
                    available INTEGER DEFAULT 1
                )
            """);

            // Members table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS members (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    phone TEXT NOT NULL
                )
            """);

            // Loans table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS loans (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    book_id INTEGER NOT NULL,
                    member_id INTEGER NOT NULL,
                    issue_date TEXT NOT NULL,
                    due_date TEXT NOT NULL,
                    return_date TEXT,
                    FOREIGN KEY (book_id) REFERENCES books(id),
                    FOREIGN KEY (member_id) REFERENCES members(id)
                )
            """);

            // Insert sample data only if tables are empty
            stmt.execute("""
                INSERT OR IGNORE INTO books (title, author, isbn) VALUES
                ('Clean Code', 'Robert C. Martin', '978-0132350884'),
                ('The Pragmatic Programmer', 'David Thomas', '978-0135957059'),
                ('Effective Java', 'Joshua Bloch', '978-0134685991'),
                ('Design Patterns', 'Gang of Four', '978-0201633610'),
                ('Java: The Complete Reference', 'Herbert Schildt', '978-1260440232')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO members (name, email, phone) VALUES
                ('Rahul Sharma', 'rahul@gmail.com', '9876543210'),
                ('Priya Singh', 'priya@yahoo.com', '9123456789'),
                ('Amit Kumar', 'amit@gmail.com', '9988776655')
            """);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}