// The Ticket class is used to store a ticket's severity,
// status, description, and the date of its completion.

import java.time.LocalDate;

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
        Archived
    }

    private String description;
    private Severity severity;
    private TicketStatus ticketStatus;
    private LocalDate dateCompleted;

    // This is a reference to the user that created this ticket so it can be
    // accessed without searching through the list of all staff.
    private User createdBy;

    // The Ticket() method is a constructor for the Ticket class.
    public Ticket(String description, Severity severity, TicketStatus ticketStatus, LocalDate dateCompleted) {
        this.description = description;
        this.severity = severity;
        this.ticketStatus = ticketStatus;
        this.dateCompleted = dateCompleted;
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
}
