package com.mycompany.oopassignment;

public class Customer extends User {

    public Customer() {
        super();
    }

    public Customer(String username, String password, String name, String email, String phoneNumber) {
        super(username, password, name, email, phoneNumber);
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }
}