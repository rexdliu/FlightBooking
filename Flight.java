import java.io.Serializable;
import java.util.*;

public class Flight implements Serializable {
    private String flightNumber;
    private Date date;
    private String time;
    private String departure;
    private String arrival;
    private int capacity;
    private ArrayList<Passenger> confirmedTickets;
    private Queue<Passenger> waitingList;

    public Flight(String flightNumber, Date date, String time, int capacity, String departure, String arrival) {
        this.flightNumber = flightNumber;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.departure = departure;
        this.arrival = arrival;
        this.confirmedTickets = new ArrayList<>();
        this.waitingList = new LinkedList<>();
    }

    // Getters for flight details
    public String getFlightNumber() {
        return flightNumber;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Passenger> getConfirmedTickets() {
        return confirmedTickets;
    }

    public Queue<Passenger> getWaitingList() {
        return waitingList;
    }

    // Method to book tickets for a passenger
    public Passenger bookTickets(Passenger passenger) {
        //use the stream method faster than using for loop
        int seatsAvailable = capacity - confirmedTickets.stream().mapToInt(Passenger::getSeatsConfirmed).sum();
        int seatsNeeded = passenger.getSeatsRequested();

        if (seatsAvailable >= seatsNeeded) {
            passenger.confirmSeats(seatsNeeded);
            confirmedTickets.add(passenger);
        } else {
            // Not enough seats, add to waiting list
            waitingList.add(passenger);
        }
        return passenger;
    }

    // Method to cancel a ticket
    public boolean cancelTicket(String bookingReference) {
        boolean found = false;
        Iterator<Passenger> iterator = confirmedTickets.iterator();
        while (iterator.hasNext()) {
            Passenger p = iterator.next();
            if (p.getBookingReference().equals(bookingReference)) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (found) {
            // Try to confirm passengers from waiting list
            Iterator<Passenger> waitIterator = waitingList.iterator();
            while (waitIterator.hasNext()) {
                Passenger waitingPassenger = waitIterator.next();
                int seatsAvailable = capacity - confirmedTickets.stream().mapToInt(Passenger::getSeatsConfirmed).sum();
                int seatsNeeded = waitingPassenger.getSeatsRequested();
                if (seatsAvailable >= seatsNeeded) {
                    waitingPassenger.confirmSeats(seatsNeeded);
                    confirmedTickets.add(waitingPassenger);
                    waitIterator.remove();
                } else {
                    break; // No more passengers can be accommodated
                }
            }
            return true;
        }
        // Search in waiting list
        Iterator<Passenger> waitIterator = waitingList.iterator();
        while (waitIterator.hasNext()) {
            Passenger p = waitIterator.next();
            if (p.getBookingReference().equals(bookingReference)) {
                waitIterator.remove();
                return true;
            }
        }
        return false; // Booking not found
    }

    // Method to find a passenger by booking reference
    public Passenger findPassenger(String bookingReference) {
        for (Passenger p : confirmedTickets) {
            if (p.getBookingReference().equals(bookingReference)) {
                return p;
            }
        }
        for (Passenger p : waitingList) {
            if (p.getBookingReference().equals(bookingReference)) {
                return p;
            }
        }
        return null; // Not found
    }
}
