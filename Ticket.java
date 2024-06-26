// The Ticket class is used to store a ticket's severity,
// status, description, and the date of its completion.

import java.time.LocalDate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.time.format.DateTimeFormatter;

public class Ticket {
	// The Severity enum is used
	// to check on the severity of a Ticket
	// either being Low, Medium or High
    enum Severity {
        Low,
        Medium,
        High
    }
    
    // The TicketStatus enum is used to check
    // a ticket's status, which can be Open,
    // Completed Resolved or Completed
    // Unresolved.
    enum TicketStatus {
        Open,
        CompletedResolved,
        CompletedUnresolved,
    }

    boolean archived = false;
    private String description;
    private Severity severity;
    private TicketStatus ticketStatus;
    private LocalDate dateCompleted, dateCreated;

    private UUID uuid = UUID.randomUUID();
    // This stores the users which created this ticket's email so it can be
    // accessed without searching through the list of all staff.
    private User createdBy;

    // This stores the technical who is currently assigned to this ticket.
    private User assignedTechnician;

    // The Ticket() method is a constructor for the Ticket class.
    public Ticket(String description, String severity, User creator) {
        setValue(description, creator);
        if (severity.compareTo("1") == 0) {
            this.severity = Severity.Low;
        } else if (severity.compareTo("2") == 0) {
            this.severity = Severity.Medium;
        } else if (severity.compareTo("3") == 0) {
            this.severity = Severity.High;
        } else {
            this.severity = null;
        }
        this.ticketStatus = TicketStatus.Open;
    }

    // The setValue method, sets the values for the constructors
    // so as to avoid code repetition.
    private void setValue(String description, User creator) {
        this.description = description;
        // There is no date completed because we are just creating the ticket now,
        // rather than accepting strings,
        // Please accept a Date, forcing the caller to either conform the date
        // themselves, or pass null
        // this.dateCompleted = LocalDate.parse(dateCompleted,
        // DateTimeFormatter.ofPattern("yyyy-MMM-dd"));
        // Placeholder:
        this.dateCompleted = null;
        this.createdBy = creator;
        this.assignedTechnician = null;
        this.dateCreated = LocalDate.now();
    }

    // The setTicketStatus method sets the value of the TicketStatus
    // when an integer is used for the constructor
    private void setTicketStatus(int status) {
        if (status == 0) {
            this.ticketStatus = TicketStatus.valueOf("Open");
        } else if (status == 1) {
            this.ticketStatus = TicketStatus.valueOf("CompletedResolved");
        } else if (status == 2) {
            this.ticketStatus = TicketStatus.valueOf("CompletedUnresolved");
        }
    }

    // The setTicketSeverity method sets a ticket's severity
    // to whatever is required. It is a setter method.
    private void setTicketSeverity(int severity) {
        if (severity == 0) {
            this.setSeverity(Severity.valueOf("Low"));
        } else if (severity == 1) {
            this.setSeverity(Severity.valueOf("Medium"));
        } else if (severity == 2) {
            this.setSeverity(Severity.valueOf("High"));
        }
    }

    // The getCreatedBy() method is a getter method
    // used to retrieve the user that created the
    // particular ticket.
    public User getCreatedBy() {
        return this.createdBy;
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

    // The getTicketStatusString() method is a getter
    // method, retrieving the ticket's status as a
    // string.
    public String getTicketStatusString() {
        switch (this.ticketStatus) {
            case Open:
                return "Open";
            case CompletedResolved:
                return "Completed (Resolved)";
            case CompletedUnresolved:
                return "Completed (Unresolved)";
        }
        return "Error";
    }

    // The setTicketStatus() method is a setter method,
    // for the ticket's status.
    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
        refreshTicketStatus();
    }

    // We're not using this.dateCompleted because that may be managed and changed
    // externally
    private Instant timeMarkedCompleted;

    // Automatically archive ticket if marked complete over 24 hours ago
    public void refreshTicketStatus() {
        // Make sure the ticket hasn't been marked Open, otherwise ignore the call
        if (this.ticketStatus == TicketStatus.Open) {
            timeMarkedCompleted = null;
            return;
        } else if (this.ticketStatus == null) {
            timeMarkedCompleted = null;
            // Bail out
            return;
        }
        // We have a valid ticketStatus that isn't open, check if we already have a
        // valid time,
        // and if not, update it now:
        if (timeMarkedCompleted == null) {
            timeMarkedCompleted = Instant.now();
        }

        // Check if it's been more that 24 hours
        if (timeMarkedCompleted.isBefore(Instant.now().minus(24, ChronoUnit.HOURS))) {
            setIsArchived(true);
        }
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

    // The getDateCreated() method is a getter method,
    // for the ticket's date.
    public LocalDate getDateCreated() {
        return this.dateCreated;
    }
    
    // The getDateCreatedString() method is a getter method,
    // used to retrieve the date the ticket was created.
    public String getDateCreatedString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = this.dateCreated.format(formatter);
        return formattedDate;

    }

    // The setDateCreated() method is a setter method,
    // for the ticket's date.
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    // The getIsArchived() method is a getter method,
    // for the ticket's archived status.
    public boolean getIsArchived() {
        return this.archived;
    }

    public boolean getIsOpen() {
        boolean isOpen = this.ticketStatus == TicketStatus.Open;
        if (isOpen) {
            return true;
        } else {
            return false;
        }
    }

    // The setIsArchived() method is a setter method,
    // for the ticket's archived status.
    public void setIsArchived(boolean isArchived) {
        this.archived = isArchived;
    }

    // The getAssignedTechnician method is a getter method
    // for the assigned technician.
    public User getAssignedTechnician() {
        return this.assignedTechnician;
    }

    // The setAssignedTechnician method is a setter method
    // for the assigned technician
    public void setAssignedTechnician(User assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    // The AssignTicket method is used to assign a ticket
    // to a particular technician.
    public void AssignTicket(User assignTo) {
        this.assignedTechnician = assignTo;
        assignTo.assignTicket(this);
    }
    
    // The method getUUID() is a getter method
    // used to retrieve a tickets UUID.
    public UUID getUUID() {
        return this.uuid;
    }
}
