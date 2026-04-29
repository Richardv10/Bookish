package com.nology;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Library {

    // Make HashMap
    private Map<String, Book> booksByTitle = new HashMap<>();
    // Make Borrowing Record
    private List<Record> record = new ArrayList<>();
    // Track those kinders
    private Map<Integer, User> usersById = new HashMap<>();


    public void loadBooks(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
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

    private String getSafeVal(String[] line, int index) {
            return (index < line.length && line[index] != null) ? line[index].trim() : "";
        }

        public void printAllBooks() {
        for(Book book : booksByTitle.values()) {
            System.out.print(formatBook(book));
        }
        }

        private String formatBook(Book book) {
        return "----------------------\n" +
                "Title: " + book.getTitle() + "\n" +
                "Author: " + book.getAuthor() + "\n" +
                "Genre: " + book.getGenre() + "\n" +
                "Publisher: " + book.getPublisher() + "\n" +
                "----------------------";
    }

    public void addUser(User user) {
        usersById.put(user.getId(), user);
    }

public boolean borrowBook(int id, String title) {
    User user = usersById.get(id);
    if (user == null) return false;

    Book book = booksByTitle.get(title.toLowerCase());
    if (book == null) return false;

    if (!book.isAvailable()) return false;

    book.setAvailable(false);

    record.add(new Record(id, book.getTitle()));

    return true;
}


    public List<Book> searchByTitle(String title) {

        if (title == null || title.isBlank()) {
            return new ArrayList<>();
        }

        String lowerTitle = title.toLowerCase();

        return booksByTitle.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerTitle))
                .toList();
    }

}


