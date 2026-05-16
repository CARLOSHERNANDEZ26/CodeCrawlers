package codecrawl.auth;


/**
 *
 * @author JE-ANN
 */
import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        // Since you have no password, leave the password string empty ""
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = ""; 

        try {
            // This line attempts to open the bridge between Java and MySQL
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null) {
                System.out.println("------------------------------------------");
                System.out.println("SUCCESS: Local database connection active!");
                System.out.println("------------------------------------------");
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("------------------------------------------");
            System.out.println("CONNECTION FAILED!");
            System.out.println("Error Message: " + e.getMessage());
            System.out.println("------------------------------------------");
        }
    }
}

