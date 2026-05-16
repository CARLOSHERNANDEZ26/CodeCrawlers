package codecrawl.levels;

import codecrawl.core.MainFrame;
import codecrawl.engine.MascotManager; 
import codecrawl.core.UserSession;    
import codecrawl.ui.MuteButton;
import codecrawl.ui.NavButton; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class HTMLRoom extends StackPane { 
    private int currentTask = 0;
    private Text unitTitle, instructionText, progressText;
    private TextArea codeEditor;
    private MainFrame mainApp;

    private final String[] tasks = {
        "Create the <!DOCTYPE html> declaration.", "Add the opening <html> tag.",
        "Add a <head> section for metadata.", "Create a <title> for your webpage.",
        "Open the <body> tag.", "Create a main heading using <h1>.",
        "Write a paragraph using <p>.", "Add a sub-heading with <h2>.",
        "Create an unordered list with <ul>.", "Add list items using <li>.",
        "Insert an image with <img>.", "Create a hyperlink with <a>.",
        "Add a <div> container.", "Use <strong> for bold text.",
        "Use <em> for emphasis.", "Create a <table> element.",
        "Add a table row with <tr>.", "Add table headers with <th>.",
        "Add table data with <td>.", "Close the <html> tag to finish."
    };

    public HTMLRoom(MainFrame mainApp) {
        this.mainApp = mainApp;

        HBox mainLayout = new HBox(30);
        mainLayout.setStyle("-fx-background-color: #1a1c1e;");
        mainLayout.setPadding(new Insets(40));
        mainLayout.setPickOnBounds(false);

        // --- LEFT SIDEBAR ---
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(300);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-border-color: #444; -fx-border-width: 2;");
        
        Label feedLabel = new Label("[ HTML_FEED_01 ]");
        feedLabel.setTextFill(Color.web("#38bdf8"));
        feedLabel.setFont(Font.font("Courier New", 14));
        sidebar.getChildren().add(feedLabel);

        // --- RIGHT CONTENT ---
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_LEFT);

        // THE NEW PREVIEW: Dynamic Mascot (Clean dark background)
        StackPane mascotPreview = new StackPane();
        mascotPreview.setPrefHeight(180);
        mascotPreview.setStyle("-fx-border-color: #38bdf8; -fx-border-width: 2; -fx-background-color: #0b0f19;"); // Deep space blue/black
        
        try {
            // Load the idle, evolving mascot based on UserSession!
            String mascotPath = MascotManager.getCurrentMascotPath(true); // true = idle state
            ImageView mascotView = new ImageView(new Image(getClass().getResource(mascotPath).toExternalForm()));
            mascotView.setFitHeight(140); // Scaled up slightly since it's the focal point now
            mascotView.setPreserveRatio(true);
            
            mascotPreview.getChildren().add(mascotView);
            StackPane.setAlignment(mascotView, Pos.CENTER);
        } catch (Exception e) {
            System.err.println("Could not load Mascot image.");
        }

        unitTitle = new Text("UNIT_01: STRUCTURE_BOOT");
        unitTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        unitTitle.setFill(Color.web("#38bdf8"));

        instructionText = new Text(tasks[0]);
        instructionText.setFont(Font.font("Arial", 18));
        instructionText.setFill(Color.WHITE);

        codeEditor = new TextArea("<html>\n  \n</html>");
        codeEditor.setPrefHeight(250); 
        codeEditor.setStyle("-fx-control-inner-background: #000; -fx-text-fill: #38bdf8; -fx-font-family: 'Consolas'; -fx-font-size: 18; -fx-border-color: #333;");

        Button executeBtn = new Button("EXECUTECOMMAND >");
        executeBtn.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: black; -fx-font-family: 'Courier New'; -fx-font-weight: bold; -fx-font-size: 20; -fx-cursor: hand;");
        executeBtn.setPadding(new Insets(15, 30, 15, 30));

        progressText = new Text("UNIT_PROGRESS: 1/20 | Lvl " + UserSession.getLevel() + " - XP: " + UserSession.getXp() + "/100");
        progressText.setFill(Color.web("#555"));
        progressText.setFont(Font.font("Courier New", 14));

        executeBtn.setOnAction(e -> handleNext());

        content.getChildren().addAll(mascotPreview, unitTitle, instructionText, codeEditor, executeBtn, progressText);
        mainLayout.getChildren().addAll(sidebar, content);

        // --- TOP TOOLBAR ---
        NavButton backBtn = new NavButton("← LEAVE ROOM", this, () -> mainApp.showHouse());
        HBox toolbar = new HBox(15, backBtn, new MuteButton());
        toolbar.setAlignment(Pos.TOP_RIGHT);
        toolbar.setPadding(new Insets(20));
        toolbar.setPickOnBounds(false);

        this.getChildren().addAll(mainLayout, toolbar);
        StackPane.setAlignment(toolbar, Pos.TOP_RIGHT);
    }

    private void handleNext() {
        if (currentTask < tasks.length - 1) {
            currentTask++;
            
            // THE REWARD: 5 XP per HTML task
            UserSession.addXp(5); 
            
            unitTitle.setText("UNIT_" + String.format("%02d", currentTask + 1) + ": HTML_CORE");
            instructionText.setText(tasks[currentTask]);
            
            // Update HUD text dynamically
            progressText.setText("UNIT_PROGRESS: " + (currentTask + 1) + "/20 | Lvl " + UserSession.getLevel() + " - XP: " + UserSession.getXp() + "/100");
            progressText.setFill(Color.web("#89C765")); // Flash green when gaining XP
        } else { 
            mainApp.showHouse(); 
        }
    }
}