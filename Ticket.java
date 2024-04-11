// The Ticket class is used to store a ticket's severity,
// status, description, and the date of its completion.

import java.time.LocalDate;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
    private LocalDate dateCompleted, dateCreated;

    // This stores the users which created this ticket's email so it can be
    // accessed without searching through the list of all staff.
    private User createdBy;

    // This stores the technical who is currently assigned to this ticket.
    private User assignedTechnician;

    // The Ticket() method is a constructor for the Ticket class.
    public Ticket(String description, String severity, String ticketStatus, User creator) {
        setValue(description, creator);
        this.severity = Severity.valueOf(severity);
        this.ticketStatus = TicketStatus.valueOf(ticketStatus);
    }

    // The Ticket() method is a constructor for the Ticket class.
    public Ticket(String description, String severity, int ticketStatus, User creator) {
        setValue(description, creator);
    	this.severity = Severity.valueOf(severity);
    	setTicketStatus(ticketStatus);
    }
    
    // The Ticket() method is a constructor for the Ticket class.
    public Ticket(String description, int severity, int ticketStatus, User creator) {
        setValue(description, creator);
        setSeverity(severity);
    	setTicketStatus(ticketStatus);
    }
    
    // The Ticket() method is a constructor for the Ticket class.
    public Ticket(String description, int severity, String ticketStatus, User creator) {
        setValue(description, creator);
        setSeverity(severity);
        this.ticketStatus = TicketStatus.valueOf(ticketStatus);
    }
    
    //The setValue method, sets the values for the constructors
    //so as to avoid code repetition.
    private void setValue(String description, User creator) {
    	this.description = description;
        // There is no date completed because we are just creating the ticket now, rather than accepting strings,
        // Please accept a Date, forcing the caller to either conform the date themselves, or pass null
        // this.dateCompleted = LocalDate.parse(dateCompleted, DateTimeFormatter.ofPattern("yyyy-MMM-dd"));
        // Placeholder:
        this.dateCompleted = null;
        this.createdBy = creator;
        this.assignedTechnician = null;
        this.dateCreated=LocalDate.now();
    }
    
    //The setTicketStatus method sets the value of the TicketStatus
    //when an integer is used for the constructor
    private void setTicketStatus(int severity) {
        if(severity==0) {
            this.ticketStatus = TicketStatus.valueOf("Low");
        }
        else if(severity==1) {
            this.ticketStatus = TicketStatus.valueOf("Medium");
        }
        else if(severity==2) {
            this.ticketStatus = TicketStatus.valueOf("High");
        }
    }
    
    //The setSeverity method sets the value of Severity
    //when an integer is used for the constructor
    private void setSeverity(int ticketStatus) {
        if(ticketStatus==0) {
            this.ticketStatus = TicketStatus.valueOf("Open");
        }
        else if(ticketStatus==1) {
            this.ticketStatus = TicketStatus.valueOf("CompletedResolved");
        }
        else if(ticketStatus==2) {
            this.ticketStatus = TicketStatus.valueOf("CompletedUnresolved");
        }
    }
    
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
    // Todo: Once this is called from the menu, execute relevant code:
    // It could be placed in the setter however Ticket holds no reference to ServiceDesk
    /*
    // Check if we need to reassign
    if ((this.severity == Severity.High) && (severity != Severity.High) || (this.severity != Severity.High) && (severity == Severity.High)) {
        serviceDesk.AssignTicket(createdTicket);        
    }
    */

    // The getTicketStatus() method is a getter method,
    // for the ticket's status.
    public TicketStatus getTicketStatus() {
        return this.ticketStatus;
    }

    // The setTicketStatus() method is a setter method,
    // for the ticket's status.
    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;        
        refreshTicketStatus();
    }

    // We're not using this.dateCompleted because that may be managed and changed externally
    private Instant timeMarkedCompleted;
    // Automatically archive ticket if marked complete over 24 hours ago
    public void refreshTicketStatus(){
        // Make sure the ticket hasn't been marked Open, otherwise ignore the call
        if (this.ticketStatus == TicketStatus.Open) {
            timeMarkedCompleted = null;
            return;
        } else if (this.ticketStatus == null) {
            timeMarkedCompleted = null;
            // Bail out
            return;
        }
        // We have a valid ticketStatus that isn't open, check if we already have a valid time,
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

    // The setDateCreated() method is a setter method,
    // for the ticket's date.
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCompleted = dateCreated;
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
    
    //The getAssignedTechnician method is a getter method
    //for the assigned technician.
    public User getAssignedTechnician() {
    	return this.assignedTechnician;
    }
    
    //The setAssignedTechnician method is a setter method
    //for the assigned technician
    public void setAssignedTechnician(User assignedTechnician) {
    	this.assignedTechnician=assignedTechnician;
    }
    
    //The AssignTicket method is used to assign a ticket
    //to a particular technician.
    public void AssignTicket(User assignTo) {
        this.assignedTechnician = assignTo;
        assignTo.assignTicket(this);
    }

}
