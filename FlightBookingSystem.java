import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class FlightBookingSystem {
    private static final int MAX_SEATS = 10; // Max seats per flight
    private Map<String, Flight> flights = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightBookingSystem().setupGUI());
    }

    private void setupGUI() {
        JFrame frame = new JFrame("Welcome To Flights4U!");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Welcome To Flights4U!", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Book Ticket", createBookTicketTab());
        tabbedPane.addTab("View Status", createViewStatusTab());
        tabbedPane.addTab("Flight Information", createFlightInfoTab());

        frame.add(tabbedPane, BorderLayout.CENTER);

        // Generate flights for the specified date range
        addFlightsFromTo("2024-11-19", "2025-11-25");

        frame.setVisible(true);

    }

    private void addFlightsFromTo(String startDate, String endDate) {
        // Parse the start and end date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        // Generate flights for every day in the range
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            String dateStr = date.format(formatter);
            // Add flights for each date
            flights.put(dateStr + "#AA101", new Flight("AA101", dateStr));
            flights.put(dateStr + "#BA202", new Flight("BA202", dateStr));

        }
    }

    private JPanel createBookTicketTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        JLabel passportLabel = new JLabel("Passport Number:");
        JTextField passportField = new JTextField(15);
        JLabel dateLabel = new JLabel("Flight Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(15);
        JLabel flightNumberLabel = new JLabel("Flight Number:");
        JTextField flightNumberField = new JTextField(15);
        JButton bookButton = new JButton("Book Ticket");
        JLabel statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.BLUE);

        bookButton.setEnabled(false); // Initially disabled

        // Enable button only when all fields are filled
        ActionListener enableButtonListener = e -> {
            bookButton.setEnabled(!nameField.getText().trim().isEmpty()
                    && !passportField.getText().trim().isEmpty()
                    && !dateField.getText().trim().isEmpty()
                    && !flightNumberField.getText().trim().isEmpty());
        };
        nameField.addActionListener(enableButtonListener);
        passportField.addActionListener(enableButtonListener);
        dateField.addActionListener(enableButtonListener);
        flightNumberField.addActionListener(enableButtonListener);

        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passportLabel, gbc);
        gbc.gridx = 1;
        panel.add(passportField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(flightNumberLabel, gbc);
        gbc.gridx = 1;
        panel.add(flightNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(bookButton, gbc);
        gbc.gridy = 5;
        panel.add(statusLabel, gbc);

        bookButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String passport = passportField.getText().trim();
            String date = dateField.getText().trim();
            String flightNumber = flightNumberField.getText().trim();

            if (name.isEmpty() || passport.isEmpty() || date.isEmpty() || flightNumber.isEmpty()) {
                statusLabel.setText("Please fill all fields.");
                return;
            }

            String flightKey = date + "#" + flightNumber;
            Flight flight = flights.get(flightKey);

            if (flight == null) {
                statusLabel.setText("Flight not available for this date.");
                return;
            }

            boolean booked = flight.bookTicket(new Passenger(name, passport));

            if (booked) {
                statusLabel.setText("Ticket booked successfully for Flight " + flightNumber);
            } else {
                statusLabel.setText("No seats available. You have been added to the waiting list.");
            }

            // Reset fields
            nameField.setText("");
            passportField.setText("");
            dateField.setText("");
            flightNumberField.setText("");
            bookButton.setEnabled(false);
        });

        // Edit Button
        JButton editButton = new JButton("Edit Ticket");
        editButton.addActionListener(e -> {
            String passport = passportField.getText().trim();
            String date = dateField.getText().trim();
            String flightNumber = flightNumberField.getText().trim();

            if (passport.isEmpty() || date.isEmpty() || flightNumber.isEmpty()) {
                statusLabel.setText("Please fill all fields.");
                return;
            }

            String flightKey = date + "#" + flightNumber;
            Flight flight = flights.get(flightKey);

            if (flight != null) {
                Passenger passenger = flight.getPassengerByPassport(passport);
                if (passenger != null) {
                    // Edit name
                    String newName = JOptionPane.showInputDialog("Enter new name for the passenger:", passenger.getName());
                    if (newName != null && !newName.trim().isEmpty()) {
                        passenger.setName(newName);
                    }

                    // Edit passport number
                    String newPassport = JOptionPane.showInputDialog("Enter new passport number for the passenger:", passenger.getPassportNumber());
                    if (newPassport != null && !newPassport.trim().isEmpty()) {
                        // Check for duplicate passport numbers
                        if (flight.getPassengerByPassport(newPassport) == null) {
                            passenger.setPassportNumber(newPassport);
                            statusLabel.setText("Passenger information updated.");
                        } else {
                            statusLabel.setText("New passport number is already in use.");
                        }
                    }
                } else {
                    statusLabel.setText("Passenger not found.");
                }
            } else {
                statusLabel.setText("Flight not found.");
            }
        });


        // Cancel Button
        JButton cancelButton = new JButton("Cancel Ticket");
        cancelButton.addActionListener(e -> {
            String passport = passportField.getText().trim();
            String date = dateField.getText().trim();
            String flightNumber = flightNumberField.getText().trim();

            if (passport.isEmpty() || date.isEmpty() || flightNumber.isEmpty()) {
                statusLabel.setText("Please fill all fields.");
                return;
            }

            String flightKey = date + "#" + flightNumber;
            Flight flight = flights.get(flightKey);

            if (flight != null) {
                boolean cancelled = flight.cancelTicket(passport);
                if (cancelled) {
                    statusLabel.setText("Ticket cancelled successfully.");
                    // Handle waiting list
                    if (!flight.getWaitingList().isEmpty()) {
                        Passenger nextPassenger = flight.getWaitingList().remove(0);
                        flight.bookTicket(nextPassenger);
                        statusLabel.setText("Waiting list passenger moved to confirmed.");
                    }
                } else {
                    statusLabel.setText("No ticket found for cancellation.");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(editButton, gbc);
        gbc.gridy = 7;
        panel.add(cancelButton, gbc);

        return panel;
    }

    private JPanel createViewStatusTab() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel passportLabel = new JLabel("Enter Passport Number:");
        JTextField passportField = new JTextField();
        JLabel dateLabel = new JLabel("Enter Flight Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();
        JLabel flightNumberLabel = new JLabel("Enter Flight Number:");
        JTextField flightNumberField = new JTextField();
        JButton viewButton = new JButton("View Status");
        JLabel statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.BLUE);

        viewButton.addActionListener(e -> {
            String passport = passportField.getText().trim();
            String date = dateField.getText().trim();
            String flightNumber = flightNumberField.getText().trim();

            if (passport.isEmpty() || date.isEmpty() || flightNumber.isEmpty()) {
                statusLabel.setText("Please fill all fields.");
                return;
            }

            String flightKey = date + "#" + flightNumber;
            Flight flight = flights.get(flightKey);

            if (flight != null) {
                if (flight.isPassengerBooked(passport)) {
                    statusLabel.setText("Ticket confirmed for Flight " + flightNumber);
                } else if (flight.isPassengerOnWaitingList(passport)) {
                    statusLabel.setText("You are on the waiting list for Flight " + flightNumber);
                } else {
                    statusLabel.setText("No ticket found for Flight " + flightNumber + ".");
                }
            } else {
                statusLabel.setText("No flight found for this date and flight number.");
            }
        });

        panel.add(passportLabel);
        panel.add(passportField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(flightNumberLabel);
        panel.add(flightNumberField);
        panel.add(viewButton);
        panel.add(statusLabel);

        return panel;
    }

    private JPanel createFlightInfoTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        JLabel dateLabel = new JLabel("Enter Flight Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(10);
        JButton searchButton = new JButton("Search Flights");

        searchPanel.add(dateLabel);
        searchPanel.add(dateField);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        searchButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            StringBuilder info = new StringBuilder();

            if (date.isEmpty()) {
                info.append("Please enter a valid date.");
            } else {
                // Fetch flights for the entered date
                List<Flight> flightsOnDate = new ArrayList<>();
                for (Flight flight : flights.values()) {
                    if (flight.getDate().equals(date)) {
                        flightsOnDate.add(flight);
                    }
                }

                if (flightsOnDate.isEmpty()) {
                    info.append("No flights available for this date.");
                } else {
                    for (Flight flight : flightsOnDate) {
                        info.append("Flight Number: ").append(flight.getFlightNumber()).append("\n");
                        info.append("Date: ").append(flight.getDate()).append("\n");
                        info.append("Available Seats: ").append(MAX_SEATS - flight.getConfirmedTickets().size()).append("\n\n");

                        // Display waiting list size
                        info.append("Waiting List: ").append(flight.getWaitingList().size()).append(" passengers\n\n");
                    }
                }
            }

            infoArea.setText(info.toString());
        });

        return panel;
    }


    private static class Flight {
        private String flightNumber;
        private String date;
        private List<Passenger> confirmedTickets = new ArrayList<>();
        private List<Passenger> waitingList = new ArrayList<>();

        public Flight(String flightNumber, String date) {
            this.flightNumber = flightNumber;
            this.date = date;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public String getDate() {
            return date;
        }

        public List<Passenger> getConfirmedTickets() {
            return confirmedTickets;
        }

        public List<Passenger> getWaitingList() {
            return waitingList;
        }

        public boolean bookTicket(Passenger passenger) {
            if (confirmedTickets.size() < MAX_SEATS) {
                confirmedTickets.add(passenger);
                return true;
            } else {
                waitingList.add(passenger);
                return false;
            }
        }

        public boolean isPassengerBooked(String passport) {
            return confirmedTickets.stream().anyMatch(p -> p.getPassportNumber().equals(passport));
        }

        public boolean isPassengerOnWaitingList(String passport) {
            return waitingList.stream().anyMatch(p -> p.getPassportNumber().equals(passport));
        }

        public boolean cancelTicket(String passport) {
            for (Iterator<Passenger> iterator = confirmedTickets.iterator(); iterator.hasNext(); ) {
                Passenger p = iterator.next();
                if (p.getPassportNumber().equals(passport)) {
                    iterator.remove();
                    return true;
                }
            }
            return false;
        }

        public Passenger getPassengerByPassport(String passport) {
            for (Passenger p : confirmedTickets) {
                if (p.getPassportNumber().equals(passport)) {
                    return p;
                }
            }
            return null;
        }
    }

    private static class Passenger {
        private String name;
        private String passportNumber;

        public Passenger(String name, String passportNumber) {
            this.name = name;
            this.passportNumber = passportNumber;
        }

        // Getter for name
        public String getName() {
            return name;
        }

        // Setter for name
        public void setName(String name) {
            this.name = name;
        }

        // Getter for passport number
        public String getPassportNumber() {
            return passportNumber;
        }

        // Setter for passport number
        public void setPassportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
        }
    }
}
