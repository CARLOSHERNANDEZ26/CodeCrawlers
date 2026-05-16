package codecrawl.ui;

import codecrawl.core.MainFrame;
import codecrawl.core.UserSession;
import java.util.prefs.Preferences;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProfileScreen extends StackPane {

    private MainFrame mainApp;
    private Label playerName; // Stored at class level so we can update it live

    public ProfileScreen(MainFrame mainApp) {
        this.mainApp = mainApp;
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #142210, #0a1208);"); 

        // --- HUD MAIN CONTAINER ---
        VBox profileHUD = new VBox(20);
        profileHUD.setAlignment(Pos.CENTER);
        profileHUD.setMaxSize(700, 560); // Slightly taller to fit the edit button
        profileHUD.setPadding(new Insets(30));
        profileHUD.setStyle("-fx-background-color: rgba(15, 25, 12, 0.93); -fx-background-radius: 20; -fx-border-color: #a2db4e; -fx-border-width: 3px; -fx-border-radius: 16px;");

        // --- LOGO HEADER ---
        try {
            ImageView logoHeader = new ImageView(new Image(getClass().getResource("/codecrawl_logo.png").toExternalForm()));
            logoHeader.setFitWidth(320);
            logoHeader.setPreserveRatio(true);
            profileHUD.getChildren().add(logoHeader);
        } catch (Exception e) {
            Label fallbackTitle = new Label("PLAYER PROFILE");
            fallbackTitle.setFont(Font.font("Arial Black", 32));
            fallbackTitle.setTextFill(Color.web("#a2db4e"));
            profileHUD.getChildren().add(fallbackTitle);
        }

        // --- PLAYER INFO ---
        String username = UserSession.getUsername();
        if (username == null || username.isEmpty()) username = "RoneloLolz";

        playerName = new Label("Account: " + username);
        playerName.setTextFill(Color.WHITE);
        playerName.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // --- NEW: EDIT PROFILE BUTTON ---
        Button editBtn = new Button("✏ Edit Profile");
        editBtn.setStyle("-fx-background-color: #2a5212; -fx-text-fill: #a2db4e; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-border-color: #a2db4e; -fx-border-width: 1px; -fx-border-radius: 8px;");
        editBtn.setOnAction(e -> showEditProfileOverlay());

        VBox statsList = new VBox(12);
        statsList.setAlignment(Pos.CENTER);
        statsList.getChildren().addAll(
            createStatRow("CURRENT RANKING", "Level " + UserSession.getLevel(), "#d1d5db"),
            createStatRow("ACTIVE MASCOT", UserSession.getSelectedMascot().toUpperCase(), "#89C765"), 
            createStatRow("ACCRUED ENERGY", UserSession.getXp() + " / 100 XP", "#ffcc00")
        );

        profileHUD.getChildren().addAll(playerName, editBtn, new Region(), statsList); // Region adds a tiny gap

        // --- BACK BUTTON NAVIGATION TOOLBAR ---
        HBox topToolbar = new HBox();
        topToolbar.setPadding(new Insets(20));
        topToolbar.setAlignment(Pos.TOP_LEFT);
        topToolbar.setPickOnBounds(false); // INVISIBLE SHIELD FIX!

        Button backBtn = new Button("←  Back to Main Menu");
        backBtn.setStyle("-fx-background-color: #2a5212; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 10px 20px; -fx-border-color: #a2db4e; -fx-border-width: 1px; -fx-border-radius: 8px;");
        backBtn.setOnAction(e -> mainApp.showHomeScreen()); 

        topToolbar.getChildren().add(backBtn);

        this.getChildren().addAll(profileHUD, topToolbar);
        StackPane.setAlignment(profileHUD, Pos.CENTER);
    }

    private HBox createStatRow(String labelText, String valueText, String valueColor) {
        Label label = new Label(labelText + ":");
        label.setTextFill(Color.web("#9ca3af"));
        label.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label value = new Label(valueText);
        value.setTextFill(Color.web(valueColor)); 
        value.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox row = new HBox(10, label, spacer, value);
        row.setMaxWidth(450); 
        return row;
    }

    // ══════════════════════════════════════════════════════════════════════
    // NEW: EDIT PROFILE OVERLAY
    // ══════════════════════════════════════════════════════════════════════
    private void showEditProfileOverlay() {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(400, 350);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: rgba(15, 25, 12, 0.98); -fx-background-radius: 20; -fx-border-color: #a2db4e; -fx-border-width: 3px; -fx-border-radius: 16px;");

        Label title = new Label("✏ EDIT ACCOUNT");
        title.setFont(Font.font("Arial Black", 22));
        title.setTextFill(Color.web("#a2db4e"));

        // Text Fields
        String inputStyle = "-fx-background-color: #1a2a1a; -fx-text-fill: #a2db4e; -fx-border-color: #2a5212; -fx-border-radius: 5; -fx-padding: 10;";
        
        TextField userField = new TextField(UserSession.getUsername());
        userField.setPromptText("New Username");
        userField.setStyle(inputStyle);

        PasswordField passField = new PasswordField();
        passField.setPromptText("New Password (leave blank to keep)");
        passField.setStyle(inputStyle);
        
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#ff6666"));

        // Buttons
        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.setStyle("-fx-background-color: #a2db4e; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 10 20;");
        
        Button cancelBtn = new Button("✖ Cancel");
        cancelBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 10 20;");

        HBox btnRow = new HBox(15, saveBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(title, userField, passField, errorLabel, btnRow);

        StackPane overlay = new StackPane(card);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        
        // --- SAVE LOGIC ---
        saveBtn.setOnAction(e -> {
            String newName = userField.getText().trim();
            String newPass = passField.getText();

            if (newName.isEmpty()) {
                errorLabel.setText("Username cannot be empty!");
                return;
            }

            // 1. Update active session
            UserSession.setUsername(newName);
            playerName.setText("Account: " + newName);

            // 2. Safely update saved Preferences for the LogInUI
            try {
                Preferences prefs = Preferences.userNodeForPackage(codecrawl.auth.LogInUI.class);
                prefs.put("username", newName);
                if (!newPass.isEmpty()) {
                    prefs.put("password", newPass);
                }
            } catch (Exception ex) {
                System.err.println("Could not save preferences: " + ex.getMessage());
            }

            // Close the overlay
            this.getChildren().remove(overlay);
        });

        // Close mechanics
        cancelBtn.setOnAction(e -> this.getChildren().remove(overlay));
        overlay.setOnMouseClicked(e -> { if (e.getTarget() == overlay) this.getChildren().remove(overlay); });

        this.getChildren().add(overlay);
    }
}