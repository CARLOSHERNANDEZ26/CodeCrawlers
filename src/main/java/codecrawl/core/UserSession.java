package codecrawl.core;

import codecrawl.db.Database;
import java.sql.*;

public class UserSession {
    private static int userId = 1;
    private static String username = "Player1";
    private static int xp = 0;
    private static int level = 1;
    private static String selectedMascot = "snake";

    public static int getUserId() { return userId; }
    public static void setUserId(int id) { userId = id; }

    public static String getUsername() { return username; }
    public static void setUsername(String name) { username = name; }

    public static int getXp() { return xp; }
    public static int getLevel() { return level; }
    
    public static String getSelectedMascot() { return selectedMascot; }
    public static void setSelectedMascot(String mascot) { 
        selectedMascot = mascot; 
        saveToDatabase(); // Automatically commit to database on configuration change
    }

    public static void addXp(int amount) {
        xp += amount;
        if (xp >= 100) {
            xp -= 100;
            level++;
            System.out.println("[PROGRESS ENGINE] LEVEL UP! Reached Level " + level);
        }
        saveToDatabase(); // Auto-save when XP drops in!
    }

    // ══════════════════════════════════════════════════════════════════════
    // DATABASE HANDLERS
    // ══════════════════════════════════════════════════════════════════════
    public static void loadFromDatabase() {
        if (userId <= 0) return;
        
        String sql = "SELECT username, level, xp, selected_mascot FROM users WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            
            if (ps == null) return;
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("username");
                    level = rs.getInt("level");
                    xp = rs.getInt("xp");
                    String mascot = rs.getString("selected_mascot");
                    if (mascot != null && !mascot.isEmpty()) {
                        selectedMascot = mascot;
                    }
                    System.out.println("[DB SUCCESS] Loaded profile for " + username + " (Lvl " + level + ")");
                }
            }
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Failed to load session states: " + ex.getMessage());
        }
    }

    public static void saveToDatabase() {
        if (userId <= 0) return;

        String sql = "UPDATE users SET username = ?, level = ?, xp = ?, selected_mascot = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            
            if (ps == null) return;
            ps.setString(1, username);
            ps.setInt(2, level);
            ps.setInt(3, xp);
            ps.setString(4, selectedMascot);
            ps.setInt(5, userId);
            ps.executeUpdate();
            System.out.println("[DB SUCCESS] User progress state securely saved.");
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Failed to serialize session state: " + ex.getMessage());
        }
    }

    public static void clear() {
        userId = 0;
        username = "";
        xp = 0;
        level = 1;
        selectedMascot = "snake";
    }
}