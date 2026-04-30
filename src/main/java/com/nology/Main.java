package com.nology;
import java.sql.SQLOutput;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.loadUsers();
        library.loadBooks();

        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(library, scanner);
        menu.start();
    }
}