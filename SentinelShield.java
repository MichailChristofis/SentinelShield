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

public class SentinelShield {

    private Scanner console = new Scanner(System.in);
    private List<User> users = new ArrayList<>();
    private ServiceDesk serviceDesk;

    public SentinelShield(List<User> users, ServiceDesk serviceDesk) {
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

    // The loginScreen() method, handles the user interface
    // and communication with the user, for the login screen.
    private void loginScreen() {

    }

    // The forgotPasswordScreen() method, handles the user interface
    // and communication with the user, for the forgot password screen.
    private void forgotPasswordScreen() {

    }

    // The signupScreen() method, handles the user interface
    // and communication with the user, for the signup screen.
    private void signupScreen() {
        String email = getUserInput(
                "Email: ",
                s -> s.matches("^([a-zA-Z0-9]+\\.?)*[a-zA-Z0-9]@([a-zA-Z0-9]+\\.?)+[a-zA-Z0-9]$"),
                "Invalid email address.\nEmail: ");
        String firstName = getUserInput(
                "First Name: ",
                s -> !s.isEmpty(),
                "Enter a name.\nFirst Name: ");
        String lastName = getUserInput(
                "Last Name: ",
                s -> !s.isEmpty(),
                "Enter a name.\nLast Name: ");
        String phone = getUserInput(
                "Phone number: ",
                s -> s.matches("^\\+?\\d{2,4} ?\\d{2,4} ?(\\d{2,4}) ?(\\d{2,4})?$"),
                "Invalid phone number.\nPhone number: ");
        String password = getUserInput(
                "Password: ",
                s -> s.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{20,}$"),
                "Password must be at least 20 characters, and contain at least 1 uppercase, lowercase, and digit.");
        boolean confirmation = getUserInput(
                "Confirm adding a new user with the information you just added (Y/N): ",
                s -> s.toLowerCase().equals("y") || s.toLowerCase().equals("n"), "Please enter 'Y' or 'N': ")
                .toLowerCase().equals("y");
        if (confirmation) {
            users.add(new User(email, firstName, lastName, phone, password, false));
        }
    }

    // The signupScreen() method, handles the user interface
    // and communication with the user, for the signup screen.
    private void viewTicketsScreen() {

    }

    // The createTicketScreen() method, handles the user interface
    // and communication with the user, for the create ticket screen.
    private void createTicketScreen() {

    }

    // The completeTicketScreen() method, handles the user interface
    // and communication with the user, for the complete ticket screen.
    private void completeTicketScreen() {

    }

    // The checkForArchivedTickets() method, ....
    // Joshua all yours mate, not sure what you wanted here
    private void checkForArchivedTickets() {

    }

    // This method starts the SentinelShield program and runs the user menu.
    public void run() {
        signupScreen();
        // TODO - this basically needs to follow Paul's flowchart diagram.
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

        List<User> users = new ArrayList<>(Arrays.asList(techniciansLevel1));
        users.addAll(Arrays.asList(techniciansLevel2));

        SentinelShield system = new SentinelShield(users, new ServiceDesk(techniciansLevel1, techniciansLevel2));

        system.run();
    }
}
