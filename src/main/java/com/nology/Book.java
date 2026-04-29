package com.nology;

public class Book {
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private boolean available = true;

    public Book(String title, String author, String genre, String publisher) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Genre: " + genre + "\n" +
                "Publisher: " + publisher;
    }


}
