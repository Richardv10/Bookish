package com.nology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();

        library.loadBooks();
    }
}

// I didn't have time today to look into how to test my app.
// I don't think it's a good sign that it's a fragile state
// The multiple csv files work to save state and data, but I'm going to need
// some helper functions to avoid breaking them