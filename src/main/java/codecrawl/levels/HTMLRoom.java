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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HTMLRoom extends StackPane {
    
    private final MainFrame mainApp;
    private TextArea codeEditor;
    private ImageView mascotView;
    private Label consoleOutput;
    
    private Label dialogueText;
    private Label hintText;
    private Label stageTrackerLabel;
    private int currentStage = 0;

    private final ChallengeData[] challenges = {
        new ChallengeData("Write the opening document type declaration string for HTML5.", "It starts with '<!' and utilizes 'DOCTYPE html'.", "<!DOCTYPE html>"),
        new ChallengeData("Write the root opening element tag for an HTML document scope.", "The tag identifier keyword is simply 'html'.", "<html>"),
        new ChallengeData("Create the opening metadata block section tag container.", "This block header uses the keyword 'head'.", "<head>"),
        new ChallengeData("Write an opening webpage title metadata definition tag identifier.", "The tag header utilizes the literal term 'title'.", "<title>"),
        new ChallengeData("Open the primary structural document visual workspace container tag.", "All visible content elements are housed inside the 'body'.", "<body>"),
        new ChallengeData("Create an opening section heading level 1 header element container tag.", "The primary heading layout tag is 'h1'.", "<h1>"),
        new ChallengeData("Write the opening character layout layout paragraph element container tag.", "Paragraph formatting relies on the single letter 'p'.", "<p>"),
        new ChallengeData("Open an unordered bulleted inventory checklist element structural node wrapper.", "Unordered data collection listings use 'ul'.", "<ul>"),
        new ChallengeData("Write the list item itemization node structure wrapper opening tag.", "Individual bullet entries require the list parameter 'li'.", "<li>"),
        new ChallengeData("Write the absolute closing root termination tag string for your HTML script scope.", "Closing element declarations utilize forward slash parameters: </html>", "</html>")
    };

    public HTMLRoom(MainFrame mainApp) {
        this.mainApp = mainApp;
        setupUI();
        loadCurrentChallenge();
    }

    private void setupUI() {
        VBox leftPane = new VBox(15);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #22252a;"); 
        leftPane.setMinWidth(460); 

        Label editorLabel = new Label("CODE TERMINAL - HTML STRUCT");
        editorLabel.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Courier New';");

        stageTrackerLabel = new Label("STAGE: 1 / 10");
        stageTrackerLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-family: 'Courier New'; -fx-font-size: 14px;");

        codeEditor = new TextArea();
        codeEditor.setPromptText("");
        codeEditor.setFont(Font.font("Consolas", 16)); 
        codeEditor.setStyle("-fx-control-inner-background: #0f1115; -fx-text-fill: #38bdf8; -fx-border-color: #334155; -fx-border-width: 2;");
        VBox.setVgrow(codeEditor, Priority.ALWAYS); 

        Button runButton = new Button("COMPILE MARKUP PARSER");
        runButton.setStyle("-fx-background-color: #38bdf8; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 15px; -fx-font-family: 'Courier New'; -fx-cursor: hand;");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> evaluateSubmission()); 
        
        consoleOutput = new Label("Markup Pipeline Ready. Standby...");
        consoleOutput.setStyle("-fx-text-fill: #9ca3af; -fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        consoleOutput.setWrapText(true);

        leftPane.getChildren().addAll(editorLabel, stageTrackerLabel, codeEditor, runButton, consoleOutput);

        StackPane rightPane = new StackPane();
        rightPane.setMinWidth(600);

        try {
            // Force downsampling to a standard 1280x720 frame to fix GPU/Prism compilation errors
            Image img = new Image(getClass().getResource("/world/HTMLroom.png").toExternalForm(), 1280, 720, false, true);
            if (img.isError()) {
                throw new Exception(img.getException());
            }
            ImageView bgView = new ImageView(img);
            bgView.fitWidthProperty().bind(rightPane.widthProperty());
            bgView.fitHeightProperty().bind(rightPane.heightProperty());
            rightPane.getChildren().add(bgView);
        } catch (Exception e) {
            System.out.println("[HTML UI ERROR] Missing room asset background map pointer context. Using solid fallback.");
            rightPane.setStyle("-fx-background-color: #0b0f19;");
        }

        VBox dialogueBubble = new VBox(8);
        dialogueBubble.setPadding(new Insets(15));
        dialogueBubble.setMaxWidth(420);
        dialogueBubble.setStyle("-fx-background-color: rgba(11, 15, 25, 0.94); -fx-background-radius: 12; -fx-border-color: #38bdf8; -fx-border-width: 2.5; -fx-border-radius: 10;");
        
        dialogueText = new Label();
        dialogueText.setWrapText(true);
        dialogueText.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        dialogueText.setTextFill(Color.WHITE);

        hintText = new Label();
        hintText.setWrapText(true);
        hintText.setFont(Font.font("Courier New", FontWeight.NORMAL, 13));
        hintText.setTextFill(Color.web("#38bdf8"));

        dialogueBubble.getChildren().addAll(dialogueText, hintText);

        String mascotPath = MascotManager.getCurrentMascotPath(false); 
        try {
            mascotView = new ImageView(new Image(getClass().getResource(mascotPath).toExternalForm()));
            mascotView.setFitWidth(150);
            mascotView.setPreserveRatio(true);
        } catch (Exception e) {
            mascotView = new ImageView(); 
            mascotView.setFitWidth(150);  
            mascotView.setFitHeight(150); 
        }

        VBox centerCharacterCluster = new VBox(20, dialogueBubble, mascotView);
        centerCharacterCluster.setAlignment(Pos.CENTER);
        centerCharacterCluster.setMaxSize(450, 400);

        rightPane.getChildren().add(centerCharacterCluster);
        StackPane.setAlignment(centerCharacterCluster, Pos.CENTER);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.38); 
        
        NavButton backBtn = new NavButton("← LEAVE ROOM", this, () -> mainApp.showHouse());
        HBox toolbar = new HBox(15, backBtn, new MuteButton());
        toolbar.setAlignment(Pos.CENTER_RIGHT); 
        toolbar.setPickOnBounds(false); 
        
        leftPane.getChildren().add(0, toolbar); 
        this.getChildren().add(splitPane);
    }

    private void loadCurrentChallenge() {
        if (currentStage >= challenges.length) {
            triggerRoomVictory();
            return;
        }
        ChallengeData challenge = challenges[currentStage];
        stageTrackerLabel.setText("STAGE PROGRESS: " + (currentStage + 1) + " / " + challenges.length);
        dialogueText.setText("💬 STRUCT: " + challenge.prompt);
        hintText.setText("💡 HINT: " + challenge.hint);
        codeEditor.clear();
    }

    private void evaluateSubmission() {
        if (currentStage >= challenges.length) return;

        String normalizedInput = codeEditor.getText().replaceAll("\\s+", "");
        String normalizedAnswer = challenges[currentStage].expectedToken.replaceAll("\\s+", "");

        if (normalizedInput.isEmpty()) {
            consoleOutput.setText("❌ RENDER ERROR: Empty code input context frame.");
            consoleOutput.setStyle("-fx-text-fill: #f43f5e; -fx-font-weight: bold;");
            return;
        }

        if (normalizedInput.contains(normalizedAnswer)) {
            int prevLevel = UserSession.getLevel();
            UserSession.addXp(5);
            int postLevel = UserSession.getLevel();
            currentStage++;

            if (postLevel > prevLevel) {
                refreshMascotVisuals();
                consoleOutput.setText("⭐ MARGIN EVOLUTION MATCH! Mascot shifted layout form to Level " + postLevel + "!");
                consoleOutput.setStyle("-fx-text-fill: #ffcc00; -fx-font-weight: bold;");
            } else {
                consoleOutput.setText("✔ ELEMENT LOADED! Gained +5 XP structure point parameters.");
                consoleOutput.setStyle("-fx-text-fill: #4ade80; -fx-font-weight: bold;");
            }
            loadCurrentChallenge();
        } else {
            consoleOutput.setText("❌ PARSING EXCEPTION: DOM Node rejected. Verify character tags matching answer requirements.");
            consoleOutput.setStyle("-fx-text-fill: #f43f5e;");
        }
    }

    private void refreshMascotVisuals() {
        try {
            String updatedPath = MascotManager.getCurrentMascotPath(false);
            mascotView.setImage(new Image(getClass().getResource(updatedPath).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Redraw failure: " + e.getMessage());
        }
    }

    private void triggerRoomVictory() {
        dialogueText.setText("🎉 TREE COMPILED! All structural webpage layouts are fully functional!");
        hintText.setText("Mission complete. Return back to sector map processing hubs.");
        codeEditor.setDisable(true);
        stageTrackerLabel.setText("COMPLETED 10 / 10");
        consoleOutput.setText("SUCCESS: Node space cleared cleanly. Safe session exit available.");
        consoleOutput.setStyle("-fx-text-fill: #ffcc00; -fx-font-weight: bold;");
    }

    private static class ChallengeData {
        final String prompt, hint, expectedToken;
        ChallengeData(String p, String h, String e) { prompt = p; hint = h; expectedToken = e; }
    }
}