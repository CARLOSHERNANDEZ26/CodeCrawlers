package codecrawl.levels;

import codecrawl.core.MainFrame;
import codecrawl.ui.MuteButton; 
import codecrawl.ui.NavButton; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CSSRoom extends StackPane {
    private int currentTask = 0;
    private Text taskTitle;
    private Text taskDescription;
    private MainFrame mainApp;

    private final String[] tasks = {
        "1. Select <body> and set background-color.", "2. Change font-family.",
        "3. Center headings with text-align.", "4. Change <h1> color.",
        "5. Adjust <p> font-size.", "6. Add a solid border.",
        "7. Apply padding.", "8. Apply margin.",
        "9. Set width and height.", "10. Change list-style.",
        "11. Create a .class selector.", "12. Create an #id selector.",
        "13. Remove link underlines.", "14. Add :hover effects.",
        "15. Set cursor: pointer.", "16. Enable display: flex.",
        "17. Use justify-content.", "18. Add a box-shadow.",
        "19. Set border-radius.", "20. Adjust opacity."
    };

    public CSSRoom(MainFrame mainApp) {
        this.mainApp = mainApp;
        setupBackground();

        // ══════════════════════════════════════════════════════════════════════
        // NEW NAVIGATION & AUDIO TOOLBAR
        // ══════════════════════════════════════════════════════════════════════
        NavButton backBtn = new NavButton("← LEAVE ROOM", this, () -> mainApp.showHouse());
        
        HBox toolbar = new HBox(15, backBtn, new MuteButton());
        toolbar.setAlignment(Pos.TOP_RIGHT);
        toolbar.setPadding(new Insets(20));
        toolbar.setPickOnBounds(false);

        // ══════════════════════════════════════════════════════════════════════
        // HEADS-UP DISPLAY (HUD) PANEL
        // ══════════════════════════════════════════════════════════════════════
        VBox hud = new VBox(25);
        hud.setAlignment(Pos.CENTER);
        hud.setMaxSize(800, 450);
        hud.setStyle("-fx-background-color: rgba(15, 23, 42, 0.85); -fx-background-radius: 20; -fx-border-color: #3b82f6; -fx-border-width: 3;");

        taskTitle = new Text("CSS STYLING - TASK 1/20");
        taskTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        taskTitle.setFill(Color.web("#3b82f6"));

        taskDescription = new Text(tasks[0]);
        taskDescription.setFont(Font.font("Courier New", 20));
        taskDescription.setFill(Color.WHITE);
        taskDescription.setWrappingWidth(700);

        Button nextBtn = new Button("APPLY STYLE");
        nextBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius: 10; -fx-cursor: hand;");
        nextBtn.setPrefSize(250, 60);
        
        nextBtn.setOnAction(e -> {
            if (currentTask < tasks.length - 1) {
                currentTask++;
                taskTitle.setText("CSS STYLING - TASK " + (currentTask + 1) + "/20");
                taskDescription.setText(tasks[currentTask]);
            } else {
                mainApp.showHouse();
            }
        });

        hud.getChildren().addAll(taskTitle, taskDescription, nextBtn);
        
        // ══════════════════════════════════════════════════════════════════════
        // SCENE ASSEMBLY
        // ══════════════════════════════════════════════════════════════════════
        // Adding hud first, then toolbar so the buttons remain accessible on top layers
        this.getChildren().addAll(hud, toolbar);
        StackPane.setAlignment(toolbar, Pos.TOP_RIGHT);
    }

    private void setupBackground() {
        try {
            BackgroundImage bg = new BackgroundImage(
                new Image(getClass().getResource("/island background.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
            );
            this.setBackground(new Background(bg));
        } catch (Exception e) { this.setStyle("-fx-background-color: #1e293b;"); }
    }
}