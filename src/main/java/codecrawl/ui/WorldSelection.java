package codecrawl.ui;

import codecrawl.core.MainFrame;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

public class WorldSelection extends StackPane {

    private MainFrame mainApp;
    private Label hoverLabel; // NEW: To display the world name

    public WorldSelection(MainFrame mainApp) {
        this.mainApp = mainApp;

        // 1. SET THE SKY BACKGROUND
        try {
            var resource = getClass().getResource("/world/island background.png"); // Updated path
            if (resource != null) {
                BackgroundImage sky = new BackgroundImage(
                    new Image(resource.toExternalForm()),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, 
                    new BackgroundSize(100, 100, true, true, true, true)
                );
                this.setBackground(new Background(sky));
            }
        } catch (Exception e) {}

        // --- NEW: HOVER LABEL SETUP ---
        hoverLabel = new Label("SELECT A DESTINATION");
        hoverLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 28));
        hoverLabel.setTextFill(Color.WHITE);
        hoverLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0.0, 0, 3);");
        StackPane.setAlignment(hoverLabel, Pos.TOP_CENTER);
        StackPane.setMargin(hoverLabel, new Insets(40, 0, 0, 0));

        // 2. CREATE THE ISLAND LAYER
        HBox islandLayer = new HBox(0); 
        islandLayer.setAlignment(Pos.CENTER);

        // Pass the name to the helper method so it knows what to display on hover
        ImageView industry = createFloatingIsland("industry.png", 380, "INDUSTRIAL SECTOR", e -> mainApp.showIndustrialLevel());
        ImageView jungle = createFloatingIsland("jungle.png", 300, "JUNGLE LEVEL", e -> mainApp.showJungleLevel());
        ImageView house = createFloatingIsland("house.png", 380, "THE LEARNING HOUSE", e -> mainApp.showHouse());

        islandLayer.getChildren().addAll(industry, jungle, house);
        islandLayer.setTranslateY(20); 

        // 3. BACK BUTTON
        Button backBtn = new Button("← BACK TO MENU");
        backBtn.setCursor(Cursor.HAND);
        backBtn.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black; -fx-font-family: 'Courier New'; -fx-font-weight: 900; -fx-font-size: 14px; -fx-padding: 10 20; -fx-border-width: 0 0 4 0; -fx-border-color: #b38f00;");
        backBtn.setOnAction(e -> mainApp.showHomeScreen());
        StackPane.setAlignment(backBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(backBtn, new Insets(0, 30, 30, 0));

        // 4. ADD TO STACKPANE
        this.getChildren().addAll(islandLayer, hoverLabel, backBtn);
    }

    private ImageView createFloatingIsland(String fileName, double width, String worldName, javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> event) {
        ImageView iv = new ImageView();
        try {
            var resource = getClass().getResource("/" + fileName);
            if (resource != null) iv.setImage(new Image(resource.toExternalForm()));
        } catch (Exception e) {}
        
        iv.setFitWidth(width);
        iv.setPreserveRatio(true);
        iv.setCursor(Cursor.HAND);

        TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(0.3), iv);

        iv.setOnMouseEntered(e -> {
            floatAnim.stop(); 
            floatAnim.setToY(-25); 
            floatAnim.play();
            hoverLabel.setText(worldName); // Update text on hover
            hoverLabel.setTextFill(Color.web("#a2db4e")); // Turn green when targeting
        });

        iv.setOnMouseExited(e -> {
            floatAnim.stop();
            floatAnim.setToY(0); 
            floatAnim.play();
            hoverLabel.setText("SELECT A DESTINATION"); // Reset
            hoverLabel.setTextFill(Color.WHITE);
        });

        iv.setOnMouseClicked(event);
        return iv;
    }
}