package codecrawl.ui;

import codecrawl.core.MainFrame;
import codecrawl.core.UserSession;
import codecrawl.db.Database;
import codecrawl.engine.AudioManager; 
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox; 
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.sql.*;

public class HomeScreen extends StackPane {

    private final MainFrame mainApp;

    public HomeScreen(MainFrame mainApp) {
        this.mainApp = mainApp;
        ensureUsersTableExists();
        UserSession.loadFromDatabase(); // Pull fresh level data immediately on load
        setupBackground();
        buildUI();
    }

    private void ensureUsersTableExists() {
        String ddl = """
            CREATE TABLE IF NOT EXISTS users (
                id               INT PRIMARY KEY AUTO_INCREMENT,
                username         VARCHAR(255) NOT NULL UNIQUE,
                email            VARCHAR(255) NOT NULL UNIQUE,
                password         VARCHAR(255) NOT NULL,
                current_level_id INT NOT NULL DEFAULT 1,
                level            INT NOT NULL DEFAULT 1,
                xp               INT NOT NULL DEFAULT 0,
                selected_mascot  VARCHAR(50) NOT NULL DEFAULT 'snake'
            ) ENGINE=InnoDB;
            """;
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {
            if (conn != null) stmt.execute(ddl);
        } catch (SQLException ex) {
            System.err.println("[HomeScreen DB] Initialization check failure: " + ex.getMessage());
        }
    }

    private void setupBackground() {
        try {
            Image img = new Image(getClass().getResource("/world/homescreen.jpg").toExternalForm(), true);
            this.setBackground(new Background(new BackgroundImage(
                img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
            )));
        } catch (Exception e) {
            this.setStyle("-fx-background-color: #2b5e2b;"); 
        }
    }

    private void buildUI() {
        AnchorPane uiLayer = new AnchorPane();
        uiLayer.setPadding(new Insets(30));

        // ─── UPGRADED: DYNAMIC PROFILE TEXT BADGE ───
        String username = UserSession.getUsername() != null ? UserSession.getUsername() : "RoneloLolz";
        int currentLevel = UserSession.getLevel();
        int currentXp = UserSession.getXp();
        
        Label playerBadge = new Label("👤 " + username + " | Lvl " + currentLevel + " (" + currentXp + "/100 XP)");
        playerBadge.setStyle("-fx-background-color: rgba(0,0,0,0.75); -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20; -fx-border-color: #a2db4e; -fx-border-width: 1.5; -fx-border-radius: 20;");
        playerBadge.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        
        AnchorPane.setTopAnchor(playerBadge, 15.0);
        AnchorPane.setLeftAnchor(playerBadge, 15.0);

        Button settingsBtn = new Button("⚙ Settings");
        settingsBtn.setStyle("-fx-background-color: rgba(0,0,0,0.65); -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20; -fx-border-color: #a2db4e; -fx-border-radius: 20; -fx-border-width: 1.5; -fx-cursor: hand;");
        settingsBtn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        
        settingsBtn.setOnMouseEntered(e -> settingsBtn.setStyle("-fx-background-color: rgba(40,80,20,0.85); -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20; -fx-border-color: #b4ec5b; -fx-border-radius: 20; -fx-border-width: 1.5; -fx-cursor: hand;"));
        settingsBtn.setOnMouseExited(e -> settingsBtn.setStyle("-fx-background-color: rgba(0,0,0,0.65); -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20; -fx-border-color: #a2db4e; -fx-border-radius: 20; -fx-border-width: 1.5; -fx-cursor: hand;"));
        settingsBtn.setOnAction(e -> showSettingsOverlay());

        AnchorPane.setTopAnchor(settingsBtn, 15.0);
        AnchorPane.setRightAnchor(settingsBtn, 15.0);

        VBox buttonMenu = new VBox(15); 
        buttonMenu.setAlignment(Pos.CENTER_RIGHT);
        
        Node playBtn;
        try {
            ImageView playImg = new ImageView(new Image(getClass().getResource("/play.png").toExternalForm()));
            playImg.setFitWidth(275); 
            playImg.setPreserveRatio(true);
            playImg.setCursor(javafx.scene.Cursor.HAND);
            
            playImg.setOnMouseEntered(e -> { playImg.setScaleX(1.05); playImg.setScaleY(1.05); });
            playImg.setOnMouseExited(e -> { playImg.setScaleX(1.0); playImg.setScaleY(1.0); });
            playImg.setOnMousePressed(e -> playImg.setTranslateY(4));
            playImg.setOnMouseReleased(e -> playImg.setTranslateY(0));
            
            playImg.setOnMouseClicked(e -> mainApp.showWorldSelection());
            playBtn = playImg;
        } catch (Exception e) {
            Button fallbackBtn = createMenuButton("▶  Play");
            fallbackBtn.setOnAction(ev -> mainApp.showWorldSelection());
            playBtn = fallbackBtn;
        }

        Button leaderBtn = createMenuButton("🏆  Leaderboard");
        leaderBtn.setOnAction(e -> mainApp.showLeaderboard());

        Button shopBtn = createMenuButton("🛒  Mascot Shop");
        shopBtn.setOnAction(e -> mainApp.showShopScreen());

        Button profileBtn = createMenuButton("👤  Profile");
        profileBtn.setOnAction(e -> mainApp.showProfileScreen()); 

        // ─── UPGRADED: ACTION DISPATCHES OVERLAY CONFIRMATION ───
        Button exitBtn = createMenuButton("✖  Exit");
        exitBtn.setOnAction(e -> showExitConfirmationOverlay());

        buttonMenu.getChildren().addAll(playBtn, leaderBtn, shopBtn, profileBtn, exitBtn);

        AnchorPane.setRightAnchor(buttonMenu, 60.0);
        AnchorPane.setTopAnchor(buttonMenu, 290.0); 

        uiLayer.getChildren().addAll(playerBadge, settingsBtn, buttonMenu);
        this.getChildren().add(uiLayer);
    }

    private void showExitConfirmationOverlay() {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(460, 220); 
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: rgba(15, 25, 12, 0.98); -fx-border-color: #ffcc00; -fx-border-width: 2.5; -fx-background-radius: 16; -fx-border-radius: 16;");

        Label warningIcon = new Label("⚠");
        warningIcon.setTextFill(Color.web("#ffcc00"));
        warningIcon.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        Label title = new Label("QUIT CODECRAWL?");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 18));

        Button saveLeaveBtn = new Button("💾 SAVE & QUIT");
        saveLeaveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 15; -fx-background-radius: 6;");
        
        Button justLeaveBtn = new Button("⚠ JUST QUIT");
        justLeaveBtn.setStyle("-fx-background-color: #cc2222; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 15; -fx-background-radius: 6;");
        
        Button cancelBtn = new Button("✖ CANCEL");
        cancelBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 15; -fx-background-radius: 6;");

        HBox btnRow = new HBox(12, saveLeaveBtn, justLeaveBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(warningIcon, title, btnRow);

        StackPane overlay = new StackPane(card);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        overlay.setOpacity(0);
        
        this.getChildren().add(overlay);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), overlay);
        fadeIn.setToValue(1);
        fadeIn.play();

        Runnable dismiss = () -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(180), overlay);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> this.getChildren().remove(overlay));
            fadeOut.play();
        };

        cancelBtn.setOnAction(e -> dismiss.run());
        overlay.setOnMouseClicked(e -> { if (e.getTarget() == overlay) dismiss.run(); });

        justLeaveBtn.setOnAction(e -> System.exit(0));
        saveLeaveBtn.setOnAction(e -> {
            UserSession.saveToDatabase(); 
            System.exit(0);
        });
    }

    private void showSettingsOverlay() {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(400, 320); 
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: rgba(15, 25, 12, 0.95); -fx-background-radius: 20; -fx-border-color: #a2db4e; -fx-border-width: 3px; -fx-border-radius: 16px;");

        Label title = new Label("⚙  SYSTEM SETTINGS");
        title.setFont(Font.font("Arial Black", 22));
        title.setTextFill(Color.web("#a2db4e"));

        CheckBox muteBox = new CheckBox("Stop Background Music");
        muteBox.setTextFill(Color.WHITE);
        muteBox.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        muteBox.setSelected(!AudioManager.isPlaying);
        muteBox.setOnAction(e -> AudioManager.toggleMusic());

        Label volLabel = new Label("Background Music Volume");
        volLabel.setTextFill(Color.WHITE);
        volLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Slider volSlider = new Slider(0, 100, 70); 
        volSlider.setStyle("-fx-control-inner-background: #2a5212;");
        volSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            AudioManager.setVolume(newVal.doubleValue() / 100.0);
        });

        Button logoutBtn = new Button("🚪 Log Out");
        logoutBtn.setStyle("-fx-background-color: #cc2222; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 12 25; -fx-font-size: 14px;");
        logoutBtn.setOnAction(e -> {
            UserSession.saveToDatabase(); // Save profile progress state cleanly on disconnect
            UserSession.clear();
            mainApp.showLoginScreen();
        });

        Button closeBtn = new Button("✖ Close");
        closeBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 12 25; -fx-font-size: 14px;");

        HBox btnRow = new HBox(20, logoutBtn, closeBtn);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(15, 0, 0, 0));

        card.getChildren().addAll(title, muteBox, volLabel, volSlider, btnRow);

        StackPane overlay = new StackPane(card);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.75);");
        
        closeBtn.setOnAction(e -> this.getChildren().remove(overlay));
        overlay.setOnMouseClicked(e -> { if (e.getTarget() == overlay) this.getChildren().remove(overlay); });

        this.getChildren().add(overlay);
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(275, 62); 
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        String normalStyle = "-fx-background-color: linear-gradient(to bottom, #a2db4e, #53a62b); -fx-background-radius: 12; -fx-border-color: #2a5212; -fx-border-width: 3.5; -fx-border-radius: 9; -fx-text-fill: white; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 4, 0.0, 0, 3);";
        String hoverStyle = "-fx-background-color: linear-gradient(to bottom, #b4ec5b, #62bf33); -fx-background-radius: 12; -fx-border-color: #2a5212; -fx-border-width: 3.5; -fx-border-radius: 9; -fx-text-fill: white; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 6, 0.0, 0, 4);";

        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
        
        btn.setOnMousePressed(e -> btn.setTranslateY(3));
        btn.setOnMouseReleased(e -> btn.setTranslateY(0));
        
        return btn;
    }
}