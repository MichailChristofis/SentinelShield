// The ServiceDesk class is dedicated to handling tickets
// and assigning them to the appropriate technician, using
// as its criteria both severity, and number of tickets assigned
// to each technician

import java.util.Random;

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
    public void AssignTicket(Ticket toAssign) {

        // First, get the severity
        boolean isHighSeverity = (toAssign.getSeverity() == Ticket.Severity.High);
        
        // If the severity is high, assign to a level 2 technician.
        // Otherwise, assign to a level 1 technician
        User[] targetServiceDesk = isHighSeverity ? techniciansLevel2 : techniciansLevel1;
        
        // Now chose a specific user to assign to
        User targetUser = PickUserForTicket(targetServiceDesk);
        toAssign.AssignTicket(targetUser);
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
}