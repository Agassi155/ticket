package com.test.model;

/**
 * Ticket progression enumeration
 */
public enum Status {
    IN_PROGRESS("In Progress"),
    FINISHED("Finished"),
    CANCELED("Canceled");

    private String displayName;

    /**
     * constructor for the enumeration ticket status
     *
     * @param displayName
     */
    Status(String displayName) {
        this.displayName = displayName;
    }

    /**
     * get displayed name off the status
     *
     * @return String name of the status
     */
    public String getDisplayName() {
        return displayName;
    }
}
