package com.nology;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {

    // Track those kinders

    private Map<String, User> usersById = new HashMap<>();

    // making default constructor using csv in my new service

    private String userFile;

    public UserService(String userFile) {
        this.userFile = userFile;
    }

    public UserService() {
        this("users.csv");
    }

    public User getUserById(String id) {
        return usersById.get(id);
    }


// This took me way longer than it should to get right, it makes a user.
// I looked into factories, but refactoring for a few "new" calls didn't seem worth it

    public User addUser(String name, String password) {
        if (name == null) {
            System.out.println("You do remember your name?");
            ;
            return null;
        }
        if (password == null) {
            System.out.println("Even Monkeys can learn 8 letters");
            ;
            return null;
        }
        if (password.length() < 8) {
            System.out.println("Please try, just this once");
            return null;
        }

        String id = UUID.randomUUID().toString();
        User user = new User(name, id, password, false);
        usersById.put(id, user);
        saveUser(user);
        System.out.println("User Account Created (your soul is now ours)");
        return user;
    }


// Writes the Users csv

    public void saveUser(User user) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(userFile, true))) {

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

// Reads the Users csv

    public void loadUsers() {
        try (CSVReader reader = new CSVReader(new FileReader(userFile))) {

            String[] line;
            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {

                String id = line[0];
                String name = line[1];
                String password = line[2];

                User user = new User(name, id, password, false);
                usersById.put(id, user);
            }

        } catch (Exception e) {
            System.out.println("Error loading users");
        }
    }

// handles login by checking for existing users

    public User login(String name, String password) {

        for (User user : usersById.values()) {

            if (user.getName().equals(name)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

// Creates a SuperUser every session

public void makeAdmin() {
    User admin = new User(
            "admin",
            "OVERLORD_BOOKSMAXXER_43",
            "admin123",
            true
    );

    usersById.put(admin.getId(), admin);
}
}
