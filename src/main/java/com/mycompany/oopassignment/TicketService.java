package com.mycompany.oopassignment;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class TicketService {

    private ArrayList<Ticket> tickets;
    private ArrayList<Staff> staffs;

    private static final String FILE_NAME = "Tickets.txt";
    
    private TicketRepository ticketRepository;
    private TicketReportService ticketReportService;
    private TicketPrinter ticketPrinter;
    private TicketAssignmentService ticketAssignmentService;
    private TicketMergeService ticketMergeService;
    
    public TicketService(ArrayList<Ticket> tickets, ArrayList<Staff> staffs) {
        this.tickets = tickets;
        this.staffs = staffs;
        this.ticketRepository = new TicketRepository();
        this.ticketReportService = new TicketReportService(tickets);
        this.ticketPrinter = new TicketPrinter();
        this.ticketAssignmentService = new TicketAssignmentService(staffs);
        this.ticketMergeService = new TicketMergeService();
    }

    private boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String formatCategory(TicketCategory category) {
        switch (category) {
            case FOOD_AND_BEVERAGE:
                return "Food and Beverage";
            case CLEANLINESS:
                return "Cleanliness";
            case FACILITIES:
                return "Facilities";
            default:
                return category.toString();
        }
    }

    private String formatPriority(PriorityLevel priority) {
        switch (priority) {
            case LOW:
                return "Low";
            case MEDIUM:
                return "Medium";
            case HIGH:
                return "High";
            default:
                return priority.toString();
        }
    }

    private String formatStatus(TicketStatus status) {
        switch (status) {
            case OPEN:
                return "Open";
            case IN_PROGRESS:
                return "In Progress";
            case RESOLVED:
                return "Resolved";
            case CLOSED:
                return "Closed";
            default:
                return status.toString();
        }
    }

    private String formatAssignedStaff(Staff staff) {
        return staff == null ? "None" : staff.getUsername();
    }

    private Staff findStaffByUsername(String username) {
        if (isEmptyOrNull(username)) return null;

        for (Staff s : staffs) {
            if (s.getUsername().equalsIgnoreCase(username)) {
                return s;
            }
        }
        return null;
    }

    private int getValidatedInt(Scanner input, String prompt, int min, int max) {
        int result;
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("b")) {
                return -1;
            }

            if (line.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a number between " + min + " and " + max + " or 'b' to go back.\n");
                continue;
            }

            try {
                result = Integer.parseInt(line);
                if (result >= min && result <= max) {
                    return result;
                } else {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + " or 'b' to go back.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or 'b' to go back.\n");
            }
        }
    }

    private String getStringInput(Scanner input, String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
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

    private String getExampleForCategory(TicketCategory category) {
        switch (category) {
            case FOOD_AND_BEVERAGE:
                return "Room service breakfast arrived cold, missing orange juice, delivery took 45 minutes";
            case CLEANLINESS:
                return "Bathroom floor has yellow stains and hair, used towels still hanging from previous guest";
            case FACILITIES:
                return "AC not cooling properly, making rattling noise";
            default:
                return "Please describe your issue in detail with location, time, and specific observations";
        }
    }

    // ======================== INCIDENT DATE/TIME METHODS ========================

    private String getIncidentDateTime(Scanner input, TicketCategory category) {
        System.out.println("\n--------------------- WHEN DID THIS HAPPEN? ----------------------");
        
        // Category-specific prompt
        switch (category) {
            case FOOD_AND_BEVERAGE:
                System.out.println("  Please specify when you placed/received your order:");
                break;
            case CLEANLINESS:
                System.out.println("  Please specify when you noticed the cleanliness issue:");
                break;
            case FACILITIES:
                System.out.println("  Please specify when the facility issue started/occurred:");
                break;
        }
        
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Options:");
        System.out.println("    1. Today (just now / within last few hours)");
        System.out.println("    2. Yesterday");
        System.out.println("    3. Specific date and time");
        System.out.println("    4. Ongoing issue (happening right now)");
        System.out.println("    5. Skip (don't specify)");
        System.out.println("------------------------------------------------------------------");
        System.out.println("press 'b' to cancel ticket creation");
        
        int choice = getValidatedInt(input, "\nSelect option: ", 1, 5);
        
        if (choice == -1) {
            return "BACK";
        }
        
        switch (choice) {
            case 1:
                return "Today, " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"));
                
            case 2:
                return "Yesterday, around " + getTimeOfDay(input);
                
            case 3:
                return getSpecificDateTime(input);
                
            case 4:
                return "Ongoing issue - happening right now";
                
            case 5:
                return "Time not specified";
                
            default:
                return "Time not specified";
        }
    }

    private String getTimeOfDay(Scanner input) {
        System.out.println("\n--------------------- APPROXIMATE TIME ----------------------");
        System.out.println("  1. Morning (6:00 AM - 11:59 AM)");
        System.out.println("  2. Afternoon (12:00 PM - 5:59 PM)");
        System.out.println("  3. Evening (6:00 PM - 11:59 PM)");
        System.out.println("  4. Night / Early Morning (12:00 AM - 5:59 AM)");
        System.out.println("  5. Not sure");
        System.out.println("-------------------------------------------------------------");
        
        int choice = getValidatedInt(input, "\nSelect time of day: ", 1, 5);
        
        switch (choice) {
            case 1:
                return "morning";
            case 2:
                return "afternoon";
            case 3:
                return "evening";
            case 4:
                return "night";
            default:
                return "";
        }
    }

    private String getSpecificDateTime(Scanner input) {
        System.out.println("\n------------------- ENTER SPECIFIC DATE/TIME -------------------");
        System.out.println("  Format examples:");
        System.out.println("    - April 17, 2026 at 8:30 AM");
        System.out.println("    - 17/04/2026 14:30");
        System.out.println("    - Last night around 10 PM");
        System.out.println("    - This morning at breakfast");
        System.out.println("---------------------------------------------------------------");
        
        String dateTime = getStringInput(input, "\nEnter date/time (or 'b' to go back): ", true);
        
        if (dateTime.equals("BACK")) {
            return "BACK";
        }
        
        if (dateTime.isEmpty()) {
            return "Time not specified";
        }
        
        return dateTime;
    }

    

    // ======================== FILE OPERATIONS ========================
    public void saveTicket(Ticket t) {
        ticketRepository.saveTicket(t);
    }
    
    public void saveAllTickets() {
        ticketRepository.saveAllTickets(tickets);
    }

    public void loadTickets(ArrayList<Customer> customers, ArrayList<Staff> staffs) {
        ticketRepository.loadTickets(tickets, customers, staffs);
    }

    // ======================== CUSTOMER METHODS ========================

    public void createTicket(Scanner input, Customer customer) {
        int categoryChoice;
        TicketCategory category;

        while (true) {
            System.out.println();
            System.out.println("----------------------- CREATE NEW TICKET ------------------------");
            System.out.println("Select Category (Press 'b' to cancel):");
            System.out.println(" 1. Food and Beverage");
            System.out.println(" 2. Cleanliness");
            System.out.println(" 3. Facilities");
            System.out.println("------------------------------------------------------------------");

            categoryChoice = getValidatedInt(input, "\nEnter choice: ", 1, 3);

            if (categoryChoice == -1) {
                System.out.println("Ticket creation cancelled. Returning to menu.\n");
                return;
            }

            switch (categoryChoice) {
                case 1:
                    category = TicketCategory.FOOD_AND_BEVERAGE;
                    break;
                case 2:
                    category = TicketCategory.CLEANLINESS;
                    break;
                case 3:
                    category = TicketCategory.FACILITIES;
                    break;
                default:
                    continue;
            }
            break;
        }

        // Display category-specific guidance
        System.out.println("\n------------------------- REMARK GUIDANCE ------------------------");
        ticketPrinter.printCategoryGuidance(category);
        System.out.println("------------------------------------------------------------------");

        // Ask when the issue occurred
        String incidentDateTime = getIncidentDateTime(input, category);
        if (incidentDateTime.equals("BACK")) {
            System.out.println("Ticket creation cancelled. Returning to menu.\n");
            return;
        }

        PriorityLevel priority;
        int priorityChoice;
        while (true) {
            System.out.println("\n------------------------- SELECT PRIORITY ------------------------");
            System.out.println("  1. Low    - Minor issue, no urgency (e.g., Suggestion, minor inconvenience)");
            System.out.println("  2. Medium - Moderate issue, needs attention (e.g., Service delay, discomfort)");
            System.out.println("  3. High   - Critical issue, immediate action required (e.g., Safety hazard, major failure)");
            System.out.println("------------------------------------------------------------------");

            priorityChoice = getValidatedInt(input, "\nEnter choice: ", 1, 3);

            if (priorityChoice == -1) {
                System.out.println("Ticket creation cancelled. Returning to menu.\n");
                return;
            }

            switch (priorityChoice) {
                case 1:
                    priority = PriorityLevel.LOW;
                    break;
                case 2:
                    priority = PriorityLevel.MEDIUM;
                    break;
                case 3:
                    priority = PriorityLevel.HIGH;
                    break;
                default:
                    continue;
            }
            break;
        }

        String remark;
        while (true) {
            // Show example for reference
            System.out.println("\nExample of a GOOD remark:");
            System.out.println("  \"" + getExampleForCategory(category) + "\"");
            System.out.println("------------------------------------------------------------------");
            
            remark = getStringInput(input, "\nEnter your remark (min 5 characters, 'b' to cancel): ", false);
            if (remark.equals("BACK")) {
                System.out.println("Ticket creation cancelled. Returning to menu.\n");
                return;
            }

            if (remark.length() < 5) {
                System.out.println("Remark too short. Please provide more details (minimum 5 characters).");
                System.out.println("A good remark includes: location, specific issue, and when it occurred.");
            } else if (remark.split("\\s+").length < 3) {
                System.out.println("Remark too brief. Please provide more specific details about the issue.");
                System.out.println("Include: what happened, where, and when.");
            } else {
                break;
            }
        }

        // Combine incident time with remark for staff context
        String fullRemark = "[" + incidentDateTime + "] " + remark;
        
        Ticket newTicket = new Ticket(customer, category, fullRemark, priority);
        Staff assignedStaff = ticketAssignmentService.getNextAvailableStaff(tickets);
        if (assignedStaff != null) {
            newTicket.assignStaff(assignedStaff);
            System.out.println("\nTicket assigned to: " + assignedStaff.getName());
        } else {
            System.out.println("\nNo staff available for assignment.");
        }

        tickets.add(newTicket);
        saveTicket(newTicket);

        System.out.println("\nTICKET CREATED SUCCESSFULLY!");
        System.out.println("   Your Ticket ID is: " + newTicket.getTicketId());
        System.out.println("   Incident occurred: " + incidentDateTime);
        System.out.println("   Staff will respond shortly.");
    }

    public void viewTickets(String username) {
        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getCustomerUsername().equals(username)) {
                ticketPrinter.printCustomerTicketDetails(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println("\nNo tickets found.");
        }
    }

    public void viewSpecificTicket(Scanner input, String username) {
        String ticketIdStr = getStringInput(input, "Enter Ticket ID (or 'b' to go back): ", false);
        if (ticketIdStr.equals("BACK")) {
            System.out.println("Returning to menu.\n");
            return;
        }

        int ticketId;
        try {
            ticketId = Integer.parseInt(ticketIdStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid Ticket ID number.\n");
            return;
        }

        for (Ticket t : tickets) {
            if (t.getTicketId() == ticketId && t.getCustomerUsername().equals(username)) {
                ticketPrinter.printCustomerTicketDetails(t);
                return;
            }
        }
        System.out.println("Ticket not found or you don't have access.\n");
    }

    public void closeTicket(Scanner input, String username) {
    int index = 1;
    ArrayList<Ticket> resolvedTickets = new ArrayList<>();

    System.out.println("\n--------------------- CLOSE RESOLVED TICKET ----------------------");

    for (Ticket t : tickets) {
        if (t.getCustomerUsername().equals(username) && t.getStatus() == TicketStatus.RESOLVED) {
            String remarkPreview = t.getRemark().length() > 40
                    ? t.getRemark().substring(0, 37) + "..."
                    : t.getRemark();
            System.out.println(index + ". Ticket #" + t.getTicketId() + " [" + formatCategory(t.getCategory()) + "] " + remarkPreview);
            resolvedTickets.add(t);
            index++;
        }
    }
    System.out.println("------------------------------------------------------------------");

    if (resolvedTickets.isEmpty()) {
        System.out.println("\nNo resolved tickets available to close.");
        return;
    }

    int choice = getValidatedInt(input, "\nSelect ticket number to close (or 'b' to go back): ", 1, resolvedTickets.size());
    if (choice == -1) {
        System.out.println("Returning to menu.\n");
        return;
    }

    Ticket selectedTicket = resolvedTickets.get(choice - 1);
    selectedTicket.closeTicket();

    System.out.println("\nTicket closed successfully!");
    
    // ======================== FEEDBACK SECTION ========================
    System.out.println("\n----------------------- PROVIDE FEEDBACK -------------------------");
    System.out.println("  Your feedback helps us improve our service!");
    System.out.println("  (Press Enter to skip, or 'b' to cancel)");
    System.out.println("------------------------------------------------------------------");
    
    // Rating input with skip option
    System.out.println("\n  Rate the service:");
    System.out.println("    1 = Very Poor");
    System.out.println("    2 = Poor");
    System.out.println("    3 = Average");
    System.out.println("    4 = Good");
    System.out.println("    5 = Excellent");
    System.out.println("------------------------------------------------------------------");
    
    String ratingInput = getStringInput(input, "\nEnter rating (1-5) or press Enter to skip: ", true);
    
    if (ratingInput.equals("BACK")) {
        System.out.println("\nFeedback cancelled. Ticket closed without feedback.");
        saveAllTickets();
        return;
    }
    
    int rating = 0;
    if (!ratingInput.isEmpty()) {
        try {
            rating = Integer.parseInt(ratingInput);
            if (rating < 1 || rating > 5) {
                System.out.println("Invalid rating. Must be between 1-5. Rating skipped.");
                rating = 0;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Rating skipped.");
            rating = 0;
        }
    }
    
    // Feedback comment with skip option
    String feedback = "";
    if (rating > 0) {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("  Additional comments (optional):");
        System.out.println("  (Press Enter to skip)");
        System.out.println("------------------------------------------------------------------");
        feedback = getStringInput(input, "\nEnter feedback: ", true);
        
        if (feedback.equals("BACK")) {
            feedback = "";
        }
        
        if (isEmptyOrNull(feedback)) {
            feedback = "No feedback provided";
        }
        
        selectedTicket.setFeedback(rating, feedback);
        
        // Display star rating confirmation
        System.out.println("\n------------------------------------------------------------------");
        System.out.print("  Rating submitted: ");
        
        System.out.println(" (" + rating + "/5)");
        
        // Display rating label
        String ratingLabel;
        switch (rating) {
            case 1: ratingLabel = "Very Poor"; break;
            case 2: ratingLabel = "Poor"; break;
            case 3: ratingLabel = "Average"; break;
            case 4: ratingLabel = "Good"; break;
            case 5: ratingLabel = "Excellent"; break;
            default: ratingLabel = "";
        }
        if (!ratingLabel.isEmpty()) {
            System.out.println("  Rating: " + ratingLabel);
        }
        System.out.println("------------------------------------------------------------------");
        System.out.println("\nThank you for your valuable feedback!");
    } else {
        selectedTicket.setFeedback(0, "");
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("  Feedback skipped.");
        System.out.println("------------------------------------------------------------------");
    }
    
    saveAllTickets();
    System.out.println("\nTicket #" + selectedTicket.getTicketId() + " has been closed.");
}
    // ======================== STAFF METHODS ========================

    public void viewMyTickets(String staffName) {
        boolean found = false;

        System.out.println("\n--------------------- YOUR ASSIGNED TICKETS ---------------------");

        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName)) {
                ticketPrinter.printTicketDetails(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo tickets assigned to you.");
        }
    }

    public void viewTicketResponse(Scanner input, String staffName) {
        System.out.println("\n---------------------- VIEW / FILTER TICKETS ---------------------");
        System.out.println("  1. View My Assigned Tickets");
        System.out.println("  2. Filter My Tickets by Category");
        System.out.println("  3. Filter My Tickets by Status");
        System.out.println("  4. Search My Tickets by Keyword");
        System.out.println("  5. Back");
        System.out.println("------------------------------------------------------------------");

        int choice = getValidatedInt(input, "\nEnter choice: ", 1, 5);
        if (choice == -1 || choice == 5) {
            System.out.println("Returning to menu.\n");
            return;
        }

        switch (choice) {
            case 1:
                viewMyTickets(staffName);
                break;
            case 2:
                filterMyTicketsByCategory(input, staffName);
                break;
            case 3:
                filterMyTicketsByStatus(input, staffName);
                break;
            case 4:
                searchMyTicketsByKeyword(input, staffName);
                break;
        }
    }

    private void filterMyTicketsByCategory(Scanner input, String staffName) {
        System.out.println("\n----------------------- FILTER BY CATEGORY -----------------------");
        System.out.println("  1. Food and Beverage");
        System.out.println("  2. Cleanliness");
        System.out.println("  3. Facilities");
        System.out.println("  4. Back");
        System.out.println("------------------------------------------------------------------");

        int categoryChoice = getValidatedInt(input, "\nEnter choice: ", 1, 4);
        if (categoryChoice == -1 || categoryChoice == 4) {
            System.out.println("Returning to menu.\n");
            return;
        }

        TicketCategory category;
        switch (categoryChoice) {
            case 1:
                category = TicketCategory.FOOD_AND_BEVERAGE;
                break;
            case 2:
                category = TicketCategory.CLEANLINESS;
                break;
            case 3:
                category = TicketCategory.FACILITIES;
                break;
            default:
                return;
        }

        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName) &&
                t.getCategory() == category) {
                ticketPrinter.printTicketDetails(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo tickets found for this category assigned to you.");
        }
    }

    private void filterMyTicketsByStatus(Scanner input, String staffName) {
        System.out.println("\n------------------------ FILTER BY STATUS ------------------------");
        System.out.println("  1. Open");
        System.out.println("  2. In Progress");
        System.out.println("  3. Resolved");
        System.out.println("  4. Closed");
        System.out.println("  5. Back");
        System.out.println("------------------------------------------------------------------");

        int statusChoice = getValidatedInt(input, "\nEnter choice: ", 1, 5);
        if (statusChoice == -1 || statusChoice == 5) {
            System.out.println("Returning to menu.\n");
            return;
        }

        TicketStatus status;
        switch (statusChoice) {
            case 1:
                status = TicketStatus.OPEN;
                break;
            case 2:
                status = TicketStatus.IN_PROGRESS;
                break;
            case 3:
                status = TicketStatus.RESOLVED;
                break;
            case 4:
                status = TicketStatus.CLOSED;
                break;
            default:
                return;
        }

        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName) &&
                t.getStatus() == status) {
                ticketPrinter.printTicketDetails(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo tickets found with this status assigned to you.");
        }
    }

    private void searchMyTicketsByKeyword(Scanner input, String staffName) {
        String keyword = getStringInput(input, "\nEnter search keyword (or 'b' to go back): ", false);
        if (keyword.equals("BACK")) {
            System.out.println("Returning to menu.\n");
            return;
        }

        boolean found = false;
        System.out.println("\n------------------------- SEARCH RESULTS -------------------------");
        System.out.println("Keyword : " + keyword);
        System.out.println("------------------------------------------------------------------");

        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName) &&
                (t.getRemark().toLowerCase().contains(keyword.toLowerCase()) ||
                 (t.getResponse() != null && t.getResponse().toLowerCase().contains(keyword.toLowerCase())))) {
                ticketPrinter.printTicketDetails(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo tickets found matching: " + keyword + " assigned to you.");
        }
    }

    public void respondToTicket(Scanner input, String staffName) {
        int index = 1;
        ArrayList<Ticket> myOpenTickets = new ArrayList<>();

        System.out.println("\n------------------------ RESPOND TO TICKET -----------------------");

        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName) &&
                !t.isClosed() &&
                !t.isLinkedDuplicate()) {

                String remarkPreview = t.getRemark().length() > 45
                        ? t.getRemark().substring(0, 42) + "..."
                        : t.getRemark();

                System.out.println(index + ". Ticket #" + t.getTicketId()
                        + " [" + formatCategory(t.getCategory()) + "] " + remarkPreview);
                myOpenTickets.add(t);
                index++;
            }
        }
        System.out.println("------------------------------------------------------------------");

        if (myOpenTickets.isEmpty()) {
            System.out.println("\nNo open main tickets assigned to you.");
            return;
        }

        int choice = getValidatedInt(input, "\nSelect ticket (or 'b' to go back): ", 1, myOpenTickets.size());
        if (choice == -1) {
            System.out.println("Returning to menu.\n");
            return;
        }

        Ticket selectedTicket = myOpenTickets.get(choice - 1);

        String response;
        while (true) {
            response = getStringInput(input, "\nEnter response (or 'b' to cancel): ", false);
            if (response.equals("BACK")) {
                System.out.println("Response cancelled. Returning to menu.\n");
                return;
            }

            if (response.length() < 3) {
                System.out.println("Response too short. Please provide a meaningful response.\n");
            } else {
                break;
            }
        }

        Staff staff = findStaffByUsername(staffName);
        if (staff == null) {
            System.out.println("Staff not found.");
            return;
        }

        selectedTicket.addResponse(response);
        selectedTicket.assignStaff(staff);

        for (Ticket t : tickets) {
            if (t.isLinkedDuplicate() && t.getMainTicketId() == selectedTicket.getTicketId()) {
                t.addResponse(response);
                t.assignStaff(staff);
            }
        }

        System.out.println("\n---------------------- UPDATE TICKET STATUS ----------------------");
        System.out.println("  1. In Progress");
        System.out.println("  2. Resolved");
        System.out.println("  3. Back");
        System.out.println("------------------------------------------------------------------");

        int statusChoice = getValidatedInt(input, "\nEnter choice: ", 1, 3);
        if (statusChoice == -1 || statusChoice == 3) {
            System.out.println("\nResponse saved but status unchanged.");
            saveAllTickets();
            return;
        }

        if (statusChoice == 2) {
            selectedTicket.markResolved();

            for (Ticket t : tickets) {
                if (t.isLinkedDuplicate() && t.getMainTicketId() == selectedTicket.getTicketId()) {
                    t.markResolved();
                }
            }

            System.out.println("\nResponse saved and ticket marked as resolved!");
        } else {
            System.out.println("\nResponse saved and ticket kept as in progress!");
        }

        saveAllTickets();
    }

    public void staffPerformance(String staffName) {
        int totalTickets = 0;
        int closedTickets = 0;

        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName)) {
                totalTickets++;
                if (t.isClosed()) {
                    closedTickets++;
                }
            }
        }

        System.out.println();
        System.out.println("------------------- STAFF PERFORMANCE DASHBOARD ------------------");
        System.out.println("  Staff Name             : " + staffName);
        System.out.println("  Total Assigned Tickets : " + totalTickets);
        System.out.println("  Closed Tickets         : " + closedTickets);
        System.out.println("------------------------------------------------------------------");

        if (totalTickets == 0) {
            System.out.println("  Completion Rate        : 0%");
        } else {
            int completionRate = (closedTickets * 100 / totalTickets);
            System.out.println("  Completion Rate        : " + completionRate + "%");
        }
        System.out.println("------------------------------------------------------------------");
    }

    public void mergeDuplicateTickets(Scanner input, String staffName) {
        ticketMergeService.mergeDuplicateTickets(tickets, input, staffName);
        saveAllTickets();
    }

    // ======================== MANAGER METHODS ========================

    public void overallPerformance() {
        ticketReportService.overallPerformance();
    }

    public void monthlyReport() {
        ticketReportService.monthlyReport();
    }

    public void reassignTicket(Scanner input) {
        if (tickets.isEmpty()) {
            System.out.println("\nNo tickets available.");
            return;
        }

        System.out.println("\n------------------------- REASSIGN TICKET ------------------------");

        int index = 1;
        for (Ticket t : tickets) {
            String remarkPreview = t.getRemark().length() > 35
                    ? t.getRemark().substring(0, 32) + "..."
                    : t.getRemark();
            System.out.println(index + ". Ticket #" + t.getTicketId() + " [" + formatCategory(t.getCategory()) + "] " + remarkPreview);
            System.out.println("   Assigned to: " + formatAssignedStaff(t.getAssignedStaff()));
            index++;
        }
        System.out.println("------------------------------------------------------------------");

        int choice = getValidatedInt(input, "\nSelect ticket to reassign (or 'b' to go back): ", 1, tickets.size());
        if (choice == -1) {
            System.out.println("Returning to menu.\n");
            return;
        }

        String newStaffUsername = getStringInput(input, "\nEnter new staff username (or 'b' to cancel): ", false);
        if (newStaffUsername.equals("BACK")) {
            System.out.println("Reassignment cancelled. Returning to menu.\n");
            return;
        }

        Staff newStaff = findStaffByUsername(newStaffUsername);
        if (newStaff == null) {
            System.out.println("Invalid staff. Staff not found.");
            return;
        }

        Ticket selectedTicket = tickets.get(choice - 1);
        selectedTicket.assignStaff(newStaff);

        System.out.println("\n----------------------- TICKET REASSIGNED ------------------------");
        System.out.println("Ticket #" + selectedTicket.getTicketId() + " has been reassigned to " + newStaffUsername);
        System.out.println("------------------------------------------------------------------");
        saveAllTickets();
    }

    public void filterTickets(Scanner input) {
        System.out.println();
        System.out.println("------------------------- FILTER TICKETS -------------------------");
        System.out.println("  1. Filter by Category");
        System.out.println("  2. Filter by Status");
        System.out.println("  3. Filter by Assigned Staff");
        System.out.println("  4. Back");
        System.out.println("------------------------------------------------------------------");

        int choice = getValidatedInt(input, "\nEnter choice: ", 1, 4);
        if (choice == -1 || choice == 4) {
            System.out.println("Returning to menu.\n");
            return;
        }

        switch (choice) {
            case 1:
                filterAllByCategory(input);
                break;
            case 2:
                filterAllByStatus(input);
                break;
            case 3:
                filterAllByAssignedStaff(input);
                break;
        }
    }

    private void filterAllByCategory(Scanner input) {
        System.out.println("\n----------------------- FILTER BY CATEGORY -----------------------");
        System.out.println("  1. Food and Beverage");
        System.out.println("  2. Cleanliness");
        System.out.println("  3. Facilities");
        System.out.println("  4. Back");
        System.out.println("------------------------------------------------------------------");

        int categoryChoice = getValidatedInt(input, "\nEnter choice: ", 1, 4);
        if (categoryChoice == -1 || categoryChoice == 4) {
            System.out.println("Returning to menu.\n");
            return;
        }

        TicketCategory category;
        switch (categoryChoice) {
            case 1:
                category = TicketCategory.FOOD_AND_BEVERAGE;
                break;
            case 2:
                category = TicketCategory.CLEANLINESS;
                break;
            case 3:
                category = TicketCategory.FACILITIES;
                break;
            default:
                return;
        }

        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getCategory() == category) {
                ticketPrinter.printTicketDetails(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo tickets found for this category.");
        }
    }

    private void filterAllByStatus(Scanner input) {
        System.out.println("\n------------------------ FILTER BY STATUS ------------------------");
        System.out.println("  1. Open");
        System.out.println("  2. In Progress");
        System.out.println("  3. Resolved");
        System.out.println("  4. Closed");
        System.out.println("  5. Back");
        System.out.println("------------------------------------------------------------------");

        int statusChoice = getValidatedInt(input, "\nEnter choice: ", 1, 5);
        if (statusChoice == -1 || statusChoice == 5) {
            System.out.println("Returning to menu.\n");
            return;
        }

        TicketStatus status;
        switch (statusChoice) {
            case 1:
                status = TicketStatus.OPEN;
                break;
            case 2:
                status = TicketStatus.IN_PROGRESS;
                break;
            case 3:
                status = TicketStatus.RESOLVED;
                break;
            case 4:
                status = TicketStatus.CLOSED;
                break;
            default:
                return;
        }

        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getStatus() == status) {
                ticketPrinter.printTicketDetails(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo tickets found for this status.");
        }
    }

    private void filterAllByAssignedStaff(Scanner input) {
        String staffName = getStringInput(input, "\nEnter staff username (or 'b' to go back): ", false);
        if (staffName.equals("BACK")) {
            System.out.println("Returning to menu.\n");
            return;
        }

        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName)) {
                ticketPrinter.printTicketDetails(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo tickets found for this staff member.");
        }
    }

    private long calculateResponseMinutes(Ticket t) {
        if (t.getFirstResponseTime() == null) {
            return -1;
        }

        LocalDateTime submittedTime = LocalDateTime.parse(
            t.getDateSubmitted(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        return Duration.between(submittedTime, t.getFirstResponseTime()).toMinutes();
    }
}