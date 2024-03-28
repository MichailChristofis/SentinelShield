//The ServiceDesk class is dedicated to handling tickets
//and assigning them to the appropriate technician, using
//as its criteria both severity, and number of tickets assigned
//to each technician
public class ServiceDesk {
	private User techniciansLevel1[], techniciansLevel2[];
	private int assignedTickets[];
	//The ServiceDesk() method is the constructor for the
	//ServiceDesk class.
	public ServiceDesk() {
		techniciansLevel1=new User[3];
		techniciansLevel1[0]=new User("harrystyles@gmail.com", "Harry", "Styles", "(02) 1234 5678", "harryharry", true);
		techniciansLevel1[1]=new User("niallhorran@gmail.com", "Niall", "Horan", "(02) 5678 1234", "nialnial", true);
		techniciansLevel1[2]=new User("liampayne@gmail.com", "Liam", "Payne", "(02) 1234 5666", "limaliam", true);
		techniciansLevel2=new User[2];
		techniciansLevel2[0]=new User("louistomlison@gmail.com", "Louis", "Tomlison", "(02) 1234 1234", "louislouis", true);
		techniciansLevel2[1]=new User("zaynmalik@gmail.com", "Zayn", "Malik", "(02) 5678 5678", "zaynzayn", true);
		assignedTickets=new int[5];
		for(int i=0; i<5; i++) {
			assignedTickets[i]=0;
		}
	}
	//The AssignTicket() method, assigns a ticket to the
	//appropriate technician, based on its severity, and
	//the number of tickets already assigned to each
	//technician.
	public void AssignTicket(Ticket toAssign) {
		
	}
}
