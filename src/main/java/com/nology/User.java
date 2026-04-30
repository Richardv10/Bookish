package com.nology;

public class User {
    String name;
    String id;
    String password;

    public User(String name, String id, String password) {
        this.name = name;
        this.id = id;
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }











}

