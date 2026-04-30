package com.nology;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private Library library;
    private Scanner scanner;

    public Menu(Library library, Scanner scanner) {
        this.library = library;
        this.scanner = scanner;
    }

    public void start() {

        User currentUser = login();

        boolean running = true;

        while (running) {

            System.out.println("\n=== LIBRARY MENU ===");
            System.out.println("1. View books");
            System.out.println("2. Search books");
            System.out.println("3. Borrow book");
            System.out.println("4. My books");
            System.out.println("5. Logout / Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    library.printAllBooks();
                    break;

                case "2":
                    searchMenu();
                    break;

                case "3":
                    //borrowMenu();
                    break;

                case "4":
                    // library.showUserBooks(currentUser.getId());
                    break;

                case "5":
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
                    break;

                }


            }

        }

    }

    private void searchMenu () {

        boolean menuRunning = true;

        while (true) {
            String title = scanner.nextLine();
            System.out.println("Enter Title to Search");
            List<Book> results = library.searchByTitle(title);
            if (results.isEmpty()) {
                System.out.println("No results found.");
            } else {
                for (Book book : results) {
                    System.out.println(library.formatBook(book));
                }
            }

        }
    }
}