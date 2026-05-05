package com.nology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    private Library library;
    private UserService userService;

    @BeforeEach
    void setup() {
        new File("userTest.csv").delete();
        new File("borrowTest.csv").delete();

        userService = new UserService("userTest.csv");
        library = new Library("userTest.csv", userService);


    }

    // User Story: As a logged in user, I borrow a book. Come back later and it's still there
    // Create + Update
    // Acceptance criteria: Checking if the test book remains attached to the user after reload
    @Test
    void BorrowBook() {
        User user = userService.addUser("Tester", "123456789");

        Book book = new Book("Learn Java in a day", "Jamie Chan", "Fiction", "Own garage");

        library.addTestBook(book);

        // Actually borrow the book (Update)
        library.borrowBook(user.getId(), "Learn Java in a day");

        // Reload Library to simulate new session

        Library reloaded = new Library("userTest.csv", userService);


        reloaded.addTestBook(book);
        reloaded.loadBorrowed();

        List<String> books = reloaded.getBorrowedBooks(user.getId());

        assertTrue(books.contains("Learn Java in a day"));
    }




}

