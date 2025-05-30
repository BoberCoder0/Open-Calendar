package com.example.opencalendar;

public class Goal {
    private String title;
    private String description;
    private boolean expanded;

    public Goal(String title, String description) {
        this.title = title;
        this.description = description;
        this.expanded = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}