/* Advance options (if available)
We can have the account details for a passenger,What's more , we ask them to do the authentication
when they are logged in and booking the ticket.Also save this passenger into our database--Next time when the same passenger
book tickets, they don't need to add their details again.
Allow user to view all their bookings.
can store the history of flight the ticket this user bought.
 */
import java.io.Serializable;
import java.util.HashMap;

public class User_Account implements Serializable {

    private static HashMap<String, User_Account> accounts = new HashMap<>();

    private String username;
    private String password;
    private String fullName;

    public User_Account(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // Create new account
    public static boolean createAccount(String username, String password, String fullName) {
        if (accounts.containsKey(username)) {
            return false;
        }
        accounts.put(username, new User_Account(username, password, fullName));
        return true;
    }

    // Validate
    public static boolean login(String username, String password) {
        User_Account account = accounts.get(username);
        return account != null && account.password.equals(password);
    }

    // Update user details
    public static boolean updateAccount(String username, String newPassword, String newFullName) {
        User_Account account = accounts.get(username);
        if (account == null) {
            return false;
        }
        account.password = newPassword;
        account.fullName = newFullName;
        return true;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", Full Name: " + fullName;
    }
}
