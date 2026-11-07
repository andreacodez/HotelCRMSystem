package com.mycompany.oopassignment;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class UserService {
    
    // ======================== CONSTANTS ========================
    private static final String FILE_NAME = "Users.txt";
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,11}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{5,20}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{5,50}$");
    
    // ======================== FIELDS ========================
    private ArrayList<Customer> customers;
    
    // ======================== CONSTRUCTOR ========================
    public UserService(ArrayList<Customer> customers) {
        this.customers = customers;
    }
    
    // ======================== VALIDATION METHODS ========================
    private boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    private boolean isValidUsername(String username) {
        if (isEmptyOrNull(username)) return false;
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    private boolean isValidName(String name) {
        if (isEmptyOrNull(name)) return false;
        return NAME_PATTERN.matcher(name).matches();
    }
    
    private boolean isValidEmail(String email) {
        if (isEmptyOrNull(email)) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    private boolean isValidPhoneNumber(String phone) {
        if (isEmptyOrNull(phone)) return false;
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    private boolean isValidPassword(String password) {
        if (isEmptyOrNull(password)) return false;
        return password.length() >= 8;
    }
    
    private boolean isUsernameUnique(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isEmailUnique(String email) {
        for (Customer c : customers) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
       }

    private boolean isPhoneNumberUnique(String phoneNumber) {
        for (Customer c : customers) {
            if (c.getPhoneNumber().equals(phoneNumber)) {
                return false;
            }
        }
        return true;
    }
    
    // ======================== INPUT METHODS ========================
    private String getStringWithBack(Scanner input, String prompt, int width, boolean allowEmpty) {
        while (true) {
            System.out.printf("%-" + width + "s: ", prompt);
            String line = input.nextLine().trim();
            
            if (line.equalsIgnoreCase("b")) {
                return "BACK";
            }
            
            if (!allowEmpty && line.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a value or 'b' to go back.\n");
                continue;
            }
            
            return line;
        }
    }
    
    
    private String getPasswordWithBack(Scanner input, String prompt, int width) {
        while (true) {
            System.out.printf("%-" + width + "s: ", prompt);
            String line = input.nextLine();
            
            if (line.equalsIgnoreCase("b")) {
                return "BACK";
            }
            
            if (line.isEmpty()) {
                System.out.println("Password cannot be empty. Please enter a password or 'b' to go back.\n");
                continue;
            }
            
            return line;
        }
    }
    
    private String formatNameForDisplay(String name) {
    if (name == null || name.isEmpty()) {
        return name;
    }
    
    String[] words = name.trim().split("\\s+");
    StringBuilder formattedName = new StringBuilder();
    
    for (String word : words) {
        if (word.length() > 0) {
            formattedName.append(word.substring(0, 1).toUpperCase());
            if (word.length() > 1) {
                formattedName.append(word.substring(1).toLowerCase());
            }
            formattedName.append(" ");
        }
    }
    
    return formattedName.toString().trim();
}
    // Helper
    private <T extends Authenticate> T authenticateUser(ArrayList<T> users, String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String normalizedUsername = username.toUpperCase();

        for (T user : users) {
            if (user.getUsername().toUpperCase().equals(normalizedUsername)
                    && user.checkPassword(password)) {
                return user;
            }
        }

        return null;
    }
    
    // ======================== USER REGISTRATION ========================
    public void register(Scanner input) {
    String username, name, email, phoneNumber, password, confirmPassword;
    int promptWidth = 38;

    System.out.println("\n------------------- Customer Registration Page -------------------");
    System.out.println("(Press 'b' at any time to cancel registration)\n");

    // Username input
    while (true) {
        username = getStringWithBack(input, "Enter username", promptWidth, false);
        if (username.equals("BACK")) {
            System.out.println("Registration cancelled. Returning to menu.\n");
            return;
        }
        
        username = username.toUpperCase();
        
        if (!isValidUsername(username)) {
            System.out.println("Invalid username format. Use 5-20 characters (letters, numbers, underscore only).\n");
            continue;
        }

        if (!isUsernameUnique(username)) {
            System.out.println("Username already exists. Try a different one.\n");
            continue;
        }
        
        break;
    }

    // Name input
    while (true) {
        name = getStringWithBack(input, "Enter full name", promptWidth, false);
        if (name.equals("BACK")) {
            System.out.println("Registration cancelled. Returning to menu.\n");
            return;
        }

        if (!isValidName(name)) {
            System.out.println("Invalid name format. Use 5-50 characters (letters and spaces only).\n");
            continue;
        }

        break;
    }

    // Email input with duplicate check
    while (true) {
        email = getStringWithBack(input, "Enter email", promptWidth, false);
        if (email.equals("BACK")) {
            System.out.println("Registration cancelled. Returning to menu\n");
            return;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Please enter a valid email address.\n");
            continue;
        }
        
        if (!isEmailUnique(email)) {
            System.out.println("Email already registered. Please use a different email address.\n");
            continue;
        }

        break;
    }

    // Phone number input with duplicate check
    while (true) {
        phoneNumber = getStringWithBack(input, "Enter phone number", promptWidth, false);
        if (phoneNumber.equals("BACK")) {
            System.out.println("Registration cancelled. Returning to menu.\n");
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            System.out.println("Invalid phone number format. Use 10-11 digits.\n");
            continue;
        }
        
        if (!isPhoneNumberUnique(phoneNumber)) {
            System.out.println("Phone number already registered. Please use a different phone number.\n");
            continue;
        }

        break;
    }

    // Password input
    while (true) {
        password = getPasswordWithBack(input, "Enter password (min 8 chars)", promptWidth);
        if (password.equals("BACK")) {
            System.out.println("Registration cancelled. Returning to menu.\n");
            return;
        }
        
        if (!isValidPassword(password)) {
            System.out.println("Password too short. Must be at least 8 characters.\n");
            continue;
        }
        
        confirmPassword = getPasswordWithBack(input, "Confirm password", promptWidth);
        if (confirmPassword.equals("BACK")) {
            System.out.println("Registration cancelled. Returning to menu.\n");
            return;
        }

        if (password.equals(confirmPassword)) {
            break;
        } else {
            System.out.println("Passwords do not match. Try again.\n");
        }
    }

    Customer newCustomer = new Customer(username.toUpperCase(), password, name, email, phoneNumber);
    customers.add(newCustomer);
    saveUser(username, password, name, email, phoneNumber);

    System.out.println("\nRegistration successful!");
    System.out.println("Welcome " + formatNameForDisplay(name) + "!");
}
    
    // ======================== USER LOGIN ========================
    public Customer login(Scanner input) {
        Customer foundCustomer = null;
        String username;
        int promptWidth = 25;

        System.out.println("\n----------------------- Customer Login Page ----------------------");
        System.out.println("(Press 'b' at any time to go back)\n");
        
        // If no customer register before
        if (customers.isEmpty()) {
        System.out.println("No registered customers found.");
        System.out.println("Please register a new account first.\n");
        return null;
        }
        
        // Username input
        while (true) {
            username = getStringWithBack(input, "Enter username", promptWidth, false);
            if (username.equals("BACK")) {
                System.out.println("Login cancelled. Returning to menu.\n");
                return null;
            }
            
            String upperUsername = username.toUpperCase();
            
            for (Customer c : customers) {
            if (c.getUsername().toUpperCase().equals(upperUsername)) {
                foundCustomer = c;
                break;
            }
        }

            if (foundCustomer == null) {
                System.out.println("Invalid username. Please try again or press 'b' to go back.\n");
            } else {
                break;
            }
        }

        // Password input
        while (true) {
            String password = getPasswordWithBack(input, "Enter password", promptWidth);
            if (password.equals("BACK")) {
                System.out.println("Login cancelled. Returning to menu.\n");
                return null;
            }

            Customer authenticatedCustomer = authenticateUser(customers, username, password);

            if (authenticatedCustomer != null) {
                System.out.println("\nLogin successful!");
                System.out.println("Welcome " + formatNameForDisplay(authenticatedCustomer.getName()) + "!");
                return authenticatedCustomer;
            } else {
                System.out.println("Invalid password. Please try again or press 'b' to go back.\n");
            }
        }
    }
    
    private String formatUsernameForDisplay(String username) {
            if (username == null || username.isEmpty()) {
                return username;
            }
            return username.substring(0, 1).toUpperCase() + 
                   username.substring(1).toLowerCase();
        }
    
    // ======================== FILE OPERATIONS ========================
    public void saveUser(String username, String password, String name, String email, String phoneNumber) {
    try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
        writer.write(username.toUpperCase() + "," + password + "," + name + "," + email + "," + phoneNumber + "\n");
    } catch (IOException e) {
        System.out.println("Error saving user.");
    }
}
    
    public void loadUsers() {
    try {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] data = line.split(",");
            if (data.length == 5) {
                boolean exists = false;
                for (Customer c : customers) {
                    if (c.getUsername().equals(data[0])) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    customers.add(new Customer(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        }
        fileScanner.close();
    } catch (Exception e) {
        System.out.println("Error loading users.");
    }
}
    
    public void saveAllUsers() {
    try (FileWriter writer = new FileWriter(FILE_NAME)) {
        for (Customer c : customers) {
            writer.write(c.getUsername().toUpperCase() + "," + c.getPassword() + "," + c.getName() + "," + c.getEmail() + "," + c.getPhoneNumber() + "\n");
        }
    } catch (IOException e) {
        System.out.println("Error saving users.");
    }
}
    
    // ======================== GETTER ========================
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
    
    // ======================== PASSWORD MANAGEMENT ========================
    public void changePassword(Scanner input, String username) {
        Customer user = null;
        int promptWidth = 37;
        int promptWidth2 = 36;
        int promptWidth3 = 35;

        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {
                user = c;
                break;
            }
        }

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("\n------------------------- Change Password ------------------------");
        System.out.println("(Press 'b' at any time to cancel)\n");
        
        String currentPassword = getPasswordWithBack(input, "Enter current password", promptWidth);
        if (currentPassword.equals("BACK")) {
            System.out.println("Password change cancelled.\n");
            return;
        }

        if (!user.checkPassword(currentPassword)) {
            System.out.println("Incorrect password.");
            return;
        }

        String newPassword;
        while (true) {
            newPassword = getPasswordWithBack(input, "Enter new password (min 8 characters)", promptWidth2);
            if (newPassword.equals("BACK")) {
                System.out.println("Password change cancelled.\n");
                return;
            }

            if (newPassword.length() < 8) {
                System.out.println("Password too short. Must be at least 8 characters.\n");
            } else {
                break;
            }
        }
                                                            
        String confirmPassword = getPasswordWithBack(input, "Confirm new password", promptWidth);
        if (confirmPassword.equals("BACK")) {
            System.out.println("Password change cancelled.\n");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Password change failed.\n");
            return;
        }

        user.setPassword(newPassword);
        saveAllUsers();
        System.out.println("\nPassword updated successfully!\n");
    }
    
    public void resetPassword(Scanner input) {
        int promptWidth = 37;

        System.out.println("\n------------------------- Reset Password -------------------------");
        System.out.println("(Press 'b' at any time to cancel)\n");
                                                   
        String username = getStringWithBack(input, "Enter username", promptWidth, false);
        if (username.equals("BACK")) {
            System.out.println("Password reset cancelled.\n");
            return;
        }

        String upperUsername = username.toUpperCase();
    for (Customer c : customers) {
        if (c.getUsername().toUpperCase().equals(upperUsername)) {
                String newPassword;
                while (true) {
                    newPassword = getPasswordWithBack(input, "Enter new password (min 8 characters)", promptWidth);
                    if (newPassword.equals("BACK")) {
                        System.out.println("Password reset cancelled.\n");
                        return;
                    }

                    if (newPassword.length() < 8) {
                        System.out.println("Password too short. Must be at least 8 characters.\n");
                    } else {
                        break;
                    }
                }
                                                                   
                String confirmPassword = getPasswordWithBack(input, "Confirm new password", promptWidth);
                if (confirmPassword.equals("BACK")) {
                    System.out.println("Password reset cancelled.\n");
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("Passwords do not match. Password reset failed.\n");
                    return;
                }

                c.setPassword(newPassword);
                saveAllUsers();
                System.out.println("\nPassword reset successful!\n");
                return;
            }
        }

        System.out.println("User not found. Please check your username or register a new account.\n");
    }
    
    // ======================== VIEW PROFILE ========================
    public void viewCustomerProfile(Scanner input) {
    int promptWidth = 28;

    System.out.println("\n---------------------- View Customer Profile ---------------------");
    System.out.println("(Press 'b' to go back)\n");
    
    if (customers.isEmpty()) {
        System.out.println("No registered customers in the system.\n");
        return;
    }
                                                   
    String username = getStringWithBack(input, "Enter customer username", promptWidth, false);
    if (username.equals("BACK")) {
        System.out.println("Returning to menu");
        return;
    }
    
    String upperUsername = username.toUpperCase();
    for (Customer c : customers) {
        if (c.getUsername().toUpperCase().equals(upperUsername)) {
            System.out.println("\n------------------------------------------------------------------");
            System.out.println("                         CUSTOMER PROFILE");
            System.out.println("------------------------------------------------------------------");
            System.out.printf("%-15s: %s%n", "Username", c.getUsername());
            System.out.printf("%-15s: %s%n", "Name", c.getName());
            System.out.printf("%-15s: %s%n", "Email", c.getEmail());
            System.out.printf("%-15s: %s%n", "Phone Number", c.getPhoneNumber());
            System.out.println("------------------------------------------------------------------");
            return;
        }
    }

    System.out.println("Customer not found.\n");
    }
}