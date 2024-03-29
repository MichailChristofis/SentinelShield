//The ServiceDesk class is dedicated to handling tickets
//and assigning them to the appropriate technician, using
//as its criteria both severity, and number of tickets assigned
//to each technician
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

    }
}
