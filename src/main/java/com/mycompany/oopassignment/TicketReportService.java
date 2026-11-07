package com.mycompany.oopassignment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketReportService {
    private ArrayList<Ticket> tickets;

    public TicketReportService(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void overallPerformance() {
        int total = tickets.size();
        int closed = 0;
        int open = 0;

        for (Ticket t : tickets) {
            if (t.isClosed()) {
                closed++;
            } else {
                open++;
            }
        }

        System.out.println();
        System.out.println("----------------- OVERALL PERFORMANCE DASHBOARD ------------------");
        System.out.println("  Total Tickets      : " + total);
        System.out.println("  Open Tickets       : " + open);
        System.out.println("  Closed Tickets     : " + closed);
        System.out.println("------------------------------------------------------------------");

        if (total > 0) {
            int completionRate = (closed * 100 / total);
            System.out.println("  Completion Rate    : " + completionRate + "%");
        } else {
            System.out.println("  Completion Rate    : No tickets available");
        }
        System.out.println("------------------------------------------------------------------");
    }

    public void monthlyReport() {
        int total = tickets.size();
        int open = 0;
        int inProgress = 0;
        int resolved = 0;
        int closed = 0;
        int totalRating = 0;
        int ratedCount = 0;
        int ticketsWithResponse = 0;
        long totalResponseMinutes = 0;
        int respondedTickets = 0;

        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> priorityCount = new HashMap<>();

        for (Ticket t : tickets) {
            if (t.getStatus() == TicketStatus.OPEN) {
                open++;
            } else if (t.getStatus() == TicketStatus.IN_PROGRESS) {
                inProgress++;
            } else if (t.getStatus() == TicketStatus.RESOLVED) {
                resolved++;
            } else if (t.getStatus() == TicketStatus.CLOSED) {
                closed++;
            }

            if (t.getResponse() != null && !t.getResponse().trim().isEmpty()) {
                ticketsWithResponse++;
            }

            long responseMinutes = calculateResponseMinutes(t);
            if (responseMinutes >= 0) {
                totalResponseMinutes += responseMinutes;
                respondedTickets++;
            }

            String category = formatCategory(t.getCategory());
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);

            String priority = formatPriority(t.getPriority());
            priorityCount.put(priority, priorityCount.getOrDefault(priority, 0) + 1);

            if (t.getRating() > 0) {
                totalRating += t.getRating();
                ratedCount++;
            }
        }

        System.out.println("==================================================================");
        System.out.println("------------------------- MONTHLY REPORT -------------------------");
        System.out.println("==================================================================");
        System.out.println("TICKET SUMMARY");
        System.out.println("------------------------------------------------------------------");
        System.out.println("  Total Tickets Submitted : " + total);
        System.out.println("  Open Tickets            : " + open);
        System.out.println("  In Progress Tickets     : " + inProgress);
        System.out.println("  Resolved Tickets        : " + resolved);
        System.out.println("  Closed Tickets          : " + closed);

        System.out.println();
        System.out.println("RESPONSE METRICS");
        System.out.println("------------------------------------------------------------------");
        if (total > 0) {
            double responseRate = (double) ticketsWithResponse / total * 100;
            System.out.println("  Response Rate           : " + String.format("%.1f", responseRate) + "% (" + ticketsWithResponse + "/" + total + " tickets)");
        } else {
            System.out.println("  Response Rate           : No tickets");
        }
        if (respondedTickets > 0) {
            double averageResponseTime = (double) totalResponseMinutes / respondedTickets;
            System.out.println("  Average Response Time   : " + String.format("%.2f", averageResponseTime) + " minutes");
        } else {
            System.out.println("  Average Response Time   : No responses yet");
        }

        System.out.println();
        System.out.println("DISTRIBUTION BY CATEGORY");
        System.out.println("------------------------------------------------------------------");
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            double percentage = total == 0 ? 0 : (double) entry.getValue() / total * 100;
            System.out.printf("  %-23s : %-1d (%.1f%%)%n", entry.getKey(), entry.getValue(), percentage);
        }

        System.out.println();
        System.out.println("DISTRIBUTION BY PRIORITY");
        System.out.println("------------------------------------------------------------------");
        for (Map.Entry<String, Integer> entry : priorityCount.entrySet()) {
            double percentage = total == 0 ? 0 : (double) entry.getValue() / total * 100;
            System.out.printf("  %-23s : %-1d (%.1f%%)%n", entry.getKey(), entry.getValue(), percentage);
        }

        System.out.println();
        System.out.println("CUSTOMER SATISFACTION");
        System.out.println("------------------------------------------------------------------");
        if (ratedCount > 0) {
            double averageRating = (double) totalRating / ratedCount;
            System.out.println("  Average Rating          : " + String.format("%.2f", averageRating) + " / 5");
            System.out.println("  Total Ratings Received  : " + ratedCount);
        } else {
            System.out.println("  Average Rating          : No ratings yet");
        }
        System.out.println("==================================================================");
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
}