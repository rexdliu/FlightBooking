
import java.io.Serializable;

public class Passenger implements Serializable {

    private String username; // The user who made the booking
    private String name;
    private String passportNumber;
    private String bookingReference;
    private int seatsRequested;
    private int seatsConfirmed;

    public Passenger(String username, String name, String passportNumber, String bookingReference, int seatsRequested) {
        this.username = username;
        this.name = name;
        this.passportNumber = passportNumber;
        this.bookingReference = bookingReference;
        this.seatsRequested = seatsRequested;
        this.seatsConfirmed = 0;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public int getSeatsRequested() {
        return seatsRequested;
    }

    public int getSeatsConfirmed() {
        return seatsConfirmed;
    }

    public void confirmSeats(int seats) {
        this.seatsConfirmed = seats;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Passport: " + passportNumber
                + ", Booking Ref: " + bookingReference
                + ", Seats Confirmed: " + seatsConfirmed
                + ", Seats Requested: " + seatsRequested;
    }
}
