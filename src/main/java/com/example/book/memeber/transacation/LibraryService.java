package com.example.book.memeber.transacation;

import java.sql.*;
import java.time.LocalDate;

public class LibraryService {

    // ==================== BOOK CRUD ====================
    public void addBook(String title, String author) {
        String sql = "INSERT INTO books (title, author) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.err.println("Database Error while adding book: " + e.getMessage());
        }
    }

    public void viewBooks() {
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Books List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Title: %s | Author: %s\n",
                        rs.getInt("id"), rs.getString("title"), rs.getString("author"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error while fetching books: " + e.getMessage());
        }
    }

    public void updateBook(int id, String newTitle, String newAuthor) {
        String sql = "UPDATE books SET title = ?, author = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newAuthor);
            pstmt.setInt(3, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Book updated successfully!");
            else System.out.println("Book ID not found.");
        } catch (SQLException e) {
            System.err.println("Database Error while updating book: " + e.getMessage());
        }
    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Book deleted successfully!");
            else System.out.println("Book ID not found.");
        } catch (SQLException e) {
            System.err.println("Database Error while deleting book: " + e.getMessage());
        }
    }

    // ==================== MEMBER CRUD ====================
    public void addMember(String name, String email) {
        String sql = "INSERT INTO members (name, email) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("Member registered successfully!");
        } catch (SQLException e) {
            System.err.println("Database Error while adding member: " + e.getMessage());
        }
    }

    public void viewMembers() {
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Members List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Email: %s\n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error while fetching members: " + e.getMessage());
        }
    }

    // ==================== TRANSACTION MANAGEMENT ====================
    public void issueBook(int bookId, int memberId) {
        String sql = "INSERT INTO transactions (book_id, member_id, issue_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));
            pstmt.executeUpdate();
            System.out.println("Book issued successfully!");
        } catch (SQLException e) {
            System.err.println("Database Error during book issuance: " + e.getMessage());
        }
    }

    public void returnBook(int transactionId) {
        String sql = "UPDATE transactions SET return_date = ? WHERE id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, transactionId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Book returned successfully!");
            else System.out.println("Transaction ID invalid or book already returned.");
        } catch (SQLException e) {
            System.err.println("Database Error during book return: " + e.getMessage());
        }
    }

    public void viewTransactions() {
        String sql = "SELECT t.id, b.title, m.name, t.issue_date, t.return_date " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.id " +
                "JOIN members m ON t.member_id = m.id";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Transaction Logs ---");
            while (rs.next()) {
                Date retDate = rs.getDate("return_date");
                String returnStr = (retDate != null) ? retDate.toString() : "Not Returned Yet";
                System.out.printf("TxID: %d | Book: %s | Member: %s | Issued: %s | Returned: %s\n",
                        rs.getInt("id"), rs.getString("title"), rs.getString("name"),
                        rs.getDate("issue_date"), returnStr);
            }
        } catch (SQLException e) {
            System.err.println("Database Error while fetching transactions: " + e.getMessage());
        }
    }
}