package com.mycompany.oopassignment;

public class TicketFeedback {
    private RatingLevel rating;
    private String comment;

    public TicketFeedback(RatingLevel rating, String comment) {
        this.rating = rating;
        this.comment = (comment == null || comment.trim().isEmpty())
                ? "No feedback provided"
                : comment.trim();
    }

    public RatingLevel getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public int getNumericRating() {
        return rating.getValue();
    }

    @Override
    public String toString() {
        return rating.getLabel() + " (" + rating.getValue() + "/5): " + comment;
    }
}