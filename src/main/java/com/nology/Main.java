package com.nology;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Library library = new Library(userService);
        library.loadBooks();
        userService.loadUsers();
        library.loadBorrowed();
        library.loadBookState();
        userService.makeAdmin();
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(library, scanner);
        menu.start();
    }
}