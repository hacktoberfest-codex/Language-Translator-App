package com.example.translatorapp.ui.elements.models;

public class User {
    private String username;
    private String Name;
    private String Uid;

    public User(String username, String name, String Uid, String email) {
        this.username = username;
        Name = name;
        this.Uid = Uid;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public User() {
    }
}
