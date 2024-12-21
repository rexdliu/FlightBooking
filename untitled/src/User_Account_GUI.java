import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class User_Account_GUI {
    public static String loggedInUser;
    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, List<Ticket>> bookings = new HashMap<>(); // Track user bookings
    private static Queue<Ticket> waitingList = new LinkedList<>(); // Queue for waiting list

    static {
        loadUsers();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(User_Account_GUI::createAndShowLoginPage);
    }

    private static void createAndShowLoginPage() {
        JFrame frame = new JFrame("User Account Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setResizable(false);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                ImageIcon icon = new ImageIcon("C:\\Users\\User\\Desktop\\Database_ FTBS\\image01.jpg");
                Image image = icon.getImage();
                float transparency = 0.7f;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        };

        frame.add(backgroundPanel);
        frame.setVisible(true);

        backgroundPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Welcome to Flights4U!");
        titleLabel.setFont(new Font("Arial", Font.TYPE1_FONT, 35));
        titleLabel.setForeground(Color.decode("#FFFFFF"));
        titleLabel.setBounds(180, 50, 400, 150);
        backgroundPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        usernameLabel.setForeground(Color.decode("#FFFFFF"));
        usernameLabel.setBounds(120, 250, 108, 35);
        backgroundPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(230, 250, 300, 30);
        usernameField.setFont(new Font("Arial", Font.BOLD, 16));
        backgroundPanel.add(usernameField);
        backgroundPanel.setBackground(Color.decode("#10243d"));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 20));
        passwordLabel.setForeground(Color.decode("#FFFFFF"));
        passwordLabel.setBounds(120, 310, 108, 35);
        backgroundPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(230, 310, 300, 30);
        passwordField.setFont(new Font("Arial", Font.BOLD, 25));
        backgroundPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.decode("#FFFFFF"));
        loginButton.setForeground(Color.decode("#10243d"));
        loginButton.setBounds(230, 380, 140, 40);
        loginButton.setFont(new Font("Arial", Font.TYPE1_FONT, 15));
        backgroundPanel.add(loginButton);

        JButton createAccountButton = new JButton("Sign Up");
        createAccountButton.setBackground(Color.decode("#10243d"));
        createAccountButton.setForeground(Color.decode("#FFFFFF"));
        createAccountButton.setBounds(390, 380, 140, 40);
        createAccountButton.setFont(new Font("Arial", Font.TYPE1_FONT, 15));
        backgroundPanel.add(createAccountButton);

        loginButton.addActionListener(e -> handleLogin(frame, usernameField.getText(), new String(passwordField.getPassword())));
        createAccountButton.addActionListener(e -> showCreateAccountDialog(frame));
    }

    private static void handleLogin(JFrame parent, String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            loggedInUser = username;
            JOptionPane.showMessageDialog(parent, "Login successful! Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.dispose();
            showMainPage();
        } else {
            JOptionPane.showMessageDialog(parent, "Invalid credentials. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showCreateAccountDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("New Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Create Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(parent, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                users.put(username, password);
                JOptionPane.showMessageDialog(parent, "Account created successfully! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static void showMainPage() {
        JFrame mainFrame = new JFrame("Main Page");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 700);
        mainFrame.setResizable(false);

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBounds(50, 50, 600, 50);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(300, 400, 100, 40);

        JButton bookingButton = new JButton("Go to Booking System");
        bookingButton.setBounds(250, 300, 200, 40);

        JButton bookingStatusButton = new JButton("Booking Status");
        bookingStatusButton.setBounds(250, 350, 200, 40);

        bookingButton.addActionListener(e -> showBookingPage());
        bookingStatusButton.addActionListener(e -> showBookingStatusPage());
        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            mainFrame.dispose();
            createAndShowLoginPage();
        });

        JPanel panel = new JPanel(null);
        panel.add(welcomeLabel);
        panel.add(bookingButton);
        panel.add(bookingStatusButton);
        panel.add(logoutButton);

        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    private static void showBookingPage() {
        JFrame bookingFrame = new JFrame("Flight Booking System");
        bookingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bookingFrame.setSize(700, 700);
        bookingFrame.setResizable(false);

        // Set layout to null for setBounds to work
        bookingFrame.setLayout(null);

        JLabel bookingLabel = new JLabel("Available Flights", JLabel.CENTER);
        bookingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bookingLabel.setBounds(50, 50, 600, 50);
        bookingFrame.add(bookingLabel);

        // Simulated list of available flights
        String[] flights = {"Air Asia - F101 - 20 Dec, 2024 12:00 PM", "Malaysian Airlines - F202 - 15 Dec, 2024 05:00 PM", "Singapore Airlines - F505 - 19 December, 2024 09:00AM"};
        JList<String> flightList = new JList<>(flights);
        flightList.setBounds(120, 150, 460, 200);
        JScrollPane flightScrollPane = new JScrollPane(flightList);
        flightScrollPane.setBounds(120, 150, 460, 200);
        bookingFrame.add(flightList);

        JButton bookFlightButton = new JButton("Book Flight");
        bookFlightButton.setBounds(250, 400, 150, 40);
        bookFlightButton.addActionListener(e -> {
            String selectedFlight = flightList.getSelectedValue();
            if (selectedFlight != null) {
                showPassengerInfoPage(selectedFlight);
            } else {
                JOptionPane.showMessageDialog(bookingFrame, "Please select a flight to book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bookingFrame.add(bookFlightButton);
        bookingFrame.setVisible(true);
    }

    private static void showPassengerInfoPage(String flight) {
        JFrame passengerInfoFrame = new JFrame("Passenger Information");
        passengerInfoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        passengerInfoFrame.setSize(700, 700);
        passengerInfoFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel passportLabel = new JLabel("Passport Number:");
        JTextField passportField = new JTextField();
        panel.add(passportLabel);
        panel.add(passportField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String passport = passportField.getText();
            if (passport.isEmpty()) {
                JOptionPane.showMessageDialog(passengerInfoFrame, "Passport number is required.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Ticket ticket = new Ticket(flight, passport);
                if (bookings.containsKey(loggedInUser)) {
                    bookings.get(loggedInUser).add(ticket);
                } else {
                    List<Ticket> userBookingList = new ArrayList<>();
                    userBookingList.add(ticket);
                    bookings.put(loggedInUser, userBookingList);
                }
                JOptionPane.showMessageDialog(passengerInfoFrame, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                passengerInfoFrame.dispose();
            }
        });

        panel.add(new JLabel()); // Empty cell for layout
        panel.add(submitButton);

        passengerInfoFrame.add(panel);
        passengerInfoFrame.setVisible(true);
    }

    private static void showBookingStatusPage() {
        JFrame bookingStatusFrame = new JFrame("Booking Status");
        bookingStatusFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bookingStatusFrame.setSize(700, 700);
        bookingStatusFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (bookings.containsKey(loggedInUser) && !bookings.get(loggedInUser).isEmpty()) {
            for (Ticket ticket : bookings.get(loggedInUser)) {
                panel.add(new JLabel("Flight: " + ticket.getFlight() + " - Passport: " + ticket.getPassport()));
            }
        } else {
            panel.add(new JLabel("No bookings found."));
        }

        bookingStatusFrame.add(panel);
        bookingStatusFrame.setVisible(true);
    }

    private static void loadUsers() {
        // Simulated users
        users.put("user1", "password123");
        users.put("user2", "password456");
        users.put("admin", "admin123");
    }

    static class Ticket {
        private String flight;
        private String passport;
        private boolean isConfirmed;

        public Ticket(String flight, String passport) {
            this.flight = flight;
            this.passport = passport;
            this.isConfirmed = false; // default is not confirmed
        }

        public String getFlight() {
            return flight;
        }

        public String getPassport() {
            return passport;
        }

        public boolean isConfirmed() {
            return isConfirmed;
        }
    }
}
