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
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SentinelShield {

    private Scanner console = new Scanner(System.in);
    private Map<String, User> users = new HashMap<>();
    private ServiceDesk serviceDesk;
    private User currentUser;
    private Date startDate, endDate;

    // The PASSWORD_REGEX is the ReGex string used
    // to verify, if the password provided meets the
    // criteria outlined, in the assignment
    // specification. It helps ensure that new passwords
    // are always correct as per the specification.
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{20,}$";

    // The SentinelShield() method is a constructor
    // used by the SentinelShield class so as
    // to construct and instantiate the variables
    // as necessary.
    public SentinelShield(Map<String, User> users, ServiceDesk serviceDesk) {
        this.users = users;
        this.serviceDesk = serviceDesk;
    }

    // The getUserInput() method is a method
    // used to get a user's input, validate
    // it based on the validation function
    // and thus ensure that the input is
    // appropriate. By doing so code repetition
    // is eliminated.
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
        email = getUserInput("Please enter your email: ");
        password = getUserInput("Please enter your password: ");
        if (!validateLogin(email, password)) {
            System.out.println("Invalid credentials");
        }
        if (!validateLogin(email, password)) {
            return;
        }
        // Print login success message based on whether or not user is technician
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
        String email = "";
        while (email.isEmpty() || users.containsKey(email)) {
            email = getUserInput(
                    "Please enter your Email: ",
                    s -> s.matches("^([a-zA-Z0-9]+\\.?)*[a-zA-Z0-9]@([a-zA-Z0-9]+\\.?)+[a-zA-Z0-9]$"),
                    "Invalid email address.\nAddress must conform to RFC 5322.\nPlease write your email address, starting with numbers and letters,\nthen the '@' symbol, then a domain name, including a '.', followed by the TLD.\nEmail: ");
            if (users.containsKey(email)) {
                System.out.println("There is already an account with that email. Try a different email.");
            }
        }
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
                "Invalid phone number. Please enter a valid Australian phone number, listing only the digits, without any other characters.\nPhone number: ");
        String password = getUserInput(
                "Please enter your Password, it must be at least 20 characters, and contain at least one uppercase letter, one lowercase letter, and one number:\n",
                s -> s.matches(PASSWORD_REGEX),
                "Password must be at least 20 characters, and contain at least 1 uppercase, lowercase, and digit.");
        users.put(email, new User(email, firstName, lastName, phone, password, false));
    }

    // The signupScreen() method, handles the user interface
    // and communication with the user, for the signup screen.
    private void viewStaffMenu() {
        boolean conloop = true;
        String menuString = "Please make a selection from the options below:\n"
                + "(1) Create a new ticket\n"
                + "(2) View your tickets\n"
                + "(3) Logout\n";
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

    // The daysBetween() method, calculates how many days
    // there are between two dates
    private long daysBetween(LocalDate date1, LocalDate date2) {
        return Math.abs(ChronoUnit.DAYS.between(date1, date2)) + 1;
    }

    // The printTicketDetails() method, prints a ticket's
    // details, as per the specification
    private void printTicketDetails(Ticket ticketToPrint) {
        String formatString = """
                ----------------------------
                Author:       %s
                Created:      %s
                Severity:     %s
                Status:       %s
                Technician:   %s
                Time Active:  %s days
                ----------------------------
                """;
        System.out.printf(formatString,
                ticketToPrint.getCreatedBy().getFirstName() + " " + ticketToPrint.getCreatedBy().getLastName(),
                ticketToPrint.getDateCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                ticketToPrint.getSeverity().name(),
                ticketToPrint.getTicketStatus().name(),
                ticketToPrint.getAssignedTechnician().getFirstName() + " "
                        + ticketToPrint.getAssignedTechnician().getLastName(),
                ticketToPrint.getTicketStatus().name().equals("Open") ? "N/A"
                        : daysBetween(ticketToPrint.getDateCreated(), ticketToPrint.getDateCompleted()));
    }

    // The viewTechMenu() method displays the tech's menu
    // and handles their selections
    private void viewTechMenu() {
        // Loop until the function returns
        System.out.println("\nWelcome, " + currentUser.getFirstName() + ".");
        while (true) {
            // Refresh ticket status every time the menu is returned to
            serviceDesk.automaticallyRefreshTickets();
            String choice = getUserInput(
                    "Do you want to view your assigned tickets (1), all closed or archived tickets (2), sort tickets by period (3) or logout (4)?\n",
                    s -> s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4"),
                    "Please enter 1, 2, 3 or 4.");
            if (choice.equals("4")) {
                // Signing out by returning from viewTechMenu()
                return;
            }
            if (choice.equals("1")) {
                // We only need to display a technician's own tickets here
                if (currentUser.getOpenTickets().size() == 0) {
                    System.out.println("\nYou don't have any open tickets currently assigned to you.\n");
                } else {
                    System.out.println("\nYour Assigned and Open Tickets: \n");
                    System.out.printf("%-3d%-30s%-10s%-15s%-15s%n", 0,
                            "Name",
                            "Severity",
                            "Status", "Description");

                    int i = 1;
                    for (Ticket t : currentUser.getOpenTickets()) {
                        System.out.printf("%-3d%-30s%-10s%-15s%-15s%n", i,
                                t.getAssignedTechnician().getFirstName() + " "
                                        + t.getAssignedTechnician().getLastName(),
                                t.getSeverity(),
                                t.getTicketStatus(), t.getDescription());
                        i++;
                    }
                    String prompt = "\nSelect a ticket (number) to view and/or edit, or type 'q' to go back.\n";
                    choice = getUserInput(prompt, s -> {
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
                    if (!choice.toLowerCase().equals("q")) {
                        int ticketNo = Integer.parseInt(choice);
                        if (ticketNo > currentUser.getOpenTickets().size() || ticketNo <= 0) {
                            System.out.println("Please choose a valid ticket number.");
                        } else {
                            techViewIndividualTicketScreen(currentUser.getOpenTickets().get(ticketNo - 1));
                        }
                    }
                }

            } else if (choice.equals("2")) {
                if (serviceDesk.returnAllClosedAndArchivedTickets().size() == 0) {
                    System.out.println("\nThere are currently no Closed or Archived Tickets.\n");
                } else {
                    System.out.println("\nAll Closed and Archived Tickets: ");
                    // Tickets are stored both in technicians, and in whatever the other ones are
                    // called.
                    // We could pick specifically just one of these lists, but instead, let's just
                    // track
                    // if a ticket's already printed, and if so, skip printing.

                    // NVM That's terrible let's just get all tickets

                    System.out.printf("%-3d%-30s%-10s%-23s%-15s%n", 0,
                            "Name",
                            "Severity",
                            "Status", "Description");

                    int i = 1;
                    for (Ticket t : serviceDesk.returnAllClosedAndArchivedTickets()) {
                        System.out.printf("%-3d%-30s%-10s%-23s%-15s%n", i,
                                t.getAssignedTechnician().getFirstName() + " "
                                        + t.getAssignedTechnician().getLastName(),
                                t.getSeverity(),
                                t.getTicketStatusString(), t.getDescription());
                        i++;
                    }
                    System.out.println("");
                    // Menu for archived tickets

                    String prompt = "\nSelect a ticket (number) to view and/or edit, or type 'q' to go back.\n";
                    choice = getUserInput(prompt, s -> {
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
                    if (!choice.toLowerCase().equals("q")) {
                        int ticketNo = Integer.parseInt(choice);
                        if (ticketNo > serviceDesk.returnAllClosedAndArchivedTickets().size() || ticketNo <= 0) {
                            System.out.println("Please choose a valid ticket number.");
                        }
                        if (serviceDesk.returnAllClosedAndArchivedTickets().get(ticketNo - 1).getIsArchived()) {
                            techViewArchivedTicketScreen(
                                    serviceDesk.returnAllClosedAndArchivedTickets().get(ticketNo - 1));
                            System.out.println("\nThis ticket is archived, and cannot be modified.\n");
                        } else {
                            techViewIndividualTicketScreen(
                                    serviceDesk.returnAllClosedAndArchivedTickets().get(ticketNo - 1));
                        }

                    }
                }
            } else if (choice.equals("3")) {
                String prompt = "Please select beginning date of filter (dd/mm/yyyy): ";
                String invalidPrompt = "Invalid date, please select beginning date of filter (dd/mm/yyyy): ";
                // Check if start date is well formatted
                getUserInput(prompt, s -> {
                    SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                    sdfrmt.setLenient(false);
                    try {
                        startDate = sdfrmt.parse(s);
                        return true;
                    } catch (ParseException e) {
                        return false;
                    }
                }, invalidPrompt);
                prompt = "Please select end date of filter (dd/mm/yyyy): ";
                invalidPrompt = "Invalid date, please select end date of filter (dd/mm/yyyy): ";
                // Check if end date is well formatted
                getUserInput(prompt, s -> {
                    SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                    sdfrmt.setLenient(false);
                    try {
                        endDate = sdfrmt.parse(s);
                        LocalDate sDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate eDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        if (sDate.isBefore(eDate) || sDate.equals(eDate)) {
                            return true;
                        } else {
                            return false;
                        }
                    } catch (ParseException e) {
                        return false;
                    }
                }, invalidPrompt);
                ArrayList<Ticket> ticketsSelected = new ArrayList<Ticket>();
                int countOpen = 0, countResolved = 0, countUnresolved = 0;
                for (Map.Entry<String, User> mapElement : users.entrySet()) {
                    User value = mapElement.getValue();
                    List<Ticket> userTickets = value.getTickets();
                    if (value.getIsTechnician() == true) {
                        for (Ticket userTicket : userTickets) {
                            LocalDate sDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            LocalDate eDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            if ((userTicket.getDateCreated().isAfter(sDate)
                                    && userTicket.getDateCreated().isBefore(eDate))
                                    || userTicket.getDateCreated().isEqual(sDate)
                                    || userTicket.getDateCreated().isEqual(eDate)) {
                                ticketsSelected.add(userTicket);
                                if (userTicket.getTicketStatus().name().equals("Open")) {
                                    countOpen++;
                                }
                                if (userTicket.getTicketStatus().name().equals("CompletedResolved")) {
                                    countResolved++;
                                }
                                if (userTicket.getTicketStatus().name().equals("CompletedUnresolved")) {
                                    countUnresolved++;
                                }
                            }
                        }
                    }
                }
                // Print results
                System.out.println("\n\nTickets submitted: " + ticketsSelected.size());
                System.out.println("Tickets open: " + countOpen);
                System.out.println("Tickets resolved: " + countResolved);
                System.out.println("Tickets unresolved: " + countUnresolved);
                for (int i = 0; i < ticketsSelected.size(); i++) {
                    printTicketDetails(ticketsSelected.get(i));
                }
            }
        }
    }

    // The techViewIndividualTicketScreen() method prints
    // the data of a particular ticket to the screen,
    // thus preventing code repetition.
    private void techViewIndividualTicketScreen(Ticket ticket) {
        System.out.printf("%nAuthor: %s%n",
                ticket.getCreatedBy().getFirstName() + " " + ticket.getCreatedBy().getLastName());
        System.out.printf("Severity: %s%n", ticket.getSeverity());
        System.out.printf("Status: %s%n", ticket.getTicketStatus());
        System.out.printf("Description: %s%n%n", ticket.getDescription());
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

    // The techViewArchivedTicketScreen() method prints
    // the data of a particular archived ticket to the
    // screen thus preventing code repetition.
    private void techViewArchivedTicketScreen(Ticket ticket) {
        System.out.printf("%nAuthor: %s%n",
                ticket.getCreatedBy().getFirstName() + " " + ticket.getCreatedBy().getLastName());
        System.out.printf("Severity: %s%n", ticket.getSeverity());
        System.out.printf("Status: %s%n", ticket.getTicketStatus());
        System.out.printf("Description: %s%n%n", ticket.getDescription());
    }

    // The updateTicketStatusMenu() method updates
    // a ticket's status to Open, CompletedResolved
    // or CompletedUnresolved, depending on the user's
    // selection.
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
                    ticket.setDateCompleted(LocalDate.now());
                    break;
                case 3:
                    ticket.setTicketStatus(Ticket.TicketStatus.CompletedUnresolved);
                    ticket.setDateCompleted(LocalDate.now());
                    break;
            }

        } else {
            System.out.println("Sorry, this ticket has been archived, and cannot be edited.");
        }
    }

    // The updateTicketSeverityMenu() method updates
    // a ticket's severity to Low Medium or High,
    // depending on the user's selection.
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
            ticket.getAssignedTechnician().forgetTicket(ticket);
            serviceDesk.AssignTicket(ticket, true);

        } else {
            System.out.println("Sorry, this ticket has been archived, and cannot be edited.");
        }
    }

    // The createTicketScreen() method, handles the user interface
    // and communication with the user, for the create ticket screen.
    private void createTicketScreen() {
        String issue = getUserInput("Please input a description of the IT issue: \n");
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
        serviceDesk.AssignTicket(createdTicket, false);
    }

    // The viewTicketsScreen() method, handles the user interface
    // and communication with the user, for the view tickets screen.
    private void viewTicketsScreen() {
        if (currentUser.getIsTechnician()) {
            // This is being handled elsewhere, viewTicketsScreen() is called exclusivly by
            // staff
            // We are a staff member:
        } else {
            List<Ticket> tickets = currentUser.getOpenTickets();
            if (tickets.size() == 0) {
                System.out.println("\nYou don't have any open tickets at the moment.\n");
            } else {
                System.out.println("\nYour Open Tickets:");
                System.out.printf("%-30s%-10s%-23s%-18s%-15s%n",
                        "Assigned to",
                        "Severity",
                        "Status",
                        "Date Created",
                        "Description");

                for (Ticket t : tickets) {
                    System.out.printf("%-30s%-10s%-23s%-18s%-15s%n",
                            t.getAssignedTechnician().getFirstName() + " "
                                    + t.getAssignedTechnician().getLastName(),
                            t.getSeverity(),
                            t.getTicketStatusString(),
                            t.getDateCreatedString(),
                            t.getDescription());
                }
                System.out.println("");

            }
        }
    }

    // This method starts the SentinelShield program and runs the user menu.
    public void run() {
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
                System.out.println("(4) Exit Program");
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
        techniciansLevel2 = new User[3];
        techniciansLevel1[0] = new User("harrystyles@gmail.com", "Harry", "Styles", "(02) 1234 5678", "harryharry",
                true);
        techniciansLevel1[1] = new User("niallhorran@gmail.com", "Niall", "Horan", "(02) 5678 1234", "nialnial", true);
        techniciansLevel1[2] = new User("liampayne@gmail.com", "Liam", "Payne", "(02) 1234 5666", "liamliam", true);
        techniciansLevel2[0] = new User("louistomlison@gmail.com", "Louis", "Tomlison", "(02) 1234 1234", "louislouis",
                true);
        techniciansLevel2[1] = new User("zaynmalik@gmail.com", "Zayn", "Malik", "(02) 5678 5678", "zaynzayn", true);
        techniciansLevel2[2] = new User("st", "SkyTech", "SkyTech", "0", "st", true);

        // Add the technicians to the user map
        Map<String, User> users = new HashMap<>();
        for (User technician : techniciansLevel1) {
            users.put(technician.getEmail(), technician);
        }
        for (User technician : techniciansLevel2) {
            users.put(technician.getEmail(), technician);
        }

        // Initialize SentinelShield
        SentinelShield system = new SentinelShield(users, new ServiceDesk(techniciansLevel1, techniciansLevel2));
        // Run the program
        system.run();
    }
}
