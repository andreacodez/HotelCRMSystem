package com.mycompany.oopassignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TicketMergeService {

    private static final int MAX_GROUP_SIZE = 5;

    public void mergeDuplicateTickets(ArrayList<Ticket> tickets, Scanner input, String staffName) {
        ArrayList<Ticket> myTickets = new ArrayList<>();

        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(staffName) &&
                !t.isClosed() &&
                !t.isLinkedDuplicate()) {
                myTickets.add(t);
            }
        }

        if (myTickets.size() < 2) {
            System.out.println("\nNot enough tickets to merge.");
            return;
        }

        System.out.println("\n------------ MERGE DUPLICATE TICKETS ------------");

        for (int i = 0; i < myTickets.size(); i++) {
            Ticket t = myTickets.get(i);
            System.out.println((i + 1) + ". Ticket #" + t.getTicketId()
                    + " | " + formatCategory(t.getCategory())
                    + " | " + t.getCustomerUsername());
        }

        int first = getValidatedInt(input, "Select MAIN ticket: ", 1, myTickets.size());
        if (first == -1) {
            System.out.println("Returning to menu.\n");
            return;
        }

        int second = getValidatedInt(input, "Select DUPLICATE ticket: ", 1, myTickets.size());
        if (second == -1) {
            System.out.println("Returning to menu.\n");
            return;
        }

        if (first == second) {
            System.out.println("Cannot merge the same ticket.");
            return;
        }

        Ticket selectedMain = myTickets.get(first - 1);
        Ticket selectedDup = myTickets.get(second - 1);

        if (selectedMain.getCategory() != selectedDup.getCategory()) {
            System.out.println("Only same category tickets can be linked.");
            return;
        }

        int rootMainId = selectedMain.getTicketId();

        Set<Integer> mainGroupIds = getGroupTicketIds(tickets, selectedMain.getTicketId());
        Set<Integer> dupGroupIds = getGroupTicketIds(tickets, selectedDup.getTicketId());

        Set<Integer> combinedIds = new HashSet<>();
        combinedIds.addAll(mainGroupIds);
        combinedIds.addAll(dupGroupIds);

        if (combinedIds.size() > MAX_GROUP_SIZE) {
            System.out.println("\nMerge not allowed.");
            System.out.println("Combining these tickets would exceed the maximum duplicate group size of " + MAX_GROUP_SIZE + ".");
            System.out.println("Please keep each duplicate group to 5 tickets or fewer.");
            return;
        }

        for (Ticket t : tickets) {
            if (combinedIds.contains(t.getTicketId())) {
                if (t.getTicketId() == rootMainId) {
                    clearAsMainTicket(t);
                } else {
                    t.linkToMainTicket(rootMainId);
                }
            }
        }

        Ticket rootMainTicket = findTicketById(tickets, rootMainId);
        if (rootMainTicket != null) {
            int duplicateCount = combinedIds.size() - 1;
            rootMainTicket.setDuplicateLink(
                    new DuplicateLink(
                            rootMainId,
                            -1,
                            "Main ticket for duplicate group (" + duplicateCount + " linked ticket"
                                    + (duplicateCount == 1 ? "" : "s") + ")"
                    )
            );
        }

        System.out.println("\nTickets linked successfully.");
        System.out.println("Ticket #" + rootMainId + " is now the main ticket for the merged duplicate group.");
        System.out.println("Current duplicate group size: " + combinedIds.size() + "/" + MAX_GROUP_SIZE);
    }

    private Set<Integer> getGroupTicketIds(ArrayList<Ticket> tickets, int selectedTicketId) {
        Set<Integer> ids = new HashSet<>();

        Ticket selectedTicket = findTicketById(tickets, selectedTicketId);
        if (selectedTicket == null) {
            return ids;
        }

        int rootMainId = selectedTicket.isLinkedDuplicate()
                ? selectedTicket.getMainTicketId()
                : selectedTicket.getTicketId();

        for (Ticket t : tickets) {
            if (t.getTicketId() == rootMainId) {
                ids.add(t.getTicketId());
            } else if (t.isLinkedDuplicate() && t.getMainTicketId() == rootMainId) {
                ids.add(t.getTicketId());
            }
        }

        return ids;
    }

    private Ticket findTicketById(ArrayList<Ticket> tickets, int ticketId) {
        for (Ticket t : tickets) {
            if (t.getTicketId() == ticketId) {
                return t;
            }
        }
        return null;
    }

    private void clearAsMainTicket(Ticket ticket) {
        ticket.setDuplicateLink(null);
        ticket.setMergeNote("");
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
}