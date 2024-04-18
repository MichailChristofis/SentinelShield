// The ServiceDesk class is dedicated to handling tickets
// and assigning them to the appropriate technician, using
// as its criteria both severity, and number of tickets assigned
// to each technician

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

// import Ticket;

public class ServiceDesk {
    private User[] techniciansLevel1, techniciansLevel2;

    // The ServiceDesk() method is the constructor for the
    // ServiceDesk class.
    public ServiceDesk(User[] techniciansLevel1, User[] techniciansLevel2) {
        this.techniciansLevel1 = techniciansLevel1;
        this.techniciansLevel2 = techniciansLevel2;
    }

    // The AssignTicket() method, assigns a ticket to the
    // appropriate technician, based on its severity, and
    // the number of tickets already assigned to each
    // technician.
    public void AssignTicket(Ticket toAssign, boolean reassign) {
        // Before we start, however, keep in mind that although fun, and learning
        // are the primary goals of all RMIT activities, we must give the user who
        // created the ticket a copy of the ticket.
        // Rather than doing this after the ticket is created from the menu system,
        // I will instead do it here, rendering "AssignTicket" something of a
        // notarization
        // system for tickets.
        // This is terrible, by the way, as if ticket ownership is changed, keeping
        // track of
        // past and future owners will be laborious, but it makes the features we
        // currently
        // need to implement rather trivial.
        // Incidentally, this also means that any user can call AssignTicket, and it
        // won't
        // necessarily be assigned to them, just to whoever was marked as the creator in
        // the ticket's properties.
        // OH WELL
        
        // If we're not reassigning, then no need to assign it to the creator again
        if (!reassign) {
            toAssign.getCreatedBy().assignTicket(toAssign);
        }

        // First, get the severity
        boolean isHighSeverity = (toAssign.getSeverity() == Ticket.Severity.High);

        // If the severity is high, assign to a level 2 technician.
        // Otherwise, assign to a level 1 technician
        User[] targetServiceDesk = isHighSeverity ? techniciansLevel2 : techniciansLevel1;

        // Now chose a specific user to assign to
        User targetUser = PickUserForTicket(targetServiceDesk);
        toAssign.AssignTicket(targetUser);
        // System.out.println("Assigning ticket to technician " + targetUser.getFirstName());
    }

    private User PickUserForTicket(User[] technicianList) {
        // The technician with the least number of tickets currently assigned
        // Initializing with first technician
        User topTechnician = technicianList[0];

        boolean allTechniciansHaveSameTicketCount = true;

        for (User technician : technicianList) {
            if (technician.getTickets().size() < topTechnician.getTickets().size()) {
                topTechnician = technician;
            }
            // It has been required that if every technician has an identical
            // number of tickets assigned, one of them be chosen at random.
            // This will aide with that process.
            if (technician.getTickets().size() != topTechnician.getTickets().size()) {
                allTechniciansHaveSameTicketCount = false;
            }
        }
        // We have now walked through all the users.
        // If they all have the same ticket count, pick one at random:
        if (allTechniciansHaveSameTicketCount) {
            Random generator = new Random();
            int index = generator.nextInt(technicianList.length);
            topTechnician = technicianList[index];

        }
        return topTechnician;
    }

    // Refresh ticket status to automatically archive after 24 hours, per Sprint 2
    // 9.1
    public void automaticallyRefreshTickets() {
        // The tickets are stored in the technician
        // So process everything for each technician
        processAllUserTickets(techniciansLevel1);
        processAllUserTickets(techniciansLevel2);
    }

    private void processAllUserTickets(User[] users) {
        // Now, for each user, walk through all their tickets
        for (User technician : users) {
            List<Ticket> tickets = technician.getTickets();
            // For every ticket
            for (Ticket ticket : tickets) {
                // Update the ticket
                ticket.refreshTicketStatus();
            }
        }
    }

    public ArrayList<Ticket> returnAllTickets() {
        ArrayList<Ticket> allTickets = new ArrayList<Ticket>();

        for (User technician : techniciansLevel1) {
            List<Ticket> tickets = technician.getTickets();
            // For every ticket
            for (Ticket ticket : tickets) {
                // add to the list
                allTickets.add(ticket);
            }
        }

        // Getting my LoC count up
        for (User technician : techniciansLevel2) {
            List<Ticket> tickets = technician.getTickets();
            // For every ticket
            for (Ticket ticket : tickets) {
                // add to the list
                allTickets.add(ticket);
            }
        }

        return allTickets;
    }
}