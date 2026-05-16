package codecrawl.ui;

import codecrawl.core.MainFrame;
import codecrawl.core.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ShopScreen extends StackPane {

    private MainFrame mainApp;
    private Label statusLabel;
    
    private VBox snakeCard, elephantCard, chameleonCard;
    private Button snakeBtn, elephantBtn, chameleonBtn;

    public ShopScreen(MainFrame mainApp) {
        this.mainApp = mainApp;
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a2a1a, #0d140d);"); 

        // --- TOP TOOLBAR ---
        HBox topToolbar = new HBox();
        topToolbar.setPadding(new Insets(20));
        topToolbar.setAlignment(Pos.TOP_LEFT);
        topToolbar.setPickOnBounds(false);

        Button backBtn = new Button("← Back to Menu");
        backBtn.setStyle("-fx-background-color: #2a5212; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 10px 20px; -fx-border-color: #a2db4e; -fx-border-width: 1px; -fx-border-radius: 8px;");
        backBtn.setOnAction(e -> mainApp.showHomeScreen()); 
        topToolbar.getChildren().add(backBtn);

        // --- MAIN CONTENT ---
        VBox mainContent = new VBox(30);
        mainContent.setAlignment(Pos.CENTER);

        Text titleText = new Text("CHOOSE YOUR MASCOT");
        titleText.setFont(Font.font("Arial Black", FontWeight.BOLD, 42));
        titleText.setFill(Color.web("#a2db4e")); 

        statusLabel = new Label("Current Mascot: " + UserSession.getSelectedMascot().toUpperCase());
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 18));

        // --- MASCOT CARDS ---
        HBox cardRow = new HBox(40);
        cardRow.setAlignment(Pos.CENTER);

        snakeBtn = new Button("SELECT");
        elephantBtn = new Button("SELECT");
        chameleonBtn = new Button("SELECT");

        snakeCard = createMascotCard("SNAKE", "/mascot/snake/egg_snake_idle.png", "snake", snakeBtn);
        elephantCard = createMascotCard("ELEPHANT", "/mascot/elephant/baby_elephant_idle.PNG", "elephant", elephantBtn);
        chameleonCard = createMascotCard("CHAMELEON", "/mascot/chameleon/baby_chameleon_idle.png", "chameleon", chameleonBtn);

        cardRow.getChildren().addAll(snakeCard, elephantCard, chameleonCard);
        
        mainContent.getChildren().addAll(titleText, statusLabel, cardRow);
        this.getChildren().addAll(mainContent, topToolbar);
        
        // Force the UI to check what is currently equipped on load
        System.out.println("[SHOP INIT] Screen Loaded. Active Mascot in Session: " + UserSession.getSelectedMascot());
        updateVisualFeedback(); 
    }

    private VBox createMascotCard(String name, String imagePath, String mascotId, Button selectBtn) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setCursor(Cursor.HAND); 
        
        // --- THE MAGIC: REUSABLE EQUIP ACTION ---
        // We define the equip logic once, so we can attach it to both the Card AND the Button
        Runnable equipAction = () -> {
            System.out.println("\n[SHOP EVENT] User clicked to equip: " + mascotId);
            
            UserSession.setSelectedMascot(mascotId); 
            System.out.println("[SHOP EVENT] UserSession memory now holds: " + UserSession.getSelectedMascot());
            
            statusLabel.setText("Mascot updated to: " + name + "!");
            statusLabel.setTextFill(Color.web("#a2db4e"));
            
            updateVisualFeedback(); 
        };

        // Attach action to BOTH the card background and the button itself!
        card.setOnMouseClicked(e -> {
            System.out.println("[SHOP EVENT] Card background clicked!");
            equipAction.run();
        });
        
        selectBtn.setOnAction(e -> {
            System.out.println("[SHOP EVENT] Select button clicked!");
            equipAction.run();
            e.consume(); // Prevents the click from triggering the card background twice
        });

        // Hover logic
        card.setOnMouseEntered(e -> {
            if (!UserSession.getSelectedMascot().equalsIgnoreCase(mascotId)) {
                card.setTranslateY(-10);
                card.setStyle("-fx-background-color: rgba(40, 70, 40, 0.9); -fx-background-radius: 15; -fx-border-color: #a2db4e; -fx-border-radius: 15; -fx-border-width: 3;");
            }
        });
        
        card.setOnMouseExited(e -> {
            if (!UserSession.getSelectedMascot().equalsIgnoreCase(mascotId)) {
                card.setTranslateY(0);
                card.setStyle("-fx-background-color: rgba(30, 50, 30, 0.8); -fx-background-radius: 15; -fx-border-color: #555; -fx-border-radius: 15; -fx-border-width: 3;");
            }
        });

        try {
            ImageView mascotImg = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
            mascotImg.setFitWidth(150);
            mascotImg.setPreserveRatio(true);
            card.getChildren().add(mascotImg);
        } catch (Exception e) {
            Label error = new Label("Image Missing");
            error.setTextFill(Color.RED);
            card.getChildren().add(error);
        }

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial Black", 20));
        nameLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(nameLabel, selectBtn);
        return card;
    }

    private void updateVisualFeedback() {
        String active = UserSession.getSelectedMascot().toLowerCase();
        System.out.println("[SHOP UI] Refreshing interface. Highlighting: " + active);
        
        resetCard(snakeCard, snakeBtn);
        resetCard(elephantCard, elephantBtn);
        resetCard(chameleonCard, chameleonBtn);

        if (active.equals("snake")) highlightCard(snakeCard, snakeBtn);
        else if (active.equals("elephant")) highlightCard(elephantCard, elephantBtn);
        else if (active.equals("chameleon")) highlightCard(chameleonCard, chameleonBtn);
    }

    private void resetCard(VBox card, Button btn) {
        card.setTranslateY(0);
        card.setStyle("-fx-background-color: rgba(30, 50, 30, 0.8); -fx-background-radius: 15; -fx-border-color: #555; -fx-border-radius: 15; -fx-border-width: 3; -fx-effect: none;");
        
        btn.setText("SELECT");
        btn.setStyle("-fx-background-color: #a2db4e; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 20;");
        btn.setDisable(false);
    }

    private void highlightCard(VBox card, Button btn) {
        card.setTranslateY(-10); 
        card.setStyle("-fx-background-color: rgba(50, 90, 30, 0.95); -fx-background-radius: 15; -fx-border-color: #ffcc00; -fx-border-radius: 15; -fx-border-width: 4; -fx-effect: dropshadow(three-pass-box, #ffcc00, 15, 0.0, 0, 0);");
        
        btn.setText("EQUIPPED");
        btn.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 20;");
        btn.setDisable(true); 
    }
}