package codecrawl.levels;

import codecrawl.core.MainFrame;
import codecrawl.core.UserSession;
import codecrawl.engine.MascotManager;
import codecrawl.ui.MuteButton;
import codecrawl.ui.NavButton; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class House extends StackPane {

    private MainFrame mainApp;

    public House(MainFrame mainApp) {
        this.mainApp = mainApp;

        // 1. CLEAN BACKGROUND
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e112a, #0a0512);");

        // 2. THE NAVIGATION & AUDIO TOOLBAR WITH INVISIBLE SHIELD FIX
        NavButton leaveMapBtn = new NavButton("← LEAVE HOUSE", this, () -> mainApp.showWorldSelection());
        HBox toolbar = new HBox(15, leaveMapBtn, new MuteButton());
        toolbar.setAlignment(Pos.TOP_RIGHT);
        toolbar.setPadding(new Insets(20));
        toolbar.setPickOnBounds(false); 

        // 3. NEW: TOP LEFT LIVE ACCOUNT LEVEL BADGE
        String username = UserSession.getUsername() != null ? UserSession.getUsername() : "RoneloLolz";
        Label playerBadge = new Label("👤 " + username + " | Lvl " + UserSession.getLevel());
        playerBadge.setStyle("-fx-background-color: rgba(0,0,0,0.65); -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20; -fx-border-color: #89C765; -fx-border-radius: 20; -fx-border-width: 1.5;");
        playerBadge.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        
        HBox leftToolbar = new HBox(playerBadge);
        leftToolbar.setAlignment(Pos.TOP_LEFT);
        leftToolbar.setPadding(new Insets(20));
        leftToolbar.setPickOnBounds(false);

        // 4. MAIN INTERFACE MENU CONTAINER
        VBox layout = new VBox(20); 
        layout.setAlignment(Pos.CENTER);
        layout.setPickOnBounds(false);

        // ─── UPGRADED: LOADS EQUIPPED PET DYNAMICALLY IN THE COUCH SLOT ───
        try {
            String activeMascotPath = MascotManager.getCurrentMascotPath(true); // Loads idle state asset file
            ImageView mascotAvatar = new ImageView(new Image(getClass().getResource(activeMascotPath).toExternalForm()));
            mascotAvatar.setFitWidth(150);
            mascotAvatar.setPreserveRatio(true);
            layout.getChildren().add(mascotAvatar); 
        } catch (Exception e) {
            // Fallback to house graphic asset if resource files fail
            try {
                ImageView houseIcon = new ImageView(new Image(getClass().getResource("/house.png").toExternalForm()));
                houseIcon.setFitWidth(160);
                houseIcon.setPreserveRatio(true);
                layout.getChildren().add(houseIcon);
            } catch (Exception ex) {
                System.err.println("[House UI] Companion sprite image parsing error.");
            }
        }

        Text title = new Text("THE LEARNING HOUSE");
        title.setFont(Font.font("Arial Black", FontWeight.BOLD, 36));
        title.setFill(Color.WHITE);

        HBox topicButtons = new HBox(25);
        topicButtons.setAlignment(Pos.CENTER);
        topicButtons.setPickOnBounds(false);

        Button htmlBtn = createStyledButton("HTML Room", "#E34C26");
        Button cssBtn = createStyledButton("CSS Room", "#264DE4");
        Button dbBtn = createStyledButton("Database Room", "#F29111");

        htmlBtn.setOnAction(e -> mainApp.showHTMLRoom());
        cssBtn.setOnAction(e -> mainApp.showCSSRoom());
        dbBtn.setOnAction(e -> mainApp.showDatabaseRoom());

        topicButtons.getChildren().addAll(htmlBtn, cssBtn, dbBtn);
        layout.getChildren().addAll(title, topicButtons);
        
        // Layer everything cleanly onto the root viewport stack pane
        this.getChildren().addAll(layout, leftToolbar, toolbar);
        StackPane.setAlignment(toolbar, Pos.TOP_RIGHT);
        StackPane.setAlignment(leftToolbar, Pos.TOP_LEFT);
    }

    private Button createStyledButton(String text, String hexColor) {
        Button btn = new Button(text);
        btn.setPrefSize(200, 80);
        btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: white; " +
                     "-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 12; " +
                     "-fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 3; -fx-border-radius: 10;");
        btn.setCursor(javafx.scene.Cursor.HAND);
        
        btn.setOnMouseEntered(e -> btn.setTranslateY(-4));
        btn.setOnMouseExited(e -> btn.setTranslateY(0));
        
        return btn;
    }
}