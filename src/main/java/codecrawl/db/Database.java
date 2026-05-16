package codecrawl.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // 1. Connection Details
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "CodeCrawl";
    private static final String USER = "root";      // Default MySQL user
    private static final String PASS = ""; // Change to your actual password

    public static Connection connect() {
        // 2. The MySQL JDBC URL with recommended parameters
        // serverTimezone=UTC prevents connection crashes in some environments
        // useSSL=false is often used for local development to simplify setup
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true", 
                            HOST, PORT, DB_NAME);
        
        try {
            // 3. Registering the modern MySQL driver (Connector/J 8.0+)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(url, USER, PASS);
            System.out.println("Success! Connected to MySQL Database: " + DB_NAME);
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found! Did you add the JAR to your classpath?");
        } catch (SQLException e) {
            System.err.println("Connection Error: " + e.getMessage());
        }
        return null;
    }
}

    
