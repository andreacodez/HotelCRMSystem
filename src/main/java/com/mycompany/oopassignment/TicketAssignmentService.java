package com.mycompany.oopassignment;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TicketAssignmentService {
    private static final String COUNTER_FILE = "StaffCounter.txt";
    private static final int MAX_TICKETS_PER_STAFF = 50;

    private ArrayList<Staff> staffs;
    private int lastAssignedStaffIndex = -1;

    public TicketAssignmentService(ArrayList<Staff> staffs) {
        this.staffs = staffs;
        loadCounter();
    }

    public Staff getNextAvailableStaff(ArrayList<Ticket> tickets) {
        if (staffs == null || staffs.isEmpty()) {
            return null;
        }

        for (int i = 0; i < staffs.size(); i++) {
            lastAssignedStaffIndex = (lastAssignedStaffIndex + 1) % staffs.size();
            Staff potentialStaff = staffs.get(lastAssignedStaffIndex);

            if (getActiveTicketCount(tickets, potentialStaff.getUsername()) < MAX_TICKETS_PER_STAFF) {
                saveCounter();
                return potentialStaff;
            }
        }

        return null;
    }

    public int getActiveTicketCount(ArrayList<Ticket> tickets, String username) {
        int count = 0;
        for (Ticket t : tickets) {
            if (t.getAssignedStaff() != null &&
                t.getAssignedStaff().getUsername().equalsIgnoreCase(username) &&
                !t.isClosed()) {
                count++;
            }
        }
        return count;
    }

    private void saveCounter() {
        try (FileWriter writer = new FileWriter(COUNTER_FILE)) {
            writer.write(String.valueOf(lastAssignedStaffIndex));
        } catch (Exception e) {
            System.out.println("Error saving staff counter.");
        }
    }

    private void loadCounter() {
        try {
            File file = new File(COUNTER_FILE);
            if (!file.exists()) {
                lastAssignedStaffIndex = -1;
                return;
            }

            Scanner sc = new Scanner(file);
            if (sc.hasNextInt()) {
                lastAssignedStaffIndex = sc.nextInt();
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading staff counter.");
            lastAssignedStaffIndex = -1;
        }
    }
}