package com.mycompany.oopassignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketResponse {
    private String message;
    private Staff responder;
    private LocalDateTime responseTime;
    private TicketStatus statusAfterResponse;

    public TicketResponse(String message, Staff responder, TicketStatus statusAfterResponse) {
        this.message = message;
        this.responder = responder;
        this.responseTime = LocalDateTime.now();
        this.statusAfterResponse = statusAfterResponse;
    }

    public String getMessage() {
        return message;
    }

    public Staff getResponder() {
        return responder;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public TicketStatus getStatusAfterResponse() {
        return statusAfterResponse;
    }

    public String getFormattedResponseTime() {
        return responseTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        String responderName = responder == null ? "Unknown Staff" : responder.getUsername();
        return "[" + getFormattedResponseTime() + "] "
                + responderName + " (" + statusAfterResponse + "): "
                + message;
    }
}