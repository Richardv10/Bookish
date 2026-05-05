package com.nology;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private Library library;
    private Scanner scanner;
    private User currentUser;

    public Menu(Library library, Scanner scanner) {
        this.library = library;
        this.scanner = scanner;
    }

    // Entry Point

    public void start() {

        currentUser = login();
        routeUser();
    }

    // Routing for admin

    private void routeUser() {

        if (currentUser.isAdmin()) {
            adminMenu();
        } else {
            userMenu();
        }
    }

    private void userMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n=== LIBRARY MENU ===");
            System.out.println("1. View books");
            System.out.println("2. Search books");
            System.out.println("3. Borrow book");
            System.out.println("4. Return book");
            System.out.println("5. My books");
            System.out.println("6. Logout / Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    library.printAllBooks();
                    break;

                case "2":
                    searchMenu();
                    break;

                case "3":
                    borrowMenu(currentUser);
                    break;

                case "4":
                    returnMenu(currentUser);
                    break;

                case "5":
                    library.showUserBooks(currentUser.getId());
                    break;

                case "6":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private User login() {

        boolean running = true;

        while (true) {


            System.out.println("Welcome to the library, please log in or create an account");
            System.out.println("1. Create User");
            System.out.println("2. Log in user");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1": {
                    System.out.println("Enter a Username");
                    String name = scanner.nextLine();
                    System.out.println("Choose a password over 8 characters");
                    String password = scanner.nextLine();
                    library.addUser(name, password);
                    System.out.println("Successfully registered");
                    break;
                }

                case "2": {
                    System.out.println("Enter Your Username");
                    String name = scanner.nextLine();
                    System.out.println("Enter your password (we have homeopathic security)");
                    String password = scanner.nextLine();
                    User user = library.login(name, password);
                    if (user != null)
                        return user;

                    System.out.println("Invalid login.");





                }


            }

        }

    }

    private void searchMenu() {

        boolean menuRunning = true;

        while (menuRunning) {

            System.out.println("Welcome to The Book Toucher Search ver 0.8b");
            System.out.println("1. Search for a book using The bookToucher");
            System.out.println("2. Back to safety");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1": {
                    System.out.println("Enter Title to Touch");
                    String title = scanner.nextLine();
                    List<Book> results = library.searchByTitle(title);

                    if (results.isEmpty()) {
                        System.out.println("No books touched.");
                    } else {

                        for (Book book : results) {
                            System.out.println(library.formatBook(book));
                        }
                    }
                    break;
                }

                case "2": {
                    menuRunning = false;
                    break;

                }

                default:
                    System.out.println("No dice big spuds");


            }
        }
    }

    private void borrowMenu(User currentUser) {

        System.out.print("Search for a book: ");
        String query = scanner.nextLine();

        List<Book> results = library.searchByTitle(query);

        if (results.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        // Show numbered list
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getTitle());
        }

        System.out.print("Select a book number to borrow: ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input) - 1;

            if (index < 0 || index >= results.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Book selected = results.get(index);

            boolean success = library.borrowBook(
                    currentUser.getId(),
                    selected.getTitle()
            );

            if (success) {
                System.out.println("Book borrowed.");
            } else {
                System.out.println("Book unavailable.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void returnMenu(User currentUser) {

        List<String> books = library.getBorrowedBooks(currentUser.getId());

        if (books == null || books.isEmpty()) {
            System.out.println("You have no books to return.");
            return;
        }

        System.out.println("\n=== YOUR BOOKS ===");

        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }

        System.out.print("Select book number to return: ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input) - 1;

            if (index < 0 || index >= books.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            String selectedTitle = books.get(index);

            boolean success = library.returnBook(
                    currentUser.getId(),
                    selectedTitle
            );

            if (success) {
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Return failed.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
        }
    }




private void adminMenu() {

    boolean running = true;

    while (running) {

        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. View all books");
        System.out.println("2. Most borrowed books");
        System.out.println("3 Ban politically inconvenient books");

        String choice = scanner.nextLine();

        switch (choice) {

            case "1":
                library.printAllBooks();
                break;

            case "2":
               // library.printMostBorrowedBooks();
                break;


                case "3":
                running = false;
                currentUser = login();
                routeUser();
                return;

            default:
                System.out.println("Invalid option");
        }
    }
}}














