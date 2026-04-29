package com.nology;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        library.loadBooks("books.csv");

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== LIBRARY MENU ===");
        System.out.println("1. View all books");
        System.out.println("2. Search book by title");
        System.out.println("3. Add user");
        System.out.println("4. Borrow book");
        System.out.println("5. Exit");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();

        switch (choice) {

            case "1":
                library.printAllBooks();
                break;

            case "2":
                System.out.print("Enter title: ");
                String title = scanner.nextLine();

                Book book = library.booksByTitle(title);

                if (book != null) {
                    System.out.println(library.formatBook(book));
                } else {
                    System.out.println("Book not found.");
                }
                break;

            case "3":
                addUser(scanner, library);
                break;

            case "4":
                borrow(scanner, library);
                break;

            case "5":
                running = false;
                System.out.println("Goodbye!");
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

        scanner.close();
}

    }
}
