package codecrawl.levels;

import codecrawl.ui.NavButton;
import codecrawl.core.MainFrame;
import codecrawl.ui.MuteButton; // New Import
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DatabaseRoom extends StackPane {
    private int currentTask = 0;
    private Text taskTitle;
    private Text taskDescription;
    private MainFrame mainApp;

    private final String[] tasks = {
        "1. CREATE TABLE users.", "2. Define PRIMARY KEY.",
        "3. Add VARCHAR column.", "4. Add email column.",
        "5. INSERT first user.", "6. Add 5 more rows.",
        "7. SELECT * from users.", "8. SELECT specific columns.",
        "9. Use WHERE clause.", "10. Use AND operator.",
        "11. Use OR operator.", "12. ORDER BY name.",
        "13. LIMIT results.", "14. UPDATE user email.",
        "15. DELETE a record.", "16. COUNT(*) rows.",
        "17. Use SUM() function.", "18. GROUP BY category.",
        "19. Use INNER JOIN.", "20. DROP TABLE users."
    };

    public DatabaseRoom(MainFrame mainApp) {
        this.mainApp = mainApp;
        setupBackground();

        NavButton backBtn = new NavButton("← LEAVE ROOM", this, () -> mainApp.showHouse());

     // Combine the back button and mute button into a single horizontal bar
     HBox toolbar = new HBox(15, backBtn, new MuteButton());
     toolbar.setAlignment(Pos.TOP_RIGHT);
     toolbar.setPadding(new Insets(20));
     toolbar.setPickOnBounds(false);

        VBox hud = new VBox(25);
        hud.setAlignment(Pos.CENTER);
        hud.setMaxSize(800, 450);
        hud.setStyle("-fx-background-color: rgba(20, 20, 20, 0.85); -fx-background-radius: 20; -fx-border-color: #f59e0b; -fx-border-width: 3;");

        taskTitle = new Text("SQL TERMINAL - TASK 1/20");
        taskTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        taskTitle.setFill(Color.web("#f59e0b"));

        taskDescription = new Text(tasks[0]);
        taskDescription.setFont(Font.font("Courier New", 20));
        taskDescription.setFill(Color.WHITE);
        taskDescription.setWrappingWidth(700);

        Button nextBtn = new Button("RUN QUERY");
        nextBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18; -fx-background-radius: 10; -fx-cursor: hand;");
        nextBtn.setPrefSize(250, 60);
        
        nextBtn.setOnAction(e -> {
            if (currentTask < tasks.length - 1) {
                currentTask++;
                taskTitle.setText("SQL TERMINAL - TASK " + (currentTask + 1) + "/20");
                taskDescription.setText(tasks[currentTask]);
            } else {
                mainApp.showHouse();
            }
        });

        hud.getChildren().addAll(taskTitle, taskDescription, nextBtn);
        
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
        } catch (Exception e) { this.setStyle("-fx-background-color: #1a1a1a;"); }
    }
}