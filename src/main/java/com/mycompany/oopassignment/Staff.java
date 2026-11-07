package com.mycompany.oopassignment;

public class Staff extends User {

    public Staff() {
        super();
    }

    public Staff(String username, String password, String name, String email, String phoneNumber) {
        super(username, password, name, email, phoneNumber);
    }

    @Override
    public String getRole() {
        return "STAFF";
    }
}