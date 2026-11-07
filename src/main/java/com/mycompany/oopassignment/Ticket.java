package com.mycompany.oopassignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Ticket {
    private static int nextId = 1;

    private int ticketId;
    private Customer customer;
    private TicketCategory category;
    private String remark;
    private PriorityLevel priority;
    private TicketStatus status;
    private String response;
    private ArrayList<TicketResponse> responses;
    private Staff assignedStaff;
    private String dateSubmitted;
    private TicketFeedback ticketFeedback;
    private LocalDateTime firstResponseTime;
    private boolean linkedDuplicate;
    private int mainTicketId;
    private String mergeNote;
    private DuplicateLink duplicateLink;

    public Ticket(Customer customer, TicketCategory category, String remark, PriorityLevel priority) {
        this.ticketId = nextId++;
        this.customer = customer;
        this.category = category;
        this.remark = remark;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
        this.response = "";
        this.responses = new ArrayList<>();
        this.assignedStaff = null;
        this.dateSubmitted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.ticketFeedback = null;
        this.firstResponseTime = null;
        this.linkedDuplicate = false;
        this.mainTicketId = -1;
        this.mergeNote = "";
        this.duplicateLink = null;
    }

    public Ticket(Customer customer, TicketCategory category, String remark) {
        this(customer, category, remark, PriorityLevel.MEDIUM);
    }

    public int getTicketId() {
        return ticketId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerUsername() {
        return customer.getUsername();
    }

    public TicketCategory getCategory() {
        return category;
    }

    public String getRemark() {
        return remark;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }

    public ArrayList<TicketResponse> getResponses() {
        return responses;
    }

    public ArrayList<String> getResponseHistory() {
        ArrayList<String> history = new ArrayList<>();
        for (TicketResponse responseItem : responses) {
            history.add(responseItem.toString());
        }
        return history;
    }

    public Staff getAssignedStaff() {
        return assignedStaff;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public int getRating() {
        return ticketFeedback == null ? 0 : ticketFeedback.getNumericRating();
    }

    public String getFeedback() {
        return ticketFeedback == null ? "" : ticketFeedback.getComment();
    }

    public TicketFeedback getTicketFeedback() {
        return ticketFeedback;
    }

    public LocalDateTime getFirstResponseTime() {
        return firstResponseTime;
    }

    public boolean isClosed() {
        return status == TicketStatus.CLOSED;
    }

    public boolean isLinkedDuplicate() {
        return linkedDuplicate;
    }

    public int getMainTicketId() {
        return duplicateLink != null ? duplicateLink.getMainTicketId() : mainTicketId;
    }

    public String getMergeNote() {
        return duplicateLink != null ? duplicateLink.getNote() : mergeNote;
    }

    public DuplicateLink getDuplicateLink() {
        return duplicateLink;
    }

    public void setDuplicateLink(DuplicateLink duplicateLink) {
        this.duplicateLink = duplicateLink;

        if (duplicateLink != null) {
            this.mainTicketId = duplicateLink.getMainTicketId();
            this.mergeNote = duplicateLink.getNote();
            this.linkedDuplicate = this.ticketId != duplicateLink.getMainTicketId();
        } else {
            this.mainTicketId = -1;
            this.mergeNote = "";
            this.linkedDuplicate = false;
        }
    }

    public void setMergeNote(String mergeNote) {
        this.mergeNote = mergeNote;
    }

    public void closeTicket() {
        status = TicketStatus.CLOSED;
    }

    public void markResolved() {
        status = TicketStatus.RESOLVED;
    }

    public void addResponse(String response) {
        addResponse(response, assignedStaff, TicketStatus.IN_PROGRESS);
    }

    public void addResponse(String response, Staff responder, TicketStatus statusAfterResponse) {
        if (responder == null) {
            responder = this.assignedStaff;
        }

        this.response = response;

        if (response != null && !response.trim().isEmpty()) {
            this.responses.add(new TicketResponse(response, responder, statusAfterResponse));
        }

        if (this.firstResponseTime == null) {
            this.firstResponseTime = LocalDateTime.now();
        }

        if (status == TicketStatus.OPEN) {
            status = TicketStatus.IN_PROGRESS;
        }
    }

    public void assignStaff(Staff staff) {
        this.assignedStaff = staff;
    }

    public void setFeedback(int rating, String feedback) {
        RatingLevel ratingLevel = null;

        switch (rating) {
            case 1:
                ratingLevel = RatingLevel.VERY_POOR;
                break;
            case 2:
                ratingLevel = RatingLevel.POOR;
                break;
            case 3:
                ratingLevel = RatingLevel.AVERAGE;
                break;
            case 4:
                ratingLevel = RatingLevel.GOOD;
                break;
            case 5:
                ratingLevel = RatingLevel.EXCELLENT;
                break;
            default:
                this.ticketFeedback = null;
                return;
        }

        this.ticketFeedback = new TicketFeedback(ratingLevel, feedback);
    }

    public void appendRemark(String extraRemark) {
        if (extraRemark != null && !extraRemark.trim().isEmpty()) {
            this.remark = this.remark + "\n[MERGED] " + extraRemark;
        }
    }

    public void mergeResponseHistory(ArrayList<TicketResponse> otherResponses) {
        if (otherResponses != null) {
            this.responses.addAll(otherResponses);
        }
    }

    public void linkToMainTicket(int mainTicketId) {
        this.linkedDuplicate = true;
        this.mainTicketId = mainTicketId;
        this.mergeNote = "Linked to main ticket #" + mainTicketId;
        this.duplicateLink = new DuplicateLink(mainTicketId, this.ticketId, this.mergeNote);
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public void setResponseForLoad(String response) {
        this.response = response;
    }

    public void setStatusForLoad(TicketStatus status) {
        this.status = status;
    }

    public void setResponsesForLoad(ArrayList<TicketResponse> responses) {
        this.responses = responses;
    }

    public static void setNextId(int nextId) {
        Ticket.nextId = nextId;
    }
}