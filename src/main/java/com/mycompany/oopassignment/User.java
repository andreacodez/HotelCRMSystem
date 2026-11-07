package com.mycompany.oopassignment;

public abstract class User implements Authenticate{
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;

    public User() {
        this.username = "";
        this.password = "";
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
    }

    public User(String username, String password, String name, String email, String phoneNumber) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public abstract String getRole();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUsername(String username) {
        if (username != null && username.matches("^[a-zA-Z0-9_]{5,20}$")) {
            this.username = username;
        } else {
            this.username = "";
        }
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 8) {
            this.password = password;
        } else {
            this.password = "";
        }
    }

    public void setName(String name) {
        if (name != null && name.matches("^[a-zA-Z\\s]{5,50}$")) {
            this.name = name;
        } else {
            this.name = "";
        }
    }

    public void setEmail(String email) {
        if (email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.email = email;
        } else {
            this.email = "";
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.matches("^\\+?[0-9]{10,11}$")) {
            this.phoneNumber = phoneNumber;
        } else {
            this.phoneNumber = "";
        }
    }

    public boolean checkPassword(String password) {
        return this.password != null && this.password.equals(password);
    }
}