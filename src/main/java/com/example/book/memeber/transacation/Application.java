package com.example.book.memeber.transacation;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class Application {

	private static final LibraryService libraryService = new LibraryService();

	public static void main(String[] args) {
		// Step 1: Initialize the database tables automatically on launch
		initializeDatabase();

		// Step 2: Run the loop for your pure Java Console Application
		runConsoleMenu();
	}

	private static void initializeDatabase() {
		try (Connection conn = DatabaseConfig.getConnection();
		     Statement stmt = conn.createStatement()) {

			// Create Books Table
			stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
					"id INT AUTO_INCREMENT PRIMARY KEY, " +
					"title VARCHAR(255) NOT NULL, " +
					"author VARCHAR(255) NOT NULL)");

			// Create Members Table
			stmt.execute("CREATE TABLE IF NOT EXISTS members (" +
					"id INT AUTO_INCREMENT PRIMARY KEY, " +
					"name VARCHAR(255) NOT NULL, " +
					"email VARCHAR(255) UNIQUE NOT NULL)");

			// Create Transactions Table
			stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
					"id INT AUTO_INCREMENT PRIMARY KEY, " +
					"book_id INT, " +
					"member_id INT, " +
					"issue_date DATE NOT NULL, " +
					"return_date DATE, " +
					"FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, " +
					"FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE)");

			System.out.println("[System Setup] Standalone database schema verified/created successfully.");
		} catch (Exception e) {
			System.err.println("[System Error] Failed to initialize tables: " + e.getMessage());
		}
	}

	private static void runConsoleMenu() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n========= LIBRARY CONSOLE SYSTEM =========");
			System.out.println("1. Add Book       2. View Books      3. Update Book    4. Delete Book");
			System.out.println("5. Add Member     6. View Members");
			System.out.println("7. Issue Book     8. Return Book     9. View Transactions");
			System.out.println("0. Exit");
			System.out.print("Enter choice: ");

			int choice = -1;
			if (scanner.hasNextInt()) {
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume remaining newline buffer
			} else {
				scanner.nextLine(); // Clear the invalid input
				System.out.println("Invalid input. Please enter a valid menu number.");
				continue;
			}

			switch (choice) {
				case 1:
					System.out.print("Enter Title: ");
					String title = scanner.nextLine();
					System.out.print("Enter Author: ");
					String author = scanner.nextLine();
					libraryService.addBook(title, author);
					break;
				case 2:
					libraryService.viewBooks();
					break;
				case 3:
					System.out.print("Enter Book ID to Update: ");
					int updateId = scanner.nextInt();
					scanner.nextLine();
					System.out.print("Enter New Title: ");
					String newTitle = scanner.nextLine();
					System.out.print("Enter New Author: ");
					String newAuthor = scanner.nextLine();
					libraryService.updateBook(updateId, newTitle, newAuthor);
					break;
				case 4:
					System.out.print("Enter Book ID to Delete: ");
					int deleteId = scanner.nextInt();
					libraryService.deleteBook(deleteId);
					break;
				case 5:
					System.out.print("Enter Member Name: ");
					String name = scanner.nextLine();
					System.out.print("Enter Email: ");
					String email = scanner.nextLine();
					libraryService.addMember(name, email);
					break;
				case 6:
					libraryService.viewMembers();
					break;
				case 7:
					System.out.print("Enter Book ID: ");
					int bId = scanner.nextInt();
					System.out.print("Enter Member ID: ");
					int mId = scanner.nextInt();
					libraryService.issueBook(bId, mId);
					break;
				case 8:
					System.out.print("Enter Transaction ID to return: ");
					int txId = scanner.nextInt();
					libraryService.returnBook(txId);
					break;
				case 9:
					libraryService.viewTransactions();
					break;
				case 0:
					System.out.println("Exiting application. Goodbye!");
					System.exit(0);
				default:
					System.out.println("Invalid choice. Try again.");
			}
		}
	}
}