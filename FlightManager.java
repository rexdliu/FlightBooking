import java.io.*;
import java.util.*;

public class FlightManager {
    private Map<Date, List<Flight>> flights = new HashMap<>();
    private static final String DATA_FILE = "flights.dat";

    // Method to generate flights for a given week
    public void generateFlights(Date startDate, String departure, String arrival) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            Date flightDate = calendar.getTime();
            List<Flight> flightsOnDate = flights.getOrDefault(flightDate, new ArrayList<>());
            // Generate 3 flights per day with different flight numbers
            for (int j = 0; j < 3; j++) {
                String flightNumber = "FLIGHT" + (100 + random.nextInt(900));
                Flight flight = new Flight(flightNumber, flightDate, 10, departure, arrival);
                flightsOnDate.add(flight);
            }
            flights.put(flightDate, flightsOnDate);
            calendar.add(Calendar.DATE, 1);
        }
    }

    // Method to display all flights
    public void displayFlights() {
        for (Map.Entry<Date, List<Flight>> entry : flights.entrySet()) {
            Date date = entry.getKey();
            List<Flight> flightsOnDate = entry.getValue();
            System.out.println("Date: " + Utils.formatDate(date));
            for (Flight flight : flightsOnDate) {
                System.out.println("  Flight Number: " + flight.getFlightNumber()
                        + ", Departure: " + flight.getDeparture()
                        + ", Arrival: " + flight.getArrival());
            }
        }
    }

    // Method to get flights by date
    public List<Flight> getFlightsByDate(Date date) {
        return flights.getOrDefault(date, new ArrayList<>());
    }

    // Method to find a passenger across all flights
    public Passenger findPassengerInAllFlights(String bookingReference) {
        for (List<Flight> flightsOnDate : flights.values()) {
            for (Flight flight : flightsOnDate) {
                Passenger passenger = flight.findPassenger(bookingReference);
                if (passenger != null) {
                    return passenger;
                }
            }
        }
        return null;
    }

    // Method to cancel a ticket across all flights
    public boolean cancelTicket(String bookingReference) {
        for (List<Flight> flightsOnDate : flights.values()) {
            for (Flight flight : flightsOnDate) {
                if (flight.cancelTicket(bookingReference)) {
                    return true;
                }
            }
        }
        return false;
    }


}