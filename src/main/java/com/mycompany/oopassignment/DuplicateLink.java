package com.mycompany.oopassignment;

public class DuplicateLink {
    private int mainTicketId;
    private int duplicateTicketId;
    private String note;

    public DuplicateLink(int mainTicketId, int duplicateTicketId, String note) {
        this.mainTicketId = mainTicketId;
        this.duplicateTicketId = duplicateTicketId;
        this.note = note;
    }

    public int getMainTicketId() {
        return mainTicketId;
    }

    public int getDuplicateTicketId() {
        return duplicateTicketId;
    }

    public String getNote() {
        return note;
    }
}