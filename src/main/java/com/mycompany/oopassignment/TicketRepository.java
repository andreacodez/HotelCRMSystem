package com.mycompany.oopassignment;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TicketRepository {
    private static final String FILE_NAME = "Tickets.txt";

    public void saveTicket(Ticket t) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            String responses = buildResponseStorage(t);

            writer.write(
                t.getTicketId() + "|" +
                t.getCustomerUsername().toUpperCase() + "|" +
                t.getCategory() + "|" +
                t.getRemark() + "|" +
                t.getPriority() + "|" +
                t.getStatus() + "|" +
                (t.getAssignedStaff() == null ? "none" : t.getAssignedStaff().getUsername().toUpperCase()) + "|" +
                t.getDateSubmitted() + "|" +
                (t.getRating() == 0 ? "0" : t.getRating()) + "|" +
                (isEmptyOrNull(t.getFeedback()) ? "No feedback" : t.getFeedback()) + "|" +
                t.isLinkedDuplicate() + "|" +
                t.getMainTicketId() + "|" +
                responses + "\n"
            );
        } catch (Exception e) {
            System.out.println("Error saving ticket.");
        }
    }

    public void saveAllTickets(ArrayList<Ticket> tickets) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Ticket t : tickets) {
                String responses = buildResponseStorage(t);

                writer.write(
                    t.getTicketId() + "|" +
                    t.getCustomerUsername().toUpperCase() + "|" +
                    t.getCategory() + "|" +
                    t.getRemark() + "|" +
                    t.getPriority() + "|" +
                    t.getStatus() + "|" +
                    (t.getAssignedStaff() == null ? "none" : t.getAssignedStaff().getUsername().toUpperCase()) + "|" +
                    t.getDateSubmitted() + "|" +
                    (t.getRating() == 0 ? "0" : t.getRating()) + "|" +
                    (isEmptyOrNull(t.getFeedback()) ? "No feedback" : t.getFeedback()) + "|" +
                    t.isLinkedDuplicate() + "|" +
                    t.getMainTicketId() + "|" +
                    responses + "\n"
                );
            }
        } catch (Exception e) {
            System.out.println("Error saving tickets.");
        }
    }

    public void loadTickets(ArrayList<Ticket> tickets, ArrayList<Customer> customers, ArrayList<Staff> staffs) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return;

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|", -1);
                if (data.length < 13) continue;

                String username = data[1];

                Customer customer = null;
                for (Customer c : customers) {
                    if (c.getUsername().equalsIgnoreCase(username)) {
                        customer = c;
                        break;
                    }
                }

                if (customer == null) {
                    continue;
                }

                TicketCategory category = TicketCategory.valueOf(data[2]);
                String remark = data[3];
                PriorityLevel priority = PriorityLevel.valueOf(data[4]);
                TicketStatus status = TicketStatus.valueOf(data[5]);

                Staff staff = null;
                if (!data[6].equalsIgnoreCase("none")) {
                    for (Staff s : staffs) {
                        if (s.getUsername().equalsIgnoreCase(data[6])) {
                            staff = s;
                            break;
                        }
                    }
                }

                int rating = 0;
                if (!data[8].equals("0")) {
                    rating = Integer.parseInt(data[8]);
                }

                String feedback = data[9];
                if (feedback.equals("No feedback")) {
                    feedback = "";
                }

                boolean linked = Boolean.parseBoolean(data[10]);
                int mainId = Integer.parseInt(data[11]);

                String[] responses = !data[12].isEmpty() ? data[12].split(";") : new String[0];

                Ticket t = new Ticket(customer, category, remark, priority);
                t.assignStaff(staff);

                String lastResponseMessage = "";
                for (String r : responses) {
                    if (!r.isEmpty()) {
                        String cleanMessage = r.replace("<semi>", ";");
                        t.addResponse(cleanMessage, staff, TicketStatus.IN_PROGRESS);
                        lastResponseMessage = cleanMessage;
                    }
                }

                if (!lastResponseMessage.isEmpty()) {
                    t.setResponseForLoad(lastResponseMessage);
                }

                if (status == TicketStatus.OPEN) {
                    t.setStatusForLoad(TicketStatus.OPEN);
                } else if (status == TicketStatus.IN_PROGRESS) {
                    t.setStatusForLoad(TicketStatus.IN_PROGRESS);
                } else if (status == TicketStatus.RESOLVED) {
                    t.setStatusForLoad(TicketStatus.RESOLVED);
                } else if (status == TicketStatus.CLOSED) {
                    t.setStatusForLoad(TicketStatus.CLOSED);
                }

                if (rating > 0) {
                    t.setFeedback(rating, feedback);
                }

                if (linked) {
                    t.linkToMainTicket(mainId);
                }

                tickets.add(t);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading tickets: " + e.getMessage());
        }
    }

    private String buildResponseStorage(Ticket t) {
        StringBuilder responsesBuilder = new StringBuilder();
        for (TicketResponse r : t.getResponses()) {
            responsesBuilder.append(r.getMessage().replace(";", "<semi>")).append(";");
        }
        return responsesBuilder.toString();
    }

    private boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }
}