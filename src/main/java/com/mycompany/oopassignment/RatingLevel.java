package com.mycompany.oopassignment;

public enum RatingLevel {
    VERY_POOR(1, "Very Poor"),
    POOR(2, "Poor"),
    AVERAGE(3, "Average"),
    GOOD(4, "Good"),
    EXCELLENT(5, "Excellent");
    
    private final int value;
    private final String label;
    
    RatingLevel(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() { return value; }
    public String getLabel() { return label; }
    
    public static String getLabelForRating(int rating) {
        for (RatingLevel level : values()) {
            if (level.value == rating) {
                return level.label;
            }
        }
        return "Unknown";
    }
}