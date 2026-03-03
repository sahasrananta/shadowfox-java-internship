# LEARNINGS.md — ShadowFox Library Management System

## What Was the Hardest Bug?
The hardest bug was a **connection leak**. Early versions opened a new database connection
every time a DAO method was called but never closed it. After many operations the app
would slow down and eventually crash with "too many open connections" error.

## How Did I Fix It?
Two fixes:

1. **Singleton Pattern** — `DatabaseConnection.getInstance()` ensures only ONE connection
   is ever created and reused throughout the app's lifetime.

2. **try-with-resources** — Every `PreparedStatement` and `ResultSet` is now inside a
   `try (PreparedStatement ps = ...)` block which automatically closes it when done,
   even if an exception is thrown.

## What I Learned
1. **JDBC** — `DriverManager.getConnection()` connects Java to SQLite database
2. **PreparedStatement** — use `?` placeholders instead of string concatenation to prevent SQL Injection
3. **DAO Pattern** — separating SQL (BookDAO) from logic (Main) makes code cleaner and testable
4. **Singleton Pattern** — only one DatabaseConnection instance prevents resource waste
5. **try-with-resources** — `try (Connection c = ...)` auto-closes resources, prevents leaks
6. **java.time.LocalDate** — `ChronoUnit.DAYS.between(dueDate, returnDate)` calculates fine days
7. **Foreign Keys** — `loans` table links to `books` and `members` via JOIN queries
8. **SQL normalization** — Author stored in separate concept, not repeated in every book record