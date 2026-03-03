package com.shadowfox.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private int id;
    private int bookId;
    private int memberId;
    private String bookTitle;
    private String memberName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private static final double FINE_PER_DAY = 5.0;

    public Loan(int id, int bookId, int memberId, String bookTitle, String memberName,
                LocalDate issueDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.bookTitle = bookTitle;
        this.memberName = memberName;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public String getBookTitle() { return bookTitle; }
    public String getMemberName() { return memberName; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returnDate != null; }

    public double calculateFine() {
        if (isReturned()) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysLate > 0 ? daysLate * FINE_PER_DAY : 0;
        } else {
            long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return daysLate > 0 ? daysLate * FINE_PER_DAY : 0;
        }
    }

    public boolean isOverdue() {
        return !isReturned() && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-20s | %-15s | %-12s | %-12s | %-12s | %-8s |",
                id, bookTitle, memberName, issueDate, dueDate,
                returnDate != null ? returnDate : "Not returned",
                calculateFine() > 0 ? "₹" + calculateFine() : "None");
    }
}