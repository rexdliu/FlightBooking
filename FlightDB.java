import java.io.*;
import java.util.*;

/*
This class is used to create database ,which stores the information of flights such as departure and arrival , date,flight_no
 and also the availability of the ticket.
 It should have the cancel ticket and save flights method
 Date,destination and departure, also the time of the flights. seat number, flight number(unique).
 The reason why we use Serializable because it allows
 */
public class FlightDB implements Serializable {
        private ArrayList<Flight> flights;
        public FlightDB() {
            this.flights = new ArrayList<>();
        }
        // Method to add a flight to the database
        public void addFlight(Flight flight) {
            flights.add(flight);
        }

        // Method to retrieve a flight by its flight number and date
        public Flight getFlight(String flightNumber, Date date) {
            for (Flight flight : flights) {
                if (flight.getFlightNumber().equals(flightNumber) && flight.getDate().equals(date)) {
                    return flight;
                }
            }
            return null;
        }

        // Method to retrieve all flights
        public ArrayList<Flight> getAllFlights() {
            return flights;
        }

        // Method to find a passenger across all flights sames as Select From By bookingReference
        public Passenger findPassenger(String bookingReference) {
            for (Flight flight : flights) {
                Passenger passenger = flight.findPassenger(bookingReference);
                if (passenger != null) {
                    return passenger;
                }
            }
            return null;
        }

        // Method to cancel a ticket across all flights
        public boolean cancelTicket(String bookingReference) {
            for (Flight flight : flights) {
                if (flight.cancelTicket(bookingReference)) {
                    return true;
                }
            }
            return false;
        }
    //  Generate flights for a given week
    public void generateFlights(Date startDate, String departure, String arrival) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            Date flightDate = calendar.getTime();
            // Generate flights per day with different flight numbers and times
            for (int j = 0; j < 10; j++) {
                String flightNumber = "FLIGHT" + (100 + random.nextInt(900));//900-include the Alpha value
                String time = String.format("%02d:%02d", random.nextInt(24), random.nextInt(60));
                Flight flight = new Flight(flightNumber, flightDate, time, 200, departure, arrival);
                this.addFlight(flight);
            }
            calendar.add(Calendar.DATE, 1);
        }
    }
        // Method to load the flight database from a file
        public static FlightDB loadData(String filename) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (FlightDB) ois.readObject();
            } catch (FileNotFoundException e) {
                // File not found, return a new FlightDB
                return new FlightDB();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading flight database: " + e.getMessage());
                return new FlightDB();
            }
        }

        // Method to save the flight database to a file
        public void saveData(String filename) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(this);
            } catch (IOException e) {
                System.out.println("Error saving flight database: " + e.getMessage());
            }
        }
    }



