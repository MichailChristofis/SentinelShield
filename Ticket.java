// The Ticket class is used to store a ticket's severity,
// status, description, and the date of its completion.

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ticket {
    enum Severity {
        Low,
        Medium,
        High
    }

    enum TicketStatus {
        Open,
        CompletedResolved,
        CompletedUnresolved,
    }

    boolean archived = false;
    private String description;
    private Severity severity;
    private TicketStatus ticketStatus;
    private LocalDate dateCompleted;

    // This stores the users which created this ticket's email so it can be
    // accessed without searching through the list of all staff.
    private String createdBy;

    // The Ticket() method is a constructor for the Ticket class.
    public Ticket(String description, String severity, String ticketStatus, String dateCompleted, String creator) {
        this.description = description;
        this.severity = Severity.valueOf(severity);
        this.ticketStatus = TicketStatus.valueOf(ticketStatus);
        this.dateCompleted = LocalDate.parse(dateCompleted, DateTimeFormatter.ofPattern("yyyy-MMM-dd"));
        this.createdBy = creator;
    }

    // The getDescription() method is a getter method,
    // for the ticket's description.
    public String getDescription() {
        return this.description;
    }

    // The setDescription() method is a setter method,
    // for the ticket's description.
    public void setDescription(String description) {
        this.description = description;
    }

    // The getSeverity() method is a getter method,
    // for the ticket's severity.
    public Severity getSeverity() {
        return this.severity;
    }

    // The setSeverity() method is a setter method,
    // for the ticket's severity.
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    // The getTicketStatus() method is a getter method,
    // for the ticket's status.
    public TicketStatus getTicketStatus() {
        return this.ticketStatus;
    }

    // The setTicketStatus() method is a setter method,
    // for the ticket's status.
    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    // The getDateCompleted() method is a getter method,
    // for the ticket's date.
    public LocalDate getDateCompleted() {
        return this.dateCompleted;
    }

    // The setDateCompleted() method is a setter method,
    // for the ticket's date.
    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
    
    // The getIsArchived() method is a getter method,
    // for the ticket's archived status.
    public boolean getIsArchived() {
        return this.archived;
    }

    // The setIsArchived() method is a setter method,
    // for the ticket's archived status.
    public void setIsArchived(boolean isArchived) {
        this.archived = isArchived;
    }

}

