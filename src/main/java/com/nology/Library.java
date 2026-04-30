package com.nology;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Library {

    // Make HashMap of books
    private Map<String, Book> booksByTitle = new HashMap<>();
    // Make Borrowing Record
    // private List<Record> record = new ArrayList<>();
    // Track those kinders
    private Map<String, User> usersById = new HashMap<>();
    // Maps the borrowed csv
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



// If you're e
    public User addUser(String name, String password) {
        if (name == null) {
            System.out.println("You do remember your name?");;
            return null;
        }
        if (password == null) {
            System.out.println("Even Monkeys can learn 8 letters");;
            return null;
        }
        if (password.length() < 8) {
            System.out.println("Please try, just this once");
            return null;
        }

        String id = UUID.randomUUID().toString();
        User user = new User(name, id, password);
        usersById.put(id, user);
        saveUser(user);
        System.out.println("User Account Created (your soul is now ours)");
        return user;



    }

    public void saveUser(User user) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("users.csv", true))) {

            String[] record = {
                    user.getId(),
                    user.getName(),
                    user.getPassword()
            };

            writer.writeNext(record);

        } catch (IOException e) {
            System.out.println("Error saving user");
        }
    }

    public void loadUsers() {
        try (CSVReader reader = new CSVReader(new FileReader("users.csv"))) {

            String[] line;
            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {

                String id = line[0];
                String name = line[1];
                String password = line[2];

                User user = new User(name, id, password);
                usersById.put(id, user);
            }

        } catch (Exception e) {
            System.out.println("Error loading users");
        }
    }


    public User login(String name, String password) {

        for (User user : usersById.values()) {

            if (user.getName().equals(name)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }




        //Book borrowing logic

public boolean borrowBook(String id, String title) {
    User user = usersById.get(id);
    if (user == null) return false;

    Book book = booksByTitle.get(title.toLowerCase());
    if (book == null) return false;

    if (!book.isAvailable()) return false;

    book.setAvailable(false);

    saveBorrowRecord(id, book.getTitle());
    borrowedBooksByUser
            .computeIfAbsent(id, k -> new ArrayList<>())
            .add(book.getTitle());
    return true;
}

    public void saveBorrowRecord(String userId, String bookTitle) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("borrowed.csv", true))) {

            String[] record = { userId, bookTitle };
            writer.writeNext(record);

        } catch (IOException e) {
            System.out.println("Error saving borrow record");
        }
    }
// Loads the Borrowed record into a Hashmap
    public void loadBorrowed() {

        try (CSVReader reader = new CSVReader(new FileReader("borrowed.csv"))) {

            String[] line;
            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {

                String userId = line[0];
                String bookTitle = line[1];

                if (!borrowedBooksByUser.containsKey(userId)) {
                    borrowedBooksByUser.put(userId, new ArrayList<>());
                }

                borrowedBooksByUser.get(userId).add(bookTitle);
            }

        } catch (Exception e) {
            System.out.println("Error loading borrowed records");
        }
    }


    // "Basic" hashmap search function (This has taken me from 16.41 to 18.44)

    public List<Book> searchByTitle(String title) {

        if (title == null || title.isBlank()) {
            return new ArrayList<>();
        }

        String lowerTitle = title.trim().toLowerCase();

        return booksByTitle.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerTitle))
                .toList();
    }







}


