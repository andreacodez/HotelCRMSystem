package com.mycompany.oopassignment;

public class Manager extends User {

    public Manager() {
        super();
    }

    public Manager(String username, String password, String name, String email, String phoneNumber) {
        super(username, password, name, email, phoneNumber);
    }

    @Override
    public String getRole() {
        return "MANAGER";
    }
}