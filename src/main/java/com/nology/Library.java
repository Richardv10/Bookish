package com.nology;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Library {
    private String borrowedFile;
    private UserService userService;

    public Library(String borrowedFile, UserService userService) {
        this.borrowedFile = borrowedFile;
        this.userService = userService;
    }

    public Library(UserService userService) {
        this("borrowed.csv", userService);
    }
    // HashMap of books

    private Map<String, Book> booksByTitle = new HashMap<>();


    // Track Borrowing count

    private Map<String, Integer> borrowCountByTitle = new HashMap<>();


    // Map the borrowed csv

    private Map<String, List<String>> borrowedBooksByUser = new HashMap<>();


    // Loads the csv, reads by line. Helper getSafeVal returns a trimmed or empty string

    public void loadBooks() {
        try (CSVReader reader = new CSVReader(new FileReader("books.csv"))) {
            String[] line;

            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {

                // Guard Clause
                if (line.length < 5) {
                    continue;
                }
                String title = getSafeVal(line, 0);
                String author = getSafeVal(line, 1);
                String genre = getSafeVal(line, 2);
                String publisher = getSafeVal(line, 4);

                Book book = new Book(title, author, genre, publisher);
                booksByTitle.put(title.toLowerCase(), book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Guards against O.O.B errors, then trims the whitespace off the string

    private String getSafeVal(String[] line, int index) {
        if (index < line.length && line[index] != null) {
            return line[index].trim();
        } else {
            return "";
        }
    }


    // This wasn't meant to still be here Remember to make it useful somehow, or put a bow on it or something?

    public void printAllBooks() {
        for(Book book : booksByTitle.values()) {
            System.out.print(formatBook(book));
            }
        }


        // I stole this, but it seems pretty universal

    String formatBook(Book book) {
        return "----------------------\n" +
                "Title: " + book.getTitle() + "\n" +
                "Author: " + book.getAuthor() + "\n" +
                "Genre: " + book.getGenre() + "\n" +
                "Publisher: " + book.getPublisher() + "\n" +
                "----------------------";

        }

    //Book borrowing logic (with new and improved error handling on the hashmaps and 99% fat free)

public boolean borrowBook(String id, String title) {
    User user = userService.getUserById(id);
    if (user == null) return false;

    Book book = booksByTitle.get(title.toLowerCase());
    if (book == null) return false;

    if (!book.isAvailable()) return false;

    book.setAvailable(false);

    borrowedBooksByUser
            .computeIfAbsent(id, k -> new ArrayList<>())
            .add(book.getTitle());

    borrowCountByTitle.put(
            book.getTitle(),
            borrowCountByTitle.getOrDefault(book.getTitle(), 0) + 1
    );

    saveBorrowRecord(id, book.getTitle(), "BORROW");

    return true;
}



// Writes to borrowing record csv

    public void saveBorrowRecord(String userId, String bookTitle, String action) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(borrowedFile, true))) {

            String[] record = { userId, bookTitle, action };
            writer.writeNext(record);

        } catch (IOException e) {
            System.out.println("Error saving borrow record");
        }
    }



// Loads the Borrowed record into a Hashmap

    public void loadBorrowed() {

        try (CSVReader reader = new CSVReader(new FileReader(borrowedFile))) {

            String[] line;
            // reader.readNext(); // skip header if using header, (which you're not)

            while ((line = reader.readNext()) != null) {

                // csv structural check
                if (line.length < 3) continue;

                String userId = line[0];
                String bookTitle = line[1];
                String action = line[2];

                if (action.equals("BORROW")) {

                    borrowedBooksByUser
                            .computeIfAbsent(userId, k -> new ArrayList<>())
                            .add(bookTitle);

                    borrowCountByTitle.put(
                            bookTitle,
                            borrowCountByTitle.getOrDefault(bookTitle, 0) + 1
                    );

                    Book book = booksByTitle.get(bookTitle.toLowerCase());
                    if (book != null) {
                        book.setAvailable(false);
                    }

                } else if (action.equals("RETURN")) {

                    List<String> books = borrowedBooksByUser.get(userId);

                    if (books != null) {
                        books.remove(bookTitle);
                    }

                    Book book = booksByTitle.get(bookTitle.toLowerCase());
                    if (book != null) {
                        book.setAvailable(true);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading borrowed records");
        }
    }



// Return Book Logic

    public boolean returnBook(String userId, String title) {

        List<String> userBooks = borrowedBooksByUser.get(userId);

        if (userBooks == null || !userBooks.contains(title)) {
            return false;
        }

        userBooks.remove(title);

        Book book = booksByTitle.get(title.toLowerCase());

        if (book != null) {
            book.setAvailable(true);
        }

        saveBorrowRecord(userId, title, "RETURN");

        return true;
    }


    // Displays Users Books (probs needs updating)

    public void showUserBooks(String userId) {

        List<String> books = borrowedBooksByUser.get(userId);

        if (books == null || books.isEmpty()) {
            System.out.println("You have no borrowed books.");
            return;
        }

        System.out.println("\n><>< My Books ><><");

        for (String title : books) {
            Book book = booksByTitle.get(title.toLowerCase());

            if (book != null) {
                System.out.println(formatBook(book));
            } else {
                // fallback if something went weird
                System.out.println(title);
            }
        }
    }


// Getter for borrowing (With error handling)

    public int getBorrowCount(String title) {
        return borrowCountByTitle.getOrDefault(title, 0);
    }

    // getter for returning

    public List<String> getBorrowedBooks(String userId) {
        return borrowedBooksByUser.getOrDefault(userId, new ArrayList<>());
    }

    // "Basic" hashmap search function (This has taken me from 16.41 to 18.44 because I had  learn how streams work to use filter)
    // So I called it "The book toucher" to amuse myself
    // (Don't worry, it's already on a list)

    public List<Book> searchByTitle(String title) {

        if (title == null || title.isBlank()) {
            return new ArrayList<>();
        }

        String lowerTitle = title.trim().toLowerCase();

        // This is cool, I feel I'll use this A LOT, streaming data from hashmap->pass it into a filter that runs the search, then into a list
        return booksByTitle.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerTitle))
                .toList();
    }


// The horrors persist... Now so do the flags

    public void loadBookState() {

        for (Book book : booksByTitle.values()) {
            book.setAvailable(true);
        }

        for (List<String> titles : borrowedBooksByUser.values()) {
            for (String title : titles) {
                Book book = booksByTitle.get(title.toLowerCase());
                if (book != null) {
                    book.setAvailable(false);
                }
            }
        }
    }

// helpers for testing

    public void addTestBook(Book book) {
        booksByTitle.put(book.getTitle().toLowerCase(), book);
    }


// Passthrough methods for UserService

    public User addUser(String name, String password) {
        return userService.addUser(name, password);
    }

    public User login(String name, String password) {
        return userService.login(name, password);
    }

    public User getUserById(String id) {
        return userService.getUserById(id);
    }

    public void loadUsers() {
        userService.loadUsers();
    }



}








