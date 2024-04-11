// SentinelShield is the main class of the project.
// It handles all communication with the user, and
// uses the data garnered from that communication
// appropriately, sending it to the relevant department
// and people.

import java.util.Scanner;
import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentinelShield {

    private Scanner console = new Scanner(System.in);
    private Map<String, User> users = new HashMap<>();
    private ServiceDesk serviceDesk;
    private User currentUser;

    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{20,}$";

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
    // Returns a boolean, and updates private variable currentUser if login
    // successful.
    private boolean validateLogin(String email, String password) {
        // Login validation is false until checks pass
        boolean isValid = false;
        // Find the user email
        if (users.containsKey(email)) {
            // Store the user object for easy access in the rest of the block
            User user = users.get(email);
            // Check if the passwords match
            if (user.getPassword().equals(password)) {
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
            if (!validateLogin(email, password)) {
                System.out.println("Invalid credentials");
            }
        } while (!validateLogin(email, password));
        // Print login success message based on whether or not user is technician
        // TODO Update me when there are separate dashboard functions
        if (currentUser.getIsTechnician()) {
            viewTechMenu();
        } else {
            viewStaffMenu();
        }

    }

    // The forgotPasswordScreen() method, handles the user interface
    // and communication with the user, for the forgot password screen.
    private void forgotPasswordScreen() {
        String usernameString = getUserInput("Please enter your email: ");
        if (users.containsKey(usernameString)) {
            User user = users.get(usernameString);
            String newPassword = getUserInput(
                    "Please enter your Password, it must be at least 20 characters, and contain at least one uppercase letter, one lowercase letter, and one number:",
                    s -> s.matches(PASSWORD_REGEX) && !user.getPassword().equals(s),
                    "Choose a different password that has at least 20 characters, and contain at least 1 uppercase, lowercase, and digit.");
            user.setPassword(newPassword);
            System.out.println("Your password is has been changed.");
        } else {
            System.out.println("User not found.");
        }
    }

    // The signupScreen() method, handles the user interface
    // and communication with the user, for the signup screen.
    private void signupScreen() {
        String email = getUserInput(
                "Please enter your Email: ",
                s -> s.matches("^([a-zA-Z0-9]+\\.?)*[a-zA-Z0-9]@([a-zA-Z0-9]+\\.?)+[a-zA-Z0-9]$"),
                "Invalid email address.\nEmail: ");
        // TODO: Decide if we need to make sure the email doesn't already exist and
        // implement if so
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
                s -> s.matches(PASSWORD_REGEX),
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
    private void viewStaffMenu() {
        boolean conloop = true;
        String menuString = "Please make a selection from the options below:\n"
                + "(1) Create a new ticket\n"
                + "(2) View your tickets\n"
                + "(3) Exit\n";
        String userinputeString = "";
        while (conloop) {
            // Refresh ticket status every time the menu is returned to
            serviceDesk.automaticallyRefreshTickets();

            userinputeString = getUserInput(menuString);
            if (userinputeString.equals("1")) {
                createTicketScreen();
            } else if (userinputeString.equals("2")) {
                viewTicketsScreen();
            } else if (userinputeString.equals("3")) {
                conloop = false;
            } else {
                System.out.println("Invalid option, please enter the number of your selection.");
            }
        }
    }

    private void viewTechMenu() {
        // Loop until the function returns
        while (true) {
            // Refresh ticket status every time the menu is returned to
            serviceDesk.automaticallyRefreshTickets();
            System.out.println("Welcome, " + currentUser.getFirstName() + ".");
            // We only need to display a technician's own tickets here
            int i = 1;
            List<Ticket> tickets = new ArrayList<>();
            for (Ticket t : currentUser.getTickets()) {
                tickets.add(t);
                System.out.printf("%-3d%-30s%-10s%-15s%n", i,
                        t.getAssignedTechnician().getFirstName() + " " + t.getAssignedTechnician().getLastName(),
                        t.getSeverity(),
                        t.getTicketStatus(), t.getDescription());
                i++;
            }
            String prompt = "Select a ticket (number) to view and/or edit, or type 'q' to quit.\n";
            String choice = getUserInput(prompt, s -> {
                if (s.toLowerCase().equals("q")) {
                    return true;
                }
                try {
                    Integer.parseInt(s);
                    return true;
                } catch (NumberFormatException _e) {
                    return false;
                }
            }, prompt);
            if (choice.toLowerCase().equals("q")) {
                return;
            } else {
                int ticketNo = Integer.parseInt(choice);
                techViewIndividualTicketScreen(tickets.get(ticketNo - 1));
            }
        }

    }

    private void techViewIndividualTicketScreen(Ticket ticket) {
        System.out.printf("Author: %s%n",
                ticket.getCreatedBy().getFirstName() + ticket.getCreatedBy().getLastName());
        System.out.printf("Severity: %s%n", ticket.getSeverity());
        System.out.printf("Status: %s%n", ticket.getTicketStatus());
        System.out.printf("Description: %s%n", ticket.getDescription());
        int choice = Integer
                .parseInt(getUserInput(
                        "Would you like to:\n(1) Update the status of this ticket\n(2) Update the severity of the ticket, or\n(3) Go back to the technician menu\n",
                        s -> s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4"),
                        "Please enter 1, 2, 3 or 4."));
        if (choice == 1) {
            updateTicketStatusMenu(ticket);
            return;
        }
        if (choice == 2) {
            updateTicketSeverityMenu(ticket);
        }
        if (choice == 3) {
            return;
        }
    }

    private void updateTicketStatusMenu(Ticket ticket) {
        // If the ticket isn't archived
        if (!ticket.getIsArchived()) {
            int choice = Integer
                    .parseInt(getUserInput(
                            "Would you like to set the ticket status to:\n(1) Open\n(2) Completed (Resolved), or\n(3) Completed (Unresolved)\n",
                            s -> s.equals("1") || s.equals("2") || s.equals("3"), "Please enter 1, 2, or 3."));
            switch (choice) {
                case 1:
                    ticket.setTicketStatus(Ticket.TicketStatus.Open);
                    break;
                case 2:
                    ticket.setTicketStatus(Ticket.TicketStatus.CompletedResolved);
                    break;
                case 3:
                    ticket.setTicketStatus(Ticket.TicketStatus.CompletedUnresolved);
                    break;
            }

        } else {
            System.out.println("Sorry, this ticket has been archived, and cannot be edited.");
        }
    }

    private void updateTicketSeverityMenu(Ticket ticket) {
        // If the ticket isn't archived
        if (!ticket.getIsArchived()) {
            int choice = Integer
                    .parseInt(getUserInput(
                            "Would you like to set the ticket severity to:\n(1) Low\n(2) Medium, or\n(3) High\n",
                            s -> s.equals("1") || s.equals("2") || s.equals("3"), "Please enter 1, 2, or 3."));
            switch (choice) {
                case 1:
                    ticket.setSeverity(Ticket.Severity.Low);
                    break;
                case 2:
                    ticket.setSeverity(Ticket.Severity.Medium);
                    break;
                case 3:
                    ticket.setSeverity(Ticket.Severity.High);
                    break;
            }

        } else {
            System.out.println("Sorry, this ticket has been archived, and cannot be edited.");
        }
    }

    // The createTicketScreen() method, handles the user interface
    // and communication with the user, for the create ticket screen.
    private void createTicketScreen() {
        String issue = getUserInput("Please input a description of the IT issue: ");
        String severity = "";
        do {
            System.out.println("Please input the severity of the issue:");
            System.out.println("(1) Low");
            System.out.println("(2) Medium");
            System.out.println("(3) High");
            severity = console.nextLine();
            if (severity.compareTo("1") != 0 && severity.compareTo("2") != 0 && severity.compareTo("3") != 0) {
                System.out.println("Invalid option, please enter the number of your selection.");
            }
        } while (severity.compareTo("1") != 0 && severity.compareTo("2") != 0 && severity.compareTo("3") != 0);
        Ticket createdTicket = new Ticket(issue, severity, currentUser);
        // Assign the ticket to the Service Desk, and therefore user
        serviceDesk.AssignTicket(createdTicket);
    }

    // The viewTicketsScreen() method, handles the user interface
    // and communication with the user, for the view tickets screen.
    private void viewTicketsScreen() {
        if (currentUser.getIsTechnician()) {
            // TODO: Display all technicians tickets and allow them to select one to view
        } else {
            List<Ticket> tickets = currentUser.getTickets();
            System.out.println("Your tickets:");
            for (int i = 0; i < tickets.size(); i++) {
                if (!tickets.get(i).getIsArchived()) {
                    System.out.println("Ticket description:");
                    System.out.println(tickets.get(i).getDescription());
                    System.out.println("Ticket severity:");
                    System.out.println(tickets.get(i).getSeverity());
                    System.out.println("Ticket status:");
                    System.out.println(tickets.get(i).getTicketStatus());
                    System.out.println("Date created:");
                    System.out.println(tickets.get(i).getDateCreated());
                }
            }
        }
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
        users.put("test@test.com", new User("test@test.com", "test", "test",
                "0412345678", "test", false));
        serviceDesk.AssignTicket(new Ticket("Test ticket", "1", users.get("test@test.com")));
        serviceDesk.AssignTicket(new Ticket("Test ticket 2", "3", users.get("test@test.com")));

        // Just because I'm a sadist doesn't mean I'm a masochist.
        users.put("ss", new User("ss", "SkyService", "SkyService",
                "0", "ss", false));

        // END TEST CODE
        boolean conloop = true;
        while (conloop) {
            // Refresh ticket status every time the menu is returned to
            serviceDesk.automaticallyRefreshTickets();
            String input = "";
            do {
                System.out.println("Please select if you would like to sign up, or login:");
                System.out.println("(1) Signup");
                System.out.println("(2) Login");
                System.out.println("(3) Forgot Password");
                System.out.println("(4) Exit");
                input = console.nextLine();
                if (input.compareTo("1") != 0 && input.compareTo("2") != 0 && input.compareTo("3") != 0
                        && input.compareTo("4") != 0) {
                    System.out.println("Invalid option, please enter the number of your selection.");
                }
            } while (input.compareTo("1") != 0 && input.compareTo("2") != 0 && input.compareTo("3") != 0
                    && input.compareTo("4") != 0);

            if (input.compareTo("1") == 0) {
                signupScreen();
            } else if (input.compareTo("2") == 0) {
                loginScreen();
            } else if (input.compareTo("3") == 0) {
                forgotPasswordScreen();
            } else {
                System.out.println("Goodbye");
                conloop = false;
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
        techniciansLevel2 = new User[3];
        techniciansLevel2[0] = new User("louistomlison@gmail.com", "Louis", "Tomlison", "(02) 1234 1234", "louislouis",
                true);
        techniciansLevel2[1] = new User("zaynmalik@gmail.com", "Zayn", "Malik", "(02) 5678 5678", "zaynzayn", true);
        techniciansLevel2[2] = new User("st", "SkyTech", "SkyTech", "0", "st", true);
        techniciansLevel2[2].getTickets().add(new Ticket("Test Description", "High", techniciansLevel2[2]));

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
