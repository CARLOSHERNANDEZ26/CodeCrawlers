package codecrawl.levels;

import codecrawl.core.MainFrame;
import codecrawl.ui.MuteButton;
import codecrawl.ui.NavButton; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

        // 1. CLEAN BACKGROUND (No more stretched icons!)
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #2b1d3d, #120b1c);");

        // 2. THE TOOLBAR (WITH CLICK FIX)
        NavButton leaveMapBtn = new NavButton("← LEAVE HOUSE", this, () -> mainApp.showWorldSelection());
        HBox toolbar = new HBox(15, leaveMapBtn, new MuteButton());
        toolbar.setAlignment(Pos.TOP_RIGHT);
        toolbar.setPadding(new Insets(20));
        
        // CRITICAL FIX: Allows clicks to pass through the empty space of the toolbar to the buttons below!
        toolbar.setPickOnBounds(false); 

        // 3. LAYOUT CONTAINER
        VBox layout = new VBox(20); // Tighter spacing
        layout.setAlignment(Pos.CENTER);
        
        // CRITICAL FIX: Ensure the layout also lets clicks pass through if it overlaps
        layout.setPickOnBounds(false);

        // 4. USE THE HOUSE ICON AS A LOGO INSTEAD OF A BACKGROUND
        try {
            ImageView houseIcon = new ImageView(new Image(getClass().getResource("/house.png").toExternalForm()));
            houseIcon.setFitWidth(180);
            houseIcon.setPreserveRatio(true);
            layout.getChildren().add(houseIcon); // Add logo to the top of the menu
        } catch (Exception e) {
            System.err.println("House icon missing!");
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
        
        this.getChildren().addAll(layout, toolbar);
        StackPane.setAlignment(toolbar, Pos.TOP_RIGHT);
    }

    private Button createStyledButton(String text, String hexColor) {
        Button btn = new Button(text);
        btn.setPrefSize(200, 80);
        btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: white; " +
                     "-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 12; " +
                     "-fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 3; -fx-border-radius: 10;");
        btn.setCursor(javafx.scene.Cursor.HAND);
        
        // Add a slight hover effect to let the user know it is clickable
        btn.setOnMouseEntered(e -> btn.setTranslateY(-4));
        btn.setOnMouseExited(e -> btn.setTranslateY(0));
        
        return btn;
    }
}