/* Advance options (if available)
We can have the account details for a passenger,What's more , we ask them to do the authentication
when they are logged in and booking the ticket.Also save this passenger into our database--Next time when the same passenger
book tickets, they don't need to add their details again.
Allow user to view all their bookings.
can store the history of flight the ticket this user bought.
 */
import java.util.HashMap;
import java.util.Scanner;

public class User_Account {

    private static HashMap<String, String> users = new HashMap<>();
    private static String loggedInUser = null;

    static {
        // Predefined accounts for testing
        users.put("admin", "password");
        users.put("user1", "12345");
        users.put("user2", "67890");
    }

    public static boolean login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            loggedInUser = username;
            System.out.println("Login successful! Welcome, " + username + "!");
            return true;
        } else {
            System.out.println("Invalid credentials. Try again.");
            return false;
        }
    }

    public static void logout() {
        System.out.println("Goodbye, " + loggedInUser + "!");
        loggedInUser = null;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }
}
