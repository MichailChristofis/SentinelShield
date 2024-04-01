//SentinelShield is the main class of the project.
//It handles all communication with the user, and
//uses the data garnered from that communication
//appropriately, sending it to the relevant department
//and people

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.HashMap;
import java.util.Map;

public class SentinelShield {

    private Scanner console = new Scanner(System.in);
    private Map<String, User> users = new HashMap<>();
    private ServiceDesk serviceDesk;
    private User currentUser;

    public SentinelShield(Map<String, User> users, ServiceDesk serviceDesk) {
        this.users = users;
        this.serviceDesk = serviceDesk;
    }

    private String getUserInput(String prompt, Predicate<String> validationFunc, String invalidPrompt) {
        System.out.print(prompt);
        String input = console.nextLine();
        while (!validationFunc.test(input)) {
            System.out.println(invalidPrompt);
            input = console.nextLine();
        }
        return input;
    }

    private String getUserInput(String prompt) {
        return getUserInput(prompt, s -> true, "");
    }

    // Takes user input and attempts to authenticate them from the map of users
    // Returns a boolean, and updates private variable currentUser if login successful.
    private boolean validateLogin(String email, String password){
        // Login validation is false until checks pass
        boolean isValid = false;
        // Find the user email
        if (users.containsKey(email)){
            // Store the user object for easy access in the rest of the block
            User user = users.get(email);
            // Check if the passwords match
            if (user.getPassword().equals(password)){
                // Login is valid
                isValid = true;
                // Store the use for the session
                currentUser = user;
            }
        }
        return isValid;
    }

    // The loginScreen() method, handles the user interface
    // and communication with the user, for the login screen.
    private void loginScreen() {
        String email = "";
        String password = "";
        // Loop until the user logs in successfully
        do {
            email = getUserInput("Please enter your email: ");
            password = getUserInput("Please enter your password: ");
            if(!validateLogin(email, password)) {
            	System.out.println("Invalid credentials");
            }
        } while (!validateLogin(email, password));
        // Print login success message based on whether or not user is technician
        // TODO Update me when there are seperate dahsboard functions
        if (currentUser.getIsTechnician()){
            System.out.println("Technician Login successful!");
            viewTicketsScreen();
        } else{
            System.out.println("Staff Login successful!");
            viewTicketsScreen();
        }
        
    }

    // The forgotPasswordScreen() method, handles the user interface
    // and communication with the user, for the forgot password screen.
    private void forgotPasswordScreen() {

    }

    // The signupScreen() method, handles the user interface
    // and communication with the user, for the signup screen.
    private void signupScreen() {
        String email = getUserInput(
                "Please enter your Email: ",
                s -> s.matches("^([a-zA-Z0-9]+\\.?)*[a-zA-Z0-9]@([a-zA-Z0-9]+\\.?)+[a-zA-Z0-9]$"),
                "Invalid email address.\nEmail: ");
        // TODO: Decide if we need to make sure the email doesn't already exist and implement if so
        String firstName = getUserInput(
                "Please enter your First Name: ",
                s -> !s.isEmpty(),
                "Enter a name.\nFirst Name: ");
        String lastName = getUserInput(
                "Please enter your Last Name: ",
                s -> !s.isEmpty(),
                "Enter a name.\nLast Name: ");
        String phone = getUserInput(
                "Please enter your Phone number: ",
                s -> s.matches("^\\+?\\d{2,4} ?\\d{2,4} ?(\\d{2,4}) ?(\\d{2,4})?$"),
                "Invalid phone number.\nPhone number: ");
        String password = getUserInput(
                "Please enter your Password, it must be at least 20 characters, and contain at least one uppercase letter, one lowercase letter, and one number:",
                s -> s.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{20,}$"),
                "Password must be at least 20 characters, and contain at least 1 uppercase, lowercase, and digit.");
        boolean confirmation = getUserInput(
                "Confirm adding a new user with the information you just added (Y/N): ",
                s -> s.toLowerCase().equals("y") || s.toLowerCase().equals("n"), "Please enter 'Y' or 'N': ")
                .toLowerCase().equals("y");
        if (confirmation) {
            users.put(email, new User(email, firstName, lastName, phone, password, false));
        }
    }

    // The signupScreen() method, handles the user interface
    // and communication with the user, for the signup screen.
    private void viewTicketsScreen() {
        // TODO
        getUserInput("Welcome, " + currentUser.getFirstName() + ". The rest of this program doesn't exist yet, please press any key to exit.");
    }

    // The createTicketScreen() method, handles the user interface
    // and communication with the user, for the create ticket screen.
    private void createTicketScreen() {
        // TODO
    }

    // The completeTicketScreen() method, handles the user interface
    // and communication with the user, for the complete ticket screen.
    private void completeTicketScreen() {
        // TODO
    }

    // The checkForArchivedTickets() method, ....
    // Joshua all yours mate, not sure what you wanted here
    private void checkForArchivedTickets() {
        // TODO
    }

    // This method starts the SentinelShield program and runs the user menu.
    public void run() {
        // TEST CODE, REMOVE BEFORE SUBMISSION
        // users.put("test@test.com", new User("test@test.com", "test", "test", "0412345678", "Test123456789123456789", false));
        
        // TODO - this basically needs to follow Paul's flowchart diagram.
        // TODO - Potentially add some kind of main menu?
    	boolean conloop=true;
    	while(conloop) {
	    	String input="";
	    	do {
				System.out.println("Please select if you would like to sign up, or login:");
				System.out.println("(1) Signup");
				System.out.println("(2) Login");
				System.out.println("(3) Exit");
			    input = console.nextLine();
			    if(input.compareTo("1")!=0 && input.compareTo("2")!=0 && input.compareTo("3")!=0) {
			    	System.out.println("Invalid option");
			    }
	    	}while(input.compareTo("1")!=0 && input.compareTo("2")!=0 && input.compareTo("3")!=0);
	    	
	    	if(input.compareTo("1")==0) {
	    		signupScreen();
	    	}
	    	else if(input.compareTo("2")==0) {
	    		loginScreen();
	    	}
	    	else {
	    		System.out.println("Goodbye");
	    		conloop=false;
	    	}
    	}
    }

    public static void main(String[] args) {
        // Create the technician for this system.
        User[] techniciansLevel1, techniciansLevel2;
        techniciansLevel1 = new User[3];
        techniciansLevel1[0] = new User("harrystyles@gmail.com", "Harry", "Styles", "(02) 1234 5678", "harryharry",
                true);
        techniciansLevel1[1] = new User("niallhorran@gmail.com", "Niall", "Horan", "(02) 5678 1234", "nialnial", true);
        techniciansLevel1[2] = new User("liampayne@gmail.com", "Liam", "Payne", "(02) 1234 5666", "limaliam", true);
        techniciansLevel2 = new User[2];
        techniciansLevel2[0] = new User("louistomlison@gmail.com", "Louis", "Tomlison", "(02) 1234 1234", "louislouis",
                true);
        techniciansLevel2[1] = new User("zaynmalik@gmail.com", "Zayn", "Malik", "(02) 5678 5678", "zaynzayn", true);


        // Add the technicians to the user map
        Map<String, User> users = new HashMap<>();
        for (User technician : techniciansLevel1) {
            users.put(technician.getEmail(), technician);
        }
        for (User technician : techniciansLevel2) {
            users.put(technician.getEmail(), technician);
        }

        // Initialise SentinelShield
        SentinelShield system = new SentinelShield(users, new ServiceDesk(techniciansLevel1, techniciansLevel2));
        // Run the program
        system.run();
    }
}
