package codecrawl.auth;

import codecrawl.auth.AuthService;
import codecrawl.core.UserSession;
import codecrawl.db.Database;
import codecrawl.engine.AudioManager; // Added import for audio management
import java.util.prefs.Preferences;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.*;

public class LogInUI extends StackPane {
    private VBox formBox;
    private ImageView logoView;
    private TextField userField, emailField, passTextField, confirmPassTextField;
    private PasswordField passField, confirmPassField;
    private Label errorLabel, title;
    private Button actionBtn;
    private Hyperlink toggleLink, forgotLink;
    private StackPane confirmPassStack;
    private CheckBox rememberMeBox; 
    private Button muteBtn; // Reference stored to change text dynamically
    
    private boolean isLoginMode = true;
    private Runnable onSuccess;

    public LogInUI(AuthService auth) {
        Image bgImage = new Image(getClass().getResourceAsStream("/island background.png"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(false);
        bgView.fitWidthProperty().bind(this.widthProperty());
        bgView.fitHeightProperty().bind(this.heightProperty());

        Image logoImg = new Image(getClass().getResourceAsStream("/codecrawl_logo.png"));
        logoView = new ImageView(logoImg);
        logoView.setFitWidth(570); 
        logoView.setPreserveRatio(true);
        VBox.setMargin(logoView, new Insets(-55, 0, 0, 0)); 

        Region topSpacer = new Region(); VBox.setVgrow(topSpacer, Priority.ALWAYS);
        Region bottomSpacer = new Region(); VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        formBox = new VBox(12);
        formBox.setMaxSize(480, 620);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setPadding(new Insets(0, 40, 40, 40));
        formBox.setStyle("-fx-background-color: rgba(30, 47, 30, 0.9); -fx-background-radius: 30; -fx-border-color: #ffcc00; -fx-border-radius: 25; -fx-border-width: 4; -fx-effect: dropshadow(three-pass-box, black, 20, 0, 0, 0);");

        title = new Label("LOG-IN");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 42));
        title.setTextFill(Color.web("#ffcc00"));

        String inputStyle = "-fx-background-color: #f4ecd8; -fx-text-fill: #3e2723; -fx-background-radius: 10; -fx-font-size: 22px; -fx-padding: 12px; -fx-prompt-text-fill: #8d6e63;";

        userField = new TextField();
        userField.setPromptText("Username/Email");
        userField.setStyle(inputStyle);

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(inputStyle);
        emailField.setManaged(false); emailField.setVisible(false);

        passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle(inputStyle);
        passTextField = new TextField();
        passTextField.setManaged(false); passTextField.setVisible(false);
        passTextField.setStyle(inputStyle);
        passTextField.textProperty().bindBidirectional(passField.textProperty());
        StackPane passStack = new StackPane(passField, passTextField);

        confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm Password");
        confirmPassField.setStyle(inputStyle);
        confirmPassTextField = new TextField();
        confirmPassTextField.setManaged(false); confirmPassTextField.setVisible(false);
        confirmPassTextField.setStyle(inputStyle);
        confirmPassTextField.textProperty().bindBidirectional(confirmPassField.textProperty());
        confirmPassStack = new StackPane(confirmPassField, confirmPassTextField);
        confirmPassStack.setManaged(false); confirmPassStack.setVisible(false);

        CheckBox showPassBox = new CheckBox("Show password");
        showPassBox.setTextFill(Color.WHITE);
        
        rememberMeBox = new CheckBox("Remember Me");
        rememberMeBox.setTextFill(Color.WHITE);
        
        Preferences prefs = Preferences.userNodeForPackage(LogInUI.class);
        String savedUser = prefs.get("username", "");
        String savedPass = prefs.get("password", "");
        
        if (!savedUser.isEmpty() && !savedPass.isEmpty()) {
            userField.setText(savedUser);
            passField.setText(savedPass);
            passTextField.setText(savedPass); 
            rememberMeBox.setSelected(true);
        }
        
        forgotLink = new Hyperlink("Forgot password");
        forgotLink.setTextFill(Color.web("#ffcc00"));
        
        Region optionsSpacer = new Region(); HBox.setHgrow(optionsSpacer, Priority.ALWAYS);
        HBox optionsRow = new HBox(15, showPassBox, rememberMeBox, optionsSpacer, forgotLink);
        optionsRow.setAlignment(Pos.CENTER_LEFT);

        errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#ff6666"));
        errorLabel.setWrapText(true);

        actionBtn = new Button("ENTER WORLD");
        actionBtn.setPrefWidth(Double.MAX_VALUE);
        actionBtn.setDefaultButton(true);
        actionBtn.setStyle("-fx-background-color: linear-gradient(#76b900, #4e7a00); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16; -fx-background-radius: 15; -fx-cursor: hand;");

        toggleLink = new Hyperlink("Don't have an account? Register here");
        toggleLink.setTextFill(Color.LIGHTGRAY);

        formBox.getChildren().addAll(logoView, topSpacer, title, userField, emailField, 
                                     passStack, confirmPassStack, optionsRow, errorLabel, 
                                     actionBtn, toggleLink, bottomSpacer);
        
        // ══════════════════════════════════════════════════════════════════════
        // NEW: FLOATING TOP-RIGHT AUDIO TOOLBAR CONTAINER
        // ══════════════════════════════════════════════════════════════════════
        muteBtn = new Button();
        updateMuteButtonText(); // Set text dynamically on load
        muteBtn.setStyle("-fx-background-color: rgba(30, 47, 30, 0.85); -fx-text-fill: #ffcc00; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-color: #ffcc00; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 10px 20px;");
        
        muteBtn.setOnAction(e -> {
            AudioManager.toggleMusic(); // Direct safe memory-deallocation call
            updateMuteButtonText();    // Toggle indicator visually
        });

        HBox topToolbar = new HBox(muteBtn);
        topToolbar.setAlignment(Pos.TOP_RIGHT);
        topToolbar.setPadding(new Insets(20));
        topToolbar.setPickOnBounds(false); // Invisible shield fix

        // Assemble all root display frames onto the main container stack
        this.getChildren().addAll(bgView, formBox, topToolbar);
        StackPane.setAlignment(topToolbar, Pos.TOP_RIGHT);

        initActions(auth, showPassBox);
    }

    private void updateMuteButtonText() {
        if (AudioManager.isPlaying) {
            muteBtn.setText("🎵 Music: ON");
        } else {
            muteBtn.setText("🔇 Music: OFF");
        }
    }

    private void initActions(AuthService auth, CheckBox showPassBox) {
        showPassBox.setOnAction(e -> {
            boolean show = showPassBox.isSelected();
            passField.setVisible(!show); passField.setManaged(!show);
            passTextField.setVisible(show); passTextField.setManaged(show);
            if (!isLoginMode) {
                confirmPassField.setVisible(!show); confirmPassField.setManaged(!show);
                confirmPassTextField.setVisible(show); confirmPassTextField.setManaged(show);
            }
        });

        toggleLink.setOnAction(e -> {
            isLoginMode = !isLoginMode;
            title.setText(isLoginMode ? "LOG-IN" : "SIGN-UP");
            actionBtn.setText(isLoginMode ? "ENTER WORLD" : "CREATE ACCOUNT");
            toggleLink.setText(isLoginMode ? "Don't have an account? Sign-up here" : "Back to Login");
            
            userField.setPromptText(isLoginMode ? "Username/Email" : "Username");
            emailField.setVisible(!isLoginMode); emailField.setManaged(!isLoginMode);
            confirmPassStack.setVisible(!isLoginMode); confirmPassStack.setManaged(!isLoginMode);
            rememberMeBox.setVisible(isLoginMode); 
            errorLabel.setText("");
        });

        actionBtn.setOnAction(e -> {
            String input = userField.getText().trim();
            String pass = passField.getText();

            if (input.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Missing Fields");
                return;
            }

            if (isLoginMode) {
                String result = auth.loginDual(input, pass);
                if ("SUCCESS".equals(result)) {
                    try (Connection conn = Database.connect();
                         PreparedStatement ps = conn != null ? conn.prepareStatement("SELECT id, username FROM users WHERE username = ? OR email = ?") : null) {
                        
                        if (ps != null) {
                            ps.setString(1, input);
                            ps.setString(2, input);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    UserSession.setUserId(rs.getInt("id"));
                                    UserSession.setUsername(rs.getString("username"));
                                    System.out.println("[SESSION BOUND] Successfully logged in as: " + UserSession.getUsername() + " (ID: " + UserSession.getUserId() + ")");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        System.err.println("[SESSION BIND ERROR] Failed linking user context details: " + ex.getMessage());
                    }

                    Preferences prefs = Preferences.userNodeForPackage(LogInUI.class);
                    if (rememberMeBox.isSelected()) {
                        prefs.put("username", input);
                        prefs.put("password", pass);
                    } else {
                        prefs.remove("username");
                        prefs.remove("password");
                    }
                    
                    if (onSuccess != null) onSuccess.run();
                } else {
                    errorLabel.setText(result);
                }
            } else {
                String email = emailField.getText().trim();
                String confirmPass = confirmPassField.getText();

                if (email.isEmpty() || !email.contains("@")) {
                    errorLabel.setText("Invalid Email");
                } else if (!pass.equals(confirmPass)) {
                    errorLabel.setText("Passwords do not match!");
                } else {
                    String result = auth.register(input, email, pass);
                    if ("SUCCESS".equals(result)) {
                        errorLabel.setText("Success! Please Login.");
                        toggleLink.fire(); 
                    } else {
                        errorLabel.setText(result); 
                    }
                }
            }
        });

        forgotLink.setOnAction(e -> {
            String user = userField.getText();
            if(user.isEmpty()) { errorLabel.setText("Enter username first!"); return; }

            TextInputDialog emailDlg = new TextInputDialog();
            emailDlg.setHeaderText("Verify Identity for " + user);
            emailDlg.setContentText("Enter registered Email:");
            
            emailDlg.showAndWait().ifPresent(email -> {
                TextInputDialog passDlg = new TextInputDialog();
                passDlg.setHeaderText("Identity Verified. Set new password:");
                passDlg.showAndWait().ifPresent(newPass -> {
                    if (auth.updatePassword(user, email, newPass)) {
                        errorLabel.setText("Password Updated! Proceed to Login.");
                    } else {
                        errorLabel.setText("Verification Failed: Info mismatch.");
                    }
                });
            });
        });
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccess = callback;
    }

    public void setError(String msg) {
        errorLabel.setText(msg);
    }
}