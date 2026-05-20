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

public class DatabaseRoom extends StackPane {
    
    private final MainFrame mainApp;
    private TextArea codeEditor;
    private ImageView mascotView;
    private Label consoleOutput;
    
    private Label dialogueText;
    private Label hintText;
    private Label stageTrackerLabel;
    private int currentStage = 0;

    // --- 10 CORE DYNAMIC SQL CHALLENGES ---
    private final ChallengeData[] challenges = {
        new ChallengeData("Write a query to create a table named 'logs' with an integer column named 'id'.", "Use CREATE TABLE logs (id INT);", "CREATETABLElogs"),
        new ChallengeData("Modify your 'logs' table to append a string text column named 'code' that holds up to 50 characters.", "Use ALTER TABLE logs ADD COLUMN code VARCHAR(50);", "ALTERTABLElogsADD"),
        new ChallengeData("Insert a row into 'logs' setting the 'id' to 1 and 'code' to 'SYS_INIT'.", "Use INSERT INTO logs (id, code) VALUES (1, 'SYS_INIT');", "INSERTINTOlogs"),
        new ChallengeData("Write a query to request and fetch all rows and column entries sitting inside the 'logs' table.", "Utilize the global asterisk selector: SELECT * FROM logs;", "SELECT*FROMlogs"),
        new ChallengeData("Fetch only the data under the 'code' column without pulling the ID rows.", "Target the explicit attribute: SELECT code FROM logs;", "SELECTcodeFROMlogs"),
        new ChallengeData("Filter your selection to pull records where the 'id' column is exactly equal to 1.", "Append a query filter clause parameter: WHERE id = 1", "WHEREid=1"),
        new ChallengeData("Sort your selected 'logs' entries alphabetically based on their 'code' values.", "Use sorting clauses: ORDER BY code ASC;", "ORDERBYcode"),
        new ChallengeData("Update the 'code' text entry to read 'CRITICAL' for rows matching an ID of 1.", "Use UPDATE logs SET code = 'CRITICAL' WHERE id = 1;", "UPDATElogsSET"),
        new ChallengeData("Wipe out and delete any row inside 'logs' that contains an ID equal to 1.", "Use DELETE FROM logs WHERE id = 1;", "DELETEFROMlogs"),
        new ChallengeData("Completely delete the entire structural definition of the 'logs' table from the database.", "Use DROP TABLE logs;", "DROPTABLElogs")
    };

    public DatabaseRoom(MainFrame mainApp) {
        this.mainApp = mainApp;
        setupUI();
        loadCurrentChallenge();
    }

    private void setupUI() {
        // Left Column Workspace
        VBox leftPane = new VBox(15);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #201a15;"); 
        leftPane.setMinWidth(460); // Strict layout split safety guard

        Label editorLabel = new Label("SQL RELATIONAL ENGINE TERMINAL");
        editorLabel.setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Courier New';");

        stageTrackerLabel = new Label("STAGE: 1 / 10");
        stageTrackerLabel.setStyle("-fx-text-fill: #e5e7eb; -fx-font-family: 'Courier New'; -fx-font-size: 14px;");

        codeEditor = new TextArea();
        codeEditor.setPromptText("-- Enter Structured Query Language (SQL) statements...");
        codeEditor.setFont(Font.font("Consolas", 16)); 
        codeEditor.setStyle("-fx-control-inner-background: #0c0906; -fx-text-fill: #f59e0b; -fx-border-color: #78350f; -fx-border-width: 2;");
        VBox.setVgrow(codeEditor, Priority.ALWAYS); 

        Button runButton = new Button("EXECUTE SCHEMATIC QUERY");
        runButton.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 15px; -fx-font-family: 'Courier New'; -fx-cursor: hand;");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> evaluateSubmission()); 
        
        consoleOutput = new Label("DBMS Connector Active. Standby for query parameters execution...");
        consoleOutput.setStyle("-fx-text-fill: #9ca3af; -fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        consoleOutput.setWrapText(true);

        leftPane.getChildren().addAll(editorLabel, stageTrackerLabel, codeEditor, runButton, consoleOutput);

        // Right Column Graphic Frame
        StackPane rightPane = new StackPane();
        rightPane.setMinWidth(600);

        try {
            // Links directly to DatabaseRoom.jpg in resources
            ImageView bgView = new ImageView(new Image(getClass().getResource("/world/DatabaseRoom.jpg").toExternalForm()));
            bgView.fitWidthProperty().bind(rightPane.widthProperty());
            bgView.fitHeightProperty().bind(rightPane.heightProperty());
            rightPane.getChildren().add(bgView);
        } catch (Exception e) {
            System.out.println("[DB UI ERROR] Target backdrop file mapping failed initialization.");
            rightPane.setStyle("-fx-background-color: #1c1917;");
        }

        VBox dialogueBubble = new VBox(8);
        dialogueBubble.setPadding(new Insets(15));
        dialogueBubble.setMaxWidth(420);
        dialogueBubble.setStyle("-fx-background-color: rgba(12, 9, 6, 0.94); -fx-background-radius: 12; -fx-border-color: #f59e0b; -fx-border-width: 2.5; -fx-border-radius: 10;");
        
        dialogueText = new Label();
        dialogueText.setWrapText(true);
        dialogueText.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        dialogueText.setTextFill(Color.WHITE);

        hintText = new Label();
        hintText.setWrapText(true);
        hintText.setFont(Font.font("Courier New", FontWeight.NORMAL, 13));
        hintText.setTextFill(Color.web("#fbbf24"));

        dialogueBubble.getChildren().addAll(dialogueText, hintText);

        String mascotPath = MascotManager.getCurrentMascotPath(false); 
        try {
            mascotView = new ImageView(new Image(getClass().getResource(mascotPath).toExternalForm()));
            mascotView.setFitWidth(150);
            mascotView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("[UI WARNING] Mascot asset missing. Using invisible structural placeholder.");
            mascotView = new ImageView(); // Creates the empty view
            mascotView.setFitWidth(150);  // Locks the width boundary
            mascotView.setFitHeight(150); // Locks the height boundary to prop up the dialogue bubble
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
        dialogueText.setText("💬 QUERY: " + challenge.prompt);
        hintText.setText("💡 HINT: " + challenge.hint);
        codeEditor.clear();
    }

    private void evaluateSubmission() {
        if (currentStage >= challenges.length) return;

        String normalizedInput = codeEditor.getText().replaceAll("\\s+", "").toLowerCase();
        String normalizedAnswer = challenges[currentStage].expectedToken.replaceAll("\\s+", "").toLowerCase();

        if (normalizedInput.isEmpty()) {
            consoleOutput.setText("❌ SCHEMA EXCEPTION: Cannot compile blank transactions buffer.");
            consoleOutput.setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold;");
            return;
        }

        if (normalizedInput.contains(normalizedAnswer)) {
            int prevLevel = UserSession.getLevel();
            UserSession.addXp(5); // Grants 5 XP per data query matched
            int postLevel = UserSession.getLevel();
            currentStage++;

            if (postLevel > prevLevel) {
                refreshMascotVisuals();
                consoleOutput.setText("⭐ DATABASE EVOLUTION STEP! Mascot optimized storage form to Level " + postLevel + "!");
                consoleOutput.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
            } else {
                consoleOutput.setText("✔ TRANSACTION COMMITTED! Gained +5 XP database metrics indexes.");
                consoleOutput.setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
            }
            loadCurrentChallenge();
        } else {
            consoleOutput.setText("❌ TRANSACTION ROLLBACK: Query syntax rejected. Check keywords structures matching hints requirements.");
            consoleOutput.setStyle("-fx-text-fill: #f87171;");
        }
    }

    private void refreshMascotVisuals() {
        try {
            String updatedPath = MascotManager.getCurrentMascotPath(false);
            mascotView.setImage(new Image(getClass().getResource(updatedPath).toExternalForm()));
        } catch (Exception e) {
            System.err.println("DB graphic reload crash: " + e.getMessage());
        }
    }

    private void triggerRoomVictory() {
        dialogueText.setText("🎉 SCHEMATIC CLEAR! All relational query ledgers compiled flawlessly!");
        hintText.setText("Mission complete. Return back to structural map processing hubs.");
        codeEditor.setDisable(true);
        stageTrackerLabel.setText("COMPLETED 10 / 10");
        consoleOutput.setText("SUCCESS: SQL cluster processing verified. Navigation routes open.");
        consoleOutput.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
    }

    private static class ChallengeData {
        final String prompt, hint, expectedToken;
        ChallengeData(String p, String h, String e) { prompt = p; hint = h; expectedToken = e; }
    }
}