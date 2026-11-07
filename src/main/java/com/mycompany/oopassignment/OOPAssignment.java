package com.mycompany.oopassignment;

import java.util.*;

public class OOPAssignment {

    // ======================== DATA STORAGE ========================
    static ArrayList<Customer> customers = new ArrayList<>();
    static ArrayList<Ticket> tickets = new ArrayList<>();
    static ArrayList<Staff> staffs = new ArrayList<>();
    static ArrayList<Manager> managers = new ArrayList<>();
    static ArrayList<User> allUsers = new ArrayList<>();

    // ======================== SERVICES ========================
    static UserService userService = new UserService(customers);
    static TicketService ticketService = new TicketService(tickets, staffs);

    static final String FILE_NAME = "Users.txt";

    public static int getValidatedIntInput(Scanner input, String prompt, int min, int max) {
        String inputLine;
        int result;

        while (true) {
            System.out.print(prompt);
            inputLine = input.nextLine().trim();

            if (inputLine.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a number between " + min + " and " + max + ".");
                continue;
            }

            try {
                result = Integer.parseInt(inputLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number between " + min + " and " + max + ".");
                continue;
            }

            if (result < min || result > max) {
                System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                continue;
            }

            return result;
        }
    }

    private static void pressEnterToContinue(Scanner input) {
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
        System.out.println();
    }

    private static void showReturnMessage() {
        System.out.println("Returning to role selection...");
    }

    private static String getNonEmptyInput(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.\n");
            } else {
                return value;
            }
        }
    }
    
    private static String formatNameForDisplay(String name) {
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

    private static <T extends Authenticate> T authenticateUser(ArrayList<T> users, String username, String password) {
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
    
    private static void initializeDefaultUsers() {
        Staff staff1 = new Staff("ANDREA", "andrea123", "Andrea", "andrea@tulip.com", "0123456789");
        Staff staff2 = new Staff("GLADYS", "gladys123", "Gladys", "gladys@tulip.com", "0112233445");
        Staff staff3 = new Staff("KYRAA", "kyra1234", "Kyra Leong", "kyra@tulip.com", "0122233345");
        Staff staff4 = new Staff("LIEWYX", "liew1234", "Yong Xin", "liew@tulip.com", "0111233411");
        Staff staff5 = new Staff("YINGYING", "ying1234", "Kah Ying", "ying@tulip.com", "0116263445");
        
        Manager manager1 = new Manager("MANAGER", "manager123", "Manager", "manager@tulip.com", "0198877665");

        staffs.add(staff1);
        staffs.add(staff2);
        staffs.add(staff3);
        staffs.add(staff4);
        staffs.add(staff5);
        managers.add(manager1);

        allUsers.add(staff1);
        allUsers.add(staff2);
        allUsers.add(staff3);
        allUsers.add(staff4);
        allUsers.add(staff5);
        allUsers.add(manager1);
    }

    private static void showMainBanner() {
        System.out.println();
        System.out.println("==================================================================");
        System.out.println("   _______ _    _ _      _____ _____         _____ _____  __  __ ");
        System.out.println("  |__   __| |  | | |    |_   _|  __ \\       / ____|  __ \\|  \\/  |");
        System.out.println("     | |  | |  | | |      | | | |__) |     | |    | |__) | \\  / |");
        System.out.println("     | |  | |  | | |      | | |  ___/      | |    |  _  /| |\\/| |");
        System.out.println("     | |  | |__| | |____ _| |_| |          | |____| | \\ \\| |  | |");
        System.out.println("     |_|   \\____/|______|_____|_|           \\_____|_|  \\_\\_|  |_|");
        System.out.println("\n=============== Welcome to Tulip Hotel CRM System ================");
    }

    // ======================== MAIN MENU ========================
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            userService.loadUsers();
            initializeDefaultUsers();
            ticketService.loadTickets(customers, staffs);

            int roleChoice;

            do {
                showMainBanner();
                System.out.println("--------------------------- SELECT ROLE --------------------------");
                System.out.println("  1. Customer");
                System.out.println("  2. Staff");
                System.out.println("  3. Manager");
                System.out.println("  4. Exit");
                System.out.println("------------------------------------------------------------------");

                roleChoice = getValidatedIntInput(input, "\nEnter choice: ", 1, 4);
                System.out.println();

                switch (roleChoice) {
                    case 1:
                        customerMainMenu(input);
                        break;
                    case 2:
                        staffMainMenu(input);
                        break;
                    case 3:
                        managerMainMenu(input);
                        break;
                    case 4:
                        System.out.println("Exiting program...");
                        System.out.println("Thank you for using! Goodbye!");
                        break;
                }
            } while (roleChoice != 4);
        }
    }

    // ======================== CUSTOMER MODULE ========================
    public static void customerMainMenu(Scanner input) {
        int choice;

        do {
            System.out.println("\n----------------------- CUSTOMER MAIN MENU -----------------------");
            System.out.println("  1. Register");
            System.out.println("  2. Login");
            System.out.println("  3. Reset Password");
            System.out.println("  4. Back");
            System.out.println("------------------------------------------------------------------");

            choice = getValidatedIntInput(input, "\nEnter choice: ", 1, 4);
            System.out.println();

            switch (choice) {
                case 1:
                    userService.register(input);
                    break;
                case 2:
                    Customer customer = userService.login(input);
                    if (customer != null) {
                        allUsers.add(customer);
                        customerMenu(input, customer);
                    }
                    break;
                case 3:
                    userService.resetPassword(input);
                    break;
                case 4:
                    showReturnMessage();
                    break;
            }
        } while (choice != 4);
    }

    public static void customerMenu(Scanner input, Customer customer) {
        int choice;

        do {
            System.out.println("\n-------------------------- CUSTOMER MENU -------------------------");
            System.out.println("  1. Create Ticket");
            System.out.println("  2. View All Tickets");
            System.out.println("  3. View Specific Ticket");
            System.out.println("  4. Close Ticket");
            System.out.println("  5. Change Password");
            System.out.println("  6. Logout");
            System.out.println("------------------------------------------------------------------");

            choice = getValidatedIntInput(input, "\nEnter choice: ", 1, 6);
            System.out.println();

            switch (choice) {
                case 1:
                    ticketService.createTicket(input, customer);
                    break;
                case 2:
                    ticketService.viewTickets(customer.getUsername());
                    break;
                case 3:
                    ticketService.viewSpecificTicket(input, customer.getUsername());
                    break;
                case 4:
                    ticketService.closeTicket(input, customer.getUsername());
                    break;
                case 5:
                    userService.changePassword(input, customer.getUsername());
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
            }

            if (choice >= 1 && choice <= 5) {
                pressEnterToContinue(input);
            }
        } while (choice != 6);
    }

    // ======================== STAFF MODULE ========================
    public static void staffMainMenu(Scanner input) {
        int choice;

        do {
            System.out.println("\n------------------------- STAFF MAIN MENU ------------------------");
            System.out.println("  1. Login");
            System.out.println("  2. Back");
            System.out.println("------------------------------------------------------------------");

            choice = getValidatedIntInput(input, "\nEnter choice: ", 1, 2);
            System.out.println();

            switch (choice) {
                case 1:
                    staffLogin(input);
                    break;
                case 2:
                    showReturnMessage();
                    break;
            }
        } while (choice != 2);
    }

    public static void staffLogin(Scanner input) {
        System.out.println("\n--------------------------- STAFF LOGIN ---------------------------");

        String username = getNonEmptyInput(input, "Enter staff username    : ");
        String password = getNonEmptyInput(input, "Enter staff password    : ");

        Staff staff = authenticateUser(staffs, username, password);

        if (staff != null) {
            System.out.println("\nLOGIN SUCCESSFUL!");
            System.out.println("Welcome " + formatNameForDisplay(staff.getName()) + "!");
            staffMenu(input, staff.getUsername());
            return;
        }

        System.out.println("\nLOGIN FAILED! Invalid staff credentials.\n");
        pressEnterToContinue(input);
    }

    public static void staffMenu(Scanner input, String username) {
        int choice;
        
        do {
            System.out.println("\n--------------------------- STAFF MENU ---------------------------");
            System.out.println("  1. View / Filter Tickets");
            System.out.println("  2. Respond to Ticket");
            System.out.println("  3. Merge Duplicate Tickets");
            System.out.println("  4. View Personal Performance");
            System.out.println("  5. View Guest Profile");
            System.out.println("  6. Logout");
            System.out.println("------------------------------------------------------------------");

            choice = getValidatedIntInput(input, "\nEnter choice: ", 1, 6);
            System.out.println();

            switch (choice) {
                case 1:
                    ticketService.viewTicketResponse(input, username);
                    break;
                case 2:
                    ticketService.respondToTicket(input, username);
                    break;
                case 3:
                    ticketService.mergeDuplicateTickets(input, username);
                    break;
                case 4:
                    ticketService.staffPerformance(username);
                    pressEnterToContinue(input);
                    break;
                case 5:
                    userService.viewCustomerProfile(input);
                    pressEnterToContinue(input);
                    break;
                case 6:
                    System.out.println("Logging out...\n");
                    break;
            }
        } while (choice != 6);
    }

    // ======================== MANAGER MODULE ========================
    public static void managerMainMenu(Scanner input) {
        int choice;

        do {
            System.out.println("\n------------------------ MANAGER MAIN MENU -----------------------");
            System.out.println("  1. Login");
            System.out.println("  2. Back ");
            System.out.println("------------------------------------------------------------------");

            choice = getValidatedIntInput(input, "\nEnter choice: ", 1, 2);
            System.out.println();

            switch (choice) {
                case 1:
                    managerLogin(input);
                    break;
                case 2:
                    showReturnMessage();
                    break;
            }
        } while (choice != 2);
    }

    public static void managerLogin(Scanner input) {
        System.out.println("-------------------------- MANAGER LOGIN -------------------------");

        String username = getNonEmptyInput(input, "Enter manager username  : ");
        String password = getNonEmptyInput(input, "Enter manager password  : ");

        Manager manager = authenticateUser(managers, username, password);

        if (manager != null) {
            System.out.println("\nLOGIN SUCCESSFUL!\n");
            System.out.println("Welcome Manager!");
            managerMenu(input);
            return;
        }

        System.out.println("LOGIN FAILED! Invalid manager credentials.\n");
        pressEnterToContinue(input);
    }

    public static void managerMenu(Scanner input) {
        int choice;

        do {
            System.out.println();
            System.out.println("-------------------------- MANAGER MENU --------------------------");
            System.out.println("  1. Overall Performance Dashboard");
            System.out.println("  2. Monthly Report ");
            System.out.println("  3. Reassign Ticket ");
            System.out.println("  4. Filter / Search Tickets");
            System.out.println("  5. View Guest Profile");
            System.out.println("  6. View Staff List");
            System.out.println("  7. Logout");
            System.out.println("------------------------------------------------------------------");

            choice = getValidatedIntInput(input, "\nEnter choice: ", 1, 7);
            System.out.println();

            switch (choice) {
                case 1:
                    ticketService.overallPerformance();
                    pressEnterToContinue(input);
                    break;
                case 2:
                    ticketService.monthlyReport();
                    pressEnterToContinue(input);
                    break;
                case 3:
                    ticketService.reassignTicket(input);
                    pressEnterToContinue(input);
                    break;
                case 4:
                    ticketService.filterTickets(input);
                    break;
                case 5:
                    userService.viewCustomerProfile(input);
                    pressEnterToContinue(input);
                    break;
                case 6:
                    viewStaffList();
                    pressEnterToContinue(input);
                    break;
                case 7:
                    System.out.println("Logging out...");
                    break;
            }
        } while (choice != 7);
    }

    public static void viewStaffList() {
        if (staffs.isEmpty()) {
            System.out.println("\nNo staff members found.");
            return;
        }

        System.out.println("\n===================================================== STAFF LIST ======================================================");
        System.out.printf("%-3s %-15s %-20s %-25s %-15s %-10s %-10s %-10s%n",
                "No.", "Username", "Name", "Email", "Phone", "Assigned", "Closed", "Performance(%)");
        System.out.println("=======================================================================================================================");

        for (int i = 0; i < staffs.size(); i++) {
            Staff s = staffs.get(i);
            int assignedCount = 0;
            int closedCount = 0;

            for (Ticket t : tickets) {
                if (t.getAssignedStaff() != null &&
                        t.getAssignedStaff().getUsername().equals(s.getUsername())) {
                    assignedCount++;
                    if (t.isClosed()) {
                        closedCount++;
                    }
                }
            }

            double performance = assignedCount > 0 ? (double) closedCount / assignedCount * 100 : 0;

            System.out.printf("%-3d %-15s %-20s %-25s %-15s %-10d %-10d %-10.1f%n",
                    (i + 1),
                    s.getUsername(),
                    s.getName(),
                    s.getEmail(),
                    s.getPhoneNumber(),
                    assignedCount,
                    closedCount,
                    
                    performance);
        }
        System.out.println("=======================================================================================================================");

        System.out.println("\nTotal Staff: " + staffs.size());
    }
}