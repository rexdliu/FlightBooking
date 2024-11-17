/*
This class is for searching the Flights in Any week,display the details of the Flights.
Can generate the details of the flight at this time period, and load the flight details from a database or a file.
The passenger can get the details of the flight by date.
*/
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Flight_Info implements Serializable {
    private Flight flight;

    public Flight_Info(Flight flight) {
        this.flight = flight;
    }

    public void displayFlightInfo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Flight Number: " + flight.getFlightNumber());
        System.out.println("Date: " + dateFormat.format(flight.getDate()));
        System.out.println("Time: " + flight.getTime());
        System.out.println("Departure: " + flight.getDeparture());
        System.out.println("Arrival: " + flight.getArrival());
        System.out.println("Capacity: " + flight.getCapacity());
        System.out.println("Seats Confirmed: " + flight.getConfirmedTickets().stream().mapToInt(Passenger::getSeatsConfirmed).sum());
        System.out.println("Seats Available: " + (flight.getCapacity() - flight.getConfirmedTickets().stream().mapToInt(Passenger::getSeatsConfirmed).sum()));
        System.out.println("Passengers on Waiting List: " + flight.getWaitingList().size());
    }
}

