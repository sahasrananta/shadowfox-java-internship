package com.shadowfox.library;

import com.shadowfox.library.dao.*;
import com.shadowfox.library.model.*;

import java.util.List;
import java.util.Scanner;

public class Main {

    static BookDAO bookDAO = new BookDAO();
    static MemberDAO memberDAO = new MemberDAO();
    static LoanDAO loanDAO = new LoanDAO();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║    ShadowFox Library Management System   ║");
        System.out.println("╚══════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter choice: ");
            switch (choice) {
                case 1 -> booksMenu();
                case 2 -> membersMenu();
                case 3 -> loansMenu();
                case 0 -> { System.out.println("👋 Goodbye!"); running = false; }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
        scanner.close();
    }

    static void printMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("[1] Books Management");
        System.out.println("[2] Members Management");
        System.out.println("[3] Loans Management");
        System.out.println("[0] Exit");
        System.out.println("================================");
    }

    static void booksMenu() {
        System.out.println("\n--- Books Menu ---");
        System.out.println("[1] Add Book  [2] View All  [3] Search  [4] Delete");
        int choice = getIntInput("Choice: ");
        switch (choice) {
            case 1 -> {
                System.out.print("Title: "); String title = scanner.nextLine();
                System.out.print("Author: "); String author = scanner.nextLine();
                System.out.print("ISBN: "); String isbn = scanner.nextLine();
                boolean ok = bookDAO.addBook(new Book(title, author, isbn));
                System.out.println(ok ? "✅ Book added!" : "❌ Failed to add book!");
            }
            case 2 -> {
                List<Book> books = bookDAO.getAllBooks();
                if (books.isEmpty()) { System.out.println("No books found!"); return; }
                System.out.println("+------+--------------------------------+----------------------+-----------------+-----------+");
                System.out.println("| ID   | Title                          | Author               | ISBN            | Available |");
                System.out.println("+------+--------------------------------+----------------------+-----------------+-----------+");
                books.forEach(System.out::println);
                System.out.println("+------+--------------------------------+----------------------+-----------------+-----------+");
            }
            case 3 -> {
                System.out.print("Search keyword: "); String kw = scanner.nextLine();
                bookDAO.searchBooks(kw).forEach(System.out::println);
            }
            case 4 -> {
                int id = getIntInput("Book ID to delete: ");
                System.out.println(bookDAO.deleteBook(id) ? "✅ Deleted!" : "❌ Failed!");
            }
        }
    }

    static void membersMenu() {
        System.out.println("\n--- Members Menu ---");
        System.out.println("[1] Add Member  [2] View All  [3] Delete");
        int choice = getIntInput("Choice: ");
        switch (choice) {
            case 1 -> {
                System.out.print("Name: "); String name = scanner.nextLine();
                System.out.print("Email: "); String email = scanner.nextLine();
                System.out.print("Phone: "); String phone = scanner.nextLine();
                boolean ok = memberDAO.addMember(new Member(name, email, phone));
                System.out.println(ok ? "✅ Member added!" : "❌ Failed!");
            }
            case 2 -> memberDAO.getAllMembers().forEach(System.out::println);
            case 3 -> {
                int id = getIntInput("Member ID to delete: ");
                System.out.println(memberDAO.deleteMember(id) ? "✅ Deleted!" : "❌ Failed!");
            }
        }
    }

    static void loansMenu() {
        System.out.println("\n--- Loans Menu ---");
        System.out.println("[1] Issue Book  [2] Return Book  [3] Active Loans  [4] All Loans");
        int choice = getIntInput("Choice: ");
        switch (choice) {
            case 1 -> {
                int bookId = getIntInput("Book ID: ");
                int memberId = getIntInput("Member ID: ");
                Book book = bookDAO.getBookById(bookId);
                if (book == null) { System.out.println("❌ Book not found!"); return; }
                if (!book.isAvailable()) { System.out.println("❌ Book not available!"); return; }
                boolean ok = loanDAO.issueLoan(bookId, memberId);
                if (ok) {
                    bookDAO.updateAvailability(bookId, false);
                    System.out.println("✅ Book issued! Due date: 14 days from today.");
                }
            }
            case 2 -> {
                int loanId = getIntInput("Loan ID to return: ");
                Loan loan = loanDAO.getLoanById(loanId);
                if (loan == null) { System.out.println("❌ Loan not found!"); return; }
                loanDAO.returnBook(loanId);
                bookDAO.updateAvailability(loan.getBookId(), true);
                double fine = loan.calculateFine();
                System.out.println("✅ Book returned!" + (fine > 0 ? " Fine: ₹" + fine : " No fine."));
            }
            case 3 -> {
                List<Loan> active = loanDAO.getActiveLoans();
                if (active.isEmpty()) { System.out.println("No active loans!"); return; }
                active.forEach(l -> System.out.println(l + (l.isOverdue() ? " ⚠️ OVERDUE" : "")));
            }
            case 4 -> loanDAO.getAllLoans().forEach(System.out::println);
        }
    }

    static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("❌ Enter a valid number!"); }
        }
    }
}