package com.shadowfox.library.ui;

import com.shadowfox.library.dao.*;
import com.shadowfox.library.model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibraryGUI extends JFrame {

    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private LoanDAO loanDAO = new LoanDAO();

    private JTabbedPane tabbedPane;
    private DefaultTableModel bookTableModel, memberTableModel, loanTableModel;
    private JLabel statusLabel;

    public LibraryGUI() {
        setTitle("ShadowFox Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 620);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(20, 20, 20));
        setLayout(new BorderLayout());

        buildUI();
        refreshAll();
        setVisible(true);
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(30, 30, 30));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.addTab("📚 Books", buildBooksPanel());
        tabbedPane.addTab("👥 Members", buildMembersPanel());
        tabbedPane.addTab("📋 Loans", buildLoansPanel());
        add(tabbedPane, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        JLabel title = new JLabel("📖 ShadowFox Library Management System");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(100, 200, 255));
        panel.add(title, BorderLayout.WEST);
        return panel;
    }

    private JPanel buildBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] cols = {"ID", "Title", "Author", "ISBN", "Available"};
        bookTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(bookTableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);

        // Form
        JTextField titleF = field(), authorF = field(), isbnF = field(), searchF = field();
        JButton addBtn = btn("➕ Add Book", new Color(40, 120, 60));
        JButton deleteBtn = btn("🗑️ Delete", new Color(160, 50, 50));
        JButton searchBtn = btn("🔍 Search", new Color(60, 80, 160));
        JButton refreshBtn = btn("🔄 Refresh", new Color(60, 60, 60));

        addBtn.addActionListener(e -> {
            boolean ok = bookDAO.addBook(new Book(titleF.getText(), authorF.getText(), isbnF.getText()));
            setStatus(ok ? "✅ Book added!" : "❌ Failed to add book!");
            refreshBooks(); titleF.setText(""); authorF.setText(""); isbnF.setText("");
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { setStatus("❌ Select a book first!"); return; }
            int id = (int) bookTableModel.getValueAt(row, 0);
            bookDAO.deleteBook(id); refreshBooks(); setStatus("✅ Book deleted!");
        });
        searchBtn.addActionListener(e -> {
            bookTableModel.setRowCount(0);
            bookDAO.searchBooks(searchF.getText()).forEach(b ->
                    bookTableModel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.isAvailable() ? "✅ Yes" : "❌ No"}));
        });
        refreshBtn.addActionListener(e -> refreshBooks());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(new Color(25, 25, 25));
        form.setPreferredSize(new Dimension(200, 0));
        form.add(lbl("Title:")); form.add(titleF);
        form.add(Box.createVerticalStrut(6));
        form.add(lbl("Author:")); form.add(authorF);
        form.add(Box.createVerticalStrut(6));
        form.add(lbl("ISBN:")); form.add(isbnF);
        form.add(Box.createVerticalStrut(6));
        form.add(lbl("Search:")); form.add(searchF);
        form.add(Box.createVerticalStrut(12));
        form.add(addBtn); form.add(Box.createVerticalStrut(6));
        form.add(searchBtn); form.add(Box.createVerticalStrut(6));
        form.add(deleteBtn); form.add(Box.createVerticalStrut(6));
        form.add(refreshBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Name", "Email", "Phone"};
        memberTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(memberTableModel);

        JTextField nameF = field(), emailF = field(), phoneF = field();
        JButton addBtn = btn("➕ Add Member", new Color(40, 120, 60));
        JButton deleteBtn = btn("🗑️ Delete", new Color(160, 50, 50));

        addBtn.addActionListener(e -> {
            boolean ok = memberDAO.addMember(new Member(nameF.getText(), emailF.getText(), phoneF.getText()));
            setStatus(ok ? "✅ Member added!" : "❌ Failed!");
            refreshMembers(); nameF.setText(""); emailF.setText(""); phoneF.setText("");
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { setStatus("❌ Select a member!"); return; }
            memberDAO.deleteMember((int) memberTableModel.getValueAt(row, 0));
            refreshMembers(); setStatus("✅ Member deleted!");
        });

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(new Color(25, 25, 25));
        form.setPreferredSize(new Dimension(200, 0));
        form.add(lbl("Name:")); form.add(nameF);
        form.add(Box.createVerticalStrut(6));
        form.add(lbl("Email:")); form.add(emailF);
        form.add(Box.createVerticalStrut(6));
        form.add(lbl("Phone:")); form.add(phoneF);
        form.add(Box.createVerticalStrut(12));
        form.add(addBtn); form.add(Box.createVerticalStrut(6));
        form.add(deleteBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Loan ID", "Book", "Member", "Issue Date", "Due Date", "Return Date", "Fine"};
        loanTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(loanTableModel);

        JTextField bookIdF = field(), memberIdF = field(), loanIdF = field();
        JButton issueBtn = btn("📤 Issue Book", new Color(40, 120, 60));
        JButton returnBtn = btn("📥 Return Book", new Color(60, 80, 160));
        JButton activeBtn = btn("📋 Active Loans", new Color(80, 60, 160));
        JButton allBtn = btn("📋 All Loans", new Color(60, 60, 60));

        issueBtn.addActionListener(e -> {
            try {
                int bId = Integer.parseInt(bookIdF.getText());
                int mId = Integer.parseInt(memberIdF.getText());
                Book book = bookDAO.getBookById(bId);
                if (book == null) { setStatus("❌ Book not found!"); return; }
                if (!book.isAvailable()) { setStatus("❌ Book not available!"); return; }
                loanDAO.issueLoan(bId, mId);
                bookDAO.updateAvailability(bId, false);
                refreshAll(); setStatus("✅ Book issued! Due in 14 days.");
            } catch (NumberFormatException ex) { setStatus("❌ Invalid ID!"); }
        });
        returnBtn.addActionListener(e -> {
            try {
                int lId = Integer.parseInt(loanIdF.getText());
                Loan loan = loanDAO.getLoanById(lId);
                if (loan == null) { setStatus("❌ Loan not found!"); return; }
                loanDAO.returnBook(lId);
                bookDAO.updateAvailability(loan.getBookId(), true);
                double fine = loan.calculateFine();
                refreshAll();
                setStatus("✅ Book returned!" + (fine > 0 ? " Fine: ₹" + fine : " No fine."));
            } catch (NumberFormatException ex) { setStatus("❌ Invalid Loan ID!"); }
        });
        activeBtn.addActionListener(e -> {
            loanTableModel.setRowCount(0);
            loanDAO.getActiveLoans().forEach(l -> addLoanRow(l));
            setStatus("Showing active loans");
        });
        allBtn.addActionListener(e -> refreshLoans());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(new Color(25, 25, 25));
        form.setPreferredSize(new Dimension(200, 0));
        form.add(lbl("Book ID (Issue):")); form.add(bookIdF);
        form.add(Box.createVerticalStrut(6));
        form.add(lbl("Member ID (Issue):")); form.add(memberIdF);
        form.add(Box.createVerticalStrut(6));
        form.add(lbl("Loan ID (Return):")); form.add(loanIdF);
        form.add(Box.createVerticalStrut(12));
        form.add(issueBtn); form.add(Box.createVerticalStrut(6));
        form.add(returnBtn); form.add(Box.createVerticalStrut(6));
        form.add(activeBtn); form.add(Box.createVerticalStrut(6));
        form.add(allBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.EAST);
        return panel;
    }

    private void addLoanRow(Loan l) {
        loanTableModel.addRow(new Object[]{
                l.getId(), l.getBookTitle(), l.getMemberName(),
                l.getIssueDate(), l.getDueDate(),
                l.getReturnDate() != null ? l.getReturnDate() : "Pending",
                l.calculateFine() > 0 ? "₹" + l.calculateFine() : "None"
        });
    }

    private void refreshBooks() {
        bookTableModel.setRowCount(0);
        bookDAO.getAllBooks().forEach(b -> bookTableModel.addRow(
                new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.isAvailable() ? "✅ Yes" : "❌ No"}));
    }

    private void refreshMembers() {
        memberTableModel.setRowCount(0);
        memberDAO.getAllMembers().forEach(m -> memberTableModel.addRow(
                new Object[]{m.getId(), m.getName(), m.getEmail(), m.getPhone()}));
    }

    private void refreshLoans() {
        loanTableModel.setRowCount(0);
        loanDAO.getAllLoans().forEach(this::addLoanRow);
    }

    private void refreshAll() { refreshBooks(); refreshMembers(); refreshLoans(); }

    private JLabel buildStatusBar() {
        statusLabel = new JLabel("  Welcome to ShadowFox Library!");
        statusLabel.setForeground(new Color(150, 150, 150));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setBackground(new Color(15, 15, 15));
        statusLabel.setOpaque(true);
        return statusLabel;
    }

    private void setStatus(String msg) { statusLabel.setText("  " + msg); }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setBackground(new Color(35, 35, 35));
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Arial", Font.PLAIN, 13));
        t.setRowHeight(28);
        t.setSelectionBackground(new Color(60, 100, 160));
        t.setGridColor(new Color(55, 55, 55));
        t.getTableHeader().setBackground(new Color(15, 15, 15));
        t.getTableHeader().setForeground(new Color(100, 200, 255));
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        return t;
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(new Color(45, 45, 45));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return f;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(180, 180, 180));
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        return l;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryGUI::new);
    }
}