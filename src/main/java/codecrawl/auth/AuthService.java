package codecrawl.auth;

import codecrawl.db.Database;
import codecrawl.core.UserSession;
import java.sql.*;
import java.util.regex.Pattern;

public class AuthService {

    private static final String USERNAME_PATTERN =
        "^[a-zA-Z0-9!@#$%^&*()_+={}\\[\\]:;\"'|\\\\<>,.?/~`-]+$";

    // REGISTER - Returns "SUCCESS" or a specific error message
    public String register(String username, String email, String password) {
        // Validation Checks
        if (username.contains(" ") || email.contains(" ") || password.contains(" "))
            return "Whitespaces are invalid in all fields!";
        if (username.length() < 4)
            return "Username must be at least 4 characters!";
        if (password.length() < 8)
            return "Password must be at least 8 characters!";

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email))
            return "Invalid email address!";
        if (!Pattern.matches(USERNAME_PATTERN, username))
            return "Username contains invalid characters!";
        
        // Note: Manual check is kept, but MySQL's UNIQUE constraint is the final safety net
        if (checkUserExists(username))
            return "Username is already taken!";

        String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return "Database connection failed!";
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            return "SUCCESS"; 
        } catch (SQLException e) {
            // MySQL error code 1062 = Duplicate entry (UNIQUE constraint violation)
            if (e.getErrorCode() == 1062) {
                return "Username or email already exists!";
            }
            return "Database Error: " + e.getMessage();
        }
    }

    // LOGIN — sets session data and returns "SUCCESS"
    public String loginDual(String identifier, String password) {
        if (identifier.isEmpty() || password.isEmpty())
            return "Please fill in all fields.";

        String sql = "SELECT id, username FROM users WHERE (username = ? OR email = ?) AND password = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return "Database connection failed!";
            
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier);
            pstmt.setString(3, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserSession.setUserId(rs.getInt("id"));
                    UserSession.setUsername(rs.getString("username"));
                    
                    // Save to Preferences for the "Remember Me" functionality
                    java.util.prefs.Preferences.userRoot().node("CodeCrawl").put("lastUser", rs.getString("username"));
                    
                    return "SUCCESS";
                } else {
                    return "Invalid username/email or password.";
                }
            }
        } catch (SQLException e) {
            return "DB_ERROR: " + e.getMessage();
        }
    }

    // FORGOT PASSWORD
    public boolean updatePassword(String username, String email, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE username = ? AND email = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update Error: " + e.getMessage());
            return false;
        }
    }

    private boolean checkUserExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
    
    public boolean saveLevelProgress(int levelId, int scoreEarned) {
    // We use the ID stored in UserSession to know WHO is playing
    String sql = "INSERT INTO user_progress (user_id, level_id, score) VALUES (?, ?, ?)";
    
        try (Connection conn = Database.connect()) {
            if (conn == null) return false;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, UserSession.getUserId()); // Links to users table
                pstmt.setInt(2, levelId);                // e.g., 1 for Jungle, 2 for CSS
                pstmt.setInt(3, scoreEarned);             // The points they just got
            
                return pstmt.executeUpdate() > 0;
        }
    } catch (SQLException e) {
        System.err.println("Progress Save Error: " + e.getMessage());
        return false;
    }
    }    
    // DELETE ACCOUNT
    public boolean deleteAccount() {
       int userId = UserSession.getUserId();
       if (userId <= 0) return false;

       String sql = "DELETE FROM users WHERE id = ?";
       try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;
            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
        
            if (affectedRows > 0) {
                UserSession.clear(); 
                java.util.prefs.Preferences.userRoot().node("CodeCrawl").remove("lastUser");
                return true; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}