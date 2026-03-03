package com.shadowfox.library.dao;

import com.shadowfox.library.db.DatabaseConnection;
import com.shadowfox.library.model.Loan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    private Connection conn = DatabaseConnection.getInstance().getConnection();

    public boolean issueLoan(int bookId, int memberId) {
        String sql = "INSERT INTO loans (book_id, member_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.setString(3, LocalDate.now().toString());
            ps.setString(4, LocalDate.now().plusDays(14).toString()); // 2 weeks due date
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error issuing loan: " + e.getMessage());
            return false;
        }
    }

    public boolean returnBook(int loanId) {
        String sql = "UPDATE loans SET return_date = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, LocalDate.now().toString());
            ps.setInt(2, loanId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error returning book: " + e.getMessage());
            return false;
        }
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = """
            SELECT l.id, l.book_id, l.member_id, b.title, m.name,
                   l.issue_date, l.due_date, l.return_date
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN members m ON l.member_id = m.id
            ORDER BY l.id DESC
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String returnDateStr = rs.getString("return_date");
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        LocalDate.parse(rs.getString("issue_date")),
                        LocalDate.parse(rs.getString("due_date")),
                        returnDateStr != null ? LocalDate.parse(returnDateStr) : null
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching loans: " + e.getMessage());
        }
        return loans;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = """
            SELECT l.id, l.book_id, l.member_id, b.title, m.name,
                   l.issue_date, l.due_date, l.return_date
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN members m ON l.member_id = m.id
            WHERE l.return_date IS NULL
            ORDER BY l.due_date ASC
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        LocalDate.parse(rs.getString("issue_date")),
                        LocalDate.parse(rs.getString("due_date")),
                        null
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching active loans: " + e.getMessage());
        }
        return loans;
    }

    public Loan getLoanById(int id) {
        String sql = """
            SELECT l.id, l.book_id, l.member_id, b.title, m.name,
                   l.issue_date, l.due_date, l.return_date
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN members m ON l.member_id = m.id
            WHERE l.id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String returnDateStr = rs.getString("return_date");
                return new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        LocalDate.parse(rs.getString("issue_date")),
                        LocalDate.parse(rs.getString("due_date")),
                        returnDateStr != null ? LocalDate.parse(returnDateStr) : null
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching loan: " + e.getMessage());
        }
        return null;
    }
}