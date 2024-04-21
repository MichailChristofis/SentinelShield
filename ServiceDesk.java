// The ServiceDesk class is dedicated to handling tickets
// and assigning them to the appropriate technician, using
// as its criteria both severity, and number of tickets assigned
// to each technician

import java.util.Random;
import java.util.List;
import java.util.ArrayList;


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
        System.out.println("\nAssigning ticket to technician " + targetUser.getFirstName() + "\n");
    }

    // The PickUserForTicket method handles the assignment of a ticket
    // to a technician. It assigns the ticket to the technician with
    // the least number of tickets currently assigned to them, and if
    // multiple technicians have the same number of tickets
    // it assigns the ticket randomly between the technicians with 
    // equal number of tickets. This is done so as to ensure that technicians
    // are not being overloaded.
    private User PickUserForTicket(User[] technicianList) {
        // The technician with the least number of tickets currently assigned
        // Initializing with first technician
        User topTechnician = technicianList[0];
        int minTicketCount = topTechnician.getTickets().size();

        boolean allTechniciansHaveSameMinimumTicketCount = true;

        for (User technician : technicianList) {
            int ticketCount = technician.getTickets().size();

            // Lets think this through properly
            if (ticketCount < minTicketCount) {
                topTechnician = technician;
                minTicketCount = ticketCount;
                allTechniciansHaveSameMinimumTicketCount = false;
            } else if (ticketCount > minTicketCount) {
                // Also false, as not all techs have same min count
                allTechniciansHaveSameMinimumTicketCount = false;
            }            
        }
        // We have now walked through all the users.
        // If they all have the same ticket count, pick one at random:

        // It has been required that if every technician has an identical
        // number of tickets assigned, one of them be chosen at random.
        // This only occurs if they all have the same count, as if n technicians
        // are tied, then one of those n should be chosen.
        // Shuffling in this case could result in another technician being overloaded
        // with even more tickets unnecessarily.
        // This will aide with that process.
        if (allTechniciansHaveSameMinimumTicketCount) {
            Random generator = new Random();
            int index = generator.nextInt(technicianList.length);
            topTechnician = technicianList[index];
        }
        return topTechnician;
    }

    // The automaticallyRefreshTickets() method is used to automatically
    // refresh the ticket's status after a 24 hour period, setting it
    // to archived.
    public void automaticallyRefreshTickets() {
        // The tickets are stored in the technician
        // So process everything for each technician
        processAllUserTickets(techniciansLevel1);
        processAllUserTickets(techniciansLevel2);
    }

    // The processAllUserTickets method is used to process
    // all the user's tickets, automatically refreshing their
    // status to archived if needed.
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

    // The returnAllTickets() method is used to return
    // all the tickets assigned to a particular technician.
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
    
    // The returnAllClosedAndArchivedTickets() method is used to
    // return an arraylist of all closed and archived tickets. This
    // is usefule in multiple sections of the code later on.
    public ArrayList<Ticket> returnAllClosedAndArchivedTickets() {
        ArrayList<Ticket> closedAndArchivedTickets = this.returnAllTickets();
        closedAndArchivedTickets.removeIf(e -> e.getIsOpen());
        return closedAndArchivedTickets;
    }
}