# ShadowFox Library Management System
> Intermediate Level Task 2 | ShadowFox Java Internship

## Features
- ✅ Full Book management (Add, Search, Delete)
- ✅ Member management (Add, View, Delete)
- ✅ Loan management (Issue, Return, Active Loans)
- ✅ Overdue fine calculation using `java.time.LocalDate`
- ✅ SQLite database for data persistence
- ✅ DAO Pattern (separates SQL from business logic)
- ✅ PreparedStatement (prevents SQL Injection)
- ✅ Singleton DatabaseConnection
- ✅ Console version + Swing GUI with tabs

## Project Structure
```
src/
├── main/java/com/shadowfox/library/
│   ├── model/
│   │   ├── Book.java        # Book POJO
│   │   ├── Member.java      # Member POJO
│   │   └── Loan.java        # Loan POJO with fine calculation
│   ├── db/
│   │   └── DatabaseConnection.java  # Singleton JDBC connection
│   ├── dao/
│   │   ├── BookDAO.java     # SQL for books
│   │   ├── MemberDAO.java   # SQL for members
│   │   └── LoanDAO.java     # SQL for loans
│   ├── ui/
│   │   └── LibraryGUI.java  # Swing GUI with tabs
│   └── Main.java            # Console application
pom.xml
```

## Database Setup
No manual setup needed! The database is created automatically when you run the app.

- Database file: `library.db` (created in project root on first run)
- Tables created automatically: `books`, `members`, `loans`
- Sample data inserted automatically on first run

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+ (downloads SQLite driver automatically)

### Console Version
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.shadowfox.library.Main"
```

### GUI Version
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.shadowfox.library.ui.LibraryGUI"
```

### Run Tests
```bash
mvn test
```

## Key Concepts Learned
- **JDBC** — Java Database Connectivity with SQLite driver
- **DAO Pattern** — BookDAO, MemberDAO, LoanDAO separate SQL from logic
- **PreparedStatement** — prevents SQL Injection (never string concatenation!)
- **Singleton Pattern** — only one DatabaseConnection instance
- **try-with-resources** — auto-closes Connection and ResultSet (no leaks)
- **java.time.LocalDate** — calculates overdue fines between dates
- **Foreign Keys** — loans table references books and members tables