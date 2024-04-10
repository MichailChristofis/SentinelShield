// The user class is used to store a user's details, including
// their email, first name, last name, phone number, password
// and whether or not they are a technician.

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email, firstName, lastName, phoneNumber, password;
    private boolean isTechnician;

    // This will be the 'created' tickets of a staff member OR the 'assigned'
    // tickets of a technician, as these two categories do not overlap.
    private ArrayList<Ticket> tickets = new ArrayList<>();

    // The User() method, is the constructor for the User class.
    public User(String email, String firstName, String lastName, String phoneNumber, String password,
            boolean isTechnician) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isTechnician = isTechnician;
    }

    // The getEmail() method is a getter method,
    // for the user's email.
    public String getEmail() {
        return this.email;
    }

    // The setEmail() method is a setter method,
    // for the user's email.
    public void setEmail(String email) {
        this.email = email;
    }

    // The getFirstName() method is a getter method,
    // for the user's first name.
    public String getFirstName() {
        return this.firstName;
    }

    // The setFirstName() method is a setter method,
    // for the user's first name.
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // The getLastName() method is a getter method,
    // for the user's last name.
    public String getLastName() {
        return this.lastName;
    }

    // The setLastName() method is a setter method,
    // for the user's last name.
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // The getPhoneNumber() method is a getter method,
    // for the user's phone number.
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    // The setPhoneNumber() method is a setter method,
    // for the user's phone number.
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // The getPassword() method is a getter method,
    // for the user's email.
    public String getPassword() {
        return this.password;
    }

    // The setPassword() method is a setter method,
    // for the user's email.
    public void setPassword(String password) {
        this.password = password;
    }

    // The getIsTechnician() method is a getter method,
    // for whether the user is a technician.
    public boolean getIsTechnician() {
        return this.isTechnician;
    }

    // The setIsTechnician() method is a getter method,
    // for whether the user is a technician.
    public void setIsTechnician(boolean isTechnician) {
        this.isTechnician = isTechnician;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public void assignTicket(Ticket ticket) {
        tickets.add(ticket);
    }
}
