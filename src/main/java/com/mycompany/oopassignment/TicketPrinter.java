package com.mycompany.oopassignment;

import java.util.ArrayList;

public class TicketPrinter {

    public void printCategoryGuidance(TicketCategory category) {
        switch (category) {
            case FOOD_AND_BEVERAGE:
                System.out.println("  FOOD AND BEVERAGE - Please include the following details:");
                System.out.println("  ------------------------------------------------------------");
                System.out.println("  - What did you order? (e.g., Room service breakfast/dinner)");
                System.out.println("  - What was the issue? (e.g., Cold food, Wrong order)");
                System.out.println("  - Any special dietary requirements not met?");
                System.out.println("  ------------------------------------------------------------");
                break;

            case CLEANLINESS:
                System.out.println("  CLEANLINESS - Please include the following details:");
                System.out.println("  ------------------------------------------------------------");
                System.out.println("  • Which area? (e.g., Room 305, Bathroom, Lobby, Hallway)");
                System.out.println("  • What did you observe? (e.g., Stains, Hair, Unpleasant odor)");
                System.out.println("  • Has housekeeping been notified already?");
                System.out.println("  ------------------------------------------------------------");
                break;

            case FACILITIES:
                System.out.println("  FACILITIES - Please include the following details:");
                System.out.println("  ------------------------------------------------------------");
                System.out.println("  • Which facility/equipment? (e.g., AC, TV, WiFi, Lights)");
                System.out.println("  • What is the problem? (e.g., Not working, Leaking, Broken)");
                System.out.println("  • Location? (e.g., Room 412, Gym, Pool area)");
                System.out.println("  • Any error messages or specific symptoms?");
                System.out.println("  ------------------------------------------------------------");
                break;
        }
    }

    public void printTicketDetails(Ticket t) {
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Ticket ID        : " + t.getTicketId());
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Date Submitted   : " + t.getDateSubmitted());
        System.out.println("  Customer         : " + t.getCustomerUsername());
        System.out.println("  Category         : " + formatCategory(t.getCategory()));
        System.out.println("  Priority         : " + formatPriority(t.getPriority()));
        System.out.println("  Status           : " + formatStatus(t.getStatus()));

        if (t.isLinkedDuplicate()) {
            System.out.println("  Merge Status     : Linked Duplicate");
            System.out.println("  Main Ticket ID   : #" + t.getMainTicketId());
        } else if (!isEmptyOrNull(t.getMergeNote())) {
            System.out.println("  Merge Note       : " + t.getMergeNote());
        }

        System.out.println("------------------------------------------------------------------");
        System.out.println("  Remark           : " + t.getRemark());
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Response History : ");
        ArrayList<String> history = t.getResponseHistory();
        if (history.isEmpty()) {
            System.out.println("  No responses yet");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + history.get(i));
            }
        }
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Assigned Staff   : " + formatAssignedStaff(t.getAssignedStaff()));
        System.out.println("  Rating           : " + (t.getRating() == 0 ? "Not rated yet" : t.getRating() + " / 5"));
        System.out.println("  Feedback         : " + (isEmptyOrNull(t.getFeedback()) ? "No feedback" : t.getFeedback()));
        System.out.println("------------------------------------------------------------------");
    }

    public void printCustomerTicketDetails(Ticket t) {
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Ticket ID        : " + t.getTicketId());
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Date Submitted   : " + t.getDateSubmitted());
        System.out.println("  Customer         : " + t.getCustomerUsername());
        System.out.println("  Category         : " + formatCategory(t.getCategory()));
        System.out.println("  Priority         : " + formatPriority(t.getPriority()));
        System.out.println("  Status           : " + formatStatus(t.getStatus()));
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Remark           : " + t.getRemark());
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Response History : ");
        ArrayList<String> history = t.getResponseHistory();
        if (history.isEmpty()) {
            System.out.println("  No responses yet");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + history.get(i));
            }
        }
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Assigned Staff   : " + formatAssignedStaff(t.getAssignedStaff()));
        System.out.println("  Rating           : " + (t.getRating() == 0 ? "Not rated yet" : t.getRating() + " / 5"));
        System.out.println("  Feedback         : " + (isEmptyOrNull(t.getFeedback()) ? "No feedback" : t.getFeedback()));
        System.out.println("------------------------------------------------------------------");
    }

    public void printTicketListHeader(String title) {
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        System.out.println("  " + title);
        System.out.println("------------------------------------------------------------------");
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

    private boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }
}