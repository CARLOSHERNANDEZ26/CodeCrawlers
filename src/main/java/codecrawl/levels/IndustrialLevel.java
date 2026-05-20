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

public class IndustrialLevel extends StackPane {
    
    private final MainFrame mainApp;
    private TextArea codeEditor;
    private ImageView mascotView;
    private Label consoleOutput;
    
    private Label dialogueText;
    private Label hintText;
    private Label stageTrackerLabel;
    private int currentStage = 0;

    // --- 10 REFACTORED HIGH-POLISH JAVA FUNDAMENTALS CHALLENGES ---
    private final ChallengeData[] challenges = {
        new ChallengeData("Print the phrase 'System Online' to the console in Java.", "Remember to use System.out.println() with a trailing semicolon.", "System.out.println(\"System Online\");"),
        new ChallengeData("Declare an integer variable named 'pressure' and set it exactly to 75.", "Whole numeric primitives use the primitive initialization keyword 'int'.", "int pressure = 75;"),
        new ChallengeData("Declare a double-precision floating variable named 'temperature' and set it to 98.6.", "Decimal points parameters require double data allocations.", "double temperature = 98.6;"),
        new ChallengeData("Create a boolean condition status named 'isLocked' initialized to true.", "Logical state flags use type 'boolean'.", "boolean isLocked = true;"),
        new ChallengeData("Write a conditional evaluation header checking if 'temperature' breaches value limits of 100.", "Do not write brackets, just provide the condition: if (temperature > 100)", "if(temperature>100)"),
        new ChallengeData("Write a evaluation block verifying if 'pressure' matches value 0 exactly using comparison signs.", "Equality operations evaluate via two equal symbols: pressure == 0", "pressure==0"),
        new ChallengeData("Write a while-loop condition header that continues processing as long as 'isLocked' stays true.", "Provide the processing block signature code: while (isLocked)", "while(isLocked)"),
        new ChallengeData("Declare an integer array data container named 'parts' allocated to hold exactly 5 entries.", "Instantiate structural array arrays: int[] parts = new int[5];", "int[]parts=newint[5];"),
        new ChallengeData("Define a standard public static void execution member method naming it 'shutdown'.", "Provide method access descriptors: public static void shutdown()", "publicstaticvoidshutdown()"),
        new ChallengeData("Instantiate a new constructor object tracking instance variable 'bot1' from template class 'Robot'.", "Instantiate new classes nodes loops variables: Robot bot1 = new Robot();", "Robotbot1=newRobot();")
    };

    public IndustrialLevel(MainFrame mainApp) {
        this.mainApp = mainApp;
        setupUI();
        loadCurrentChallenge();
    }

    private void setupUI() {
        VBox leftPane = new VBox(15);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #181a1b;"); 
        leftPane.setMinWidth(460); 

        Label editorLabel = new Label("FACTORY AUTOMATION TERMINAL HUB");
        editorLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Courier New';");

        stageTrackerLabel = new Label("STAGE: 1 / 10");
        stageTrackerLabel.setStyle("-fx-text-fill: #d1d5db; -fx-font-family: 'Courier New'; -fx-font-size: 14px;");

        codeEditor = new TextArea();
        codeEditor.setPromptText("// Feed compilation industrial firmware automation code blocks here...");
        codeEditor.setFont(Font.font("Consolas", 16)); 
        codeEditor.setStyle("-fx-control-inner-background: #090a0a; -fx-text-fill: #00d4ff; -fx-border-color: #374151; -fx-border-width: 2;");
        VBox.setVgrow(codeEditor, Priority.ALWAYS); 

        Button runButton = new Button("COMPILE LOGIC OPERATIONS FLAG");
        runButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 15px; -fx-font-family: 'Courier New'; -fx-cursor: hand;");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> evaluateSubmission()); 
        
        consoleOutput = new Label("Automation network synchronized. Awaiting instruction sequence executions...");
        consoleOutput.setStyle("-fx-text-fill: #6b7280; -fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        consoleOutput.setWrapText(true);

        leftPane.getChildren().addAll(editorLabel, stageTrackerLabel, codeEditor, runButton, consoleOutput);

        StackPane rightPane = new StackPane();
        rightPane.setMinWidth(600);

        try {
            // Links directly to IndustrialLevel.jpg in resources
            ImageView bgView = new ImageView(new Image(getClass().getResource("/world/IndustrialLevel.jpg").toExternalForm()));
            bgView.fitWidthProperty().bind(rightPane.widthProperty());
            bgView.fitHeightProperty().bind(rightPane.heightProperty());
            rightPane.getChildren().add(bgView);
        } catch (Exception e) {
            System.out.println("[FACTORY UI ERROR] Industrial backdrop texture files path mapping lost context connection variables.");
            rightPane.setStyle("-fx-background-color: #111827;");
        }

        VBox dialogueBubble = new VBox(8);
        dialogueBubble.setPadding(new Insets(15));
        dialogueBubble.setMaxWidth(420);
        dialogueBubble.setStyle("-fx-background-color: rgba(9, 10, 10, 0.94); -fx-background-radius: 12; -fx-border-color: #ff8c00; -fx-border-width: 2.5; -fx-border-radius: 10;");
        
        dialogueText = new Label();
        dialogueText.setWrapText(true);
        dialogueText.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        dialogueText.setTextFill(Color.WHITE);

        hintText = new Label();
        hintText.setWrapText(true);
        hintText.setFont(Font.font("Courier New", FontWeight.NORMAL, 13));
        hintText.setTextFill(Color.web("#00d4ff"));

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
        
        NavButton backBtn = new NavButton("← LEAVE SECTOR", this, () -> mainApp.showWorldSelection());
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
        dialogueText.setText("💬 CORE: " + challenge.prompt);
        hintText.setText("💡 HINT: " + challenge.hint);
        codeEditor.clear();
    }

    private void evaluateSubmission() {
        if (currentStage >= challenges.length) return;

        String normalizedInput = codeEditor.getText().replaceAll("\\s+", "");
        String normalizedAnswer = challenges[currentStage].expectedToken.replaceAll("\\s+", "");

        if (normalizedInput.isEmpty()) {
            consoleOutput.setText("❌ CORE PANIC: Operations processing pipeline cannot verify null code matrices inputs loops.");
            consoleOutput.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
            return;
        }

        if (normalizedInput.contains(normalizedAnswer)) {
            int prevLevel = UserSession.getLevel();
            UserSession.addXp(5); // Symmetrical 5 XP per problem set match
            int postLevel = UserSession.getLevel();
            currentStage++;

            if (postLevel > prevLevel) {
                refreshMascotVisuals();
                consoleOutput.setText("⭐ FIRMWARE LEVEL SYNCHRONIZED! Mascot triggered physical structure updates to Level " + postLevel + "!");
                consoleOutput.setStyle("-fx-text-fill: #ff8c00; -fx-font-weight: bold;");
            } else {
                consoleOutput.setText("✔ FIRMWARE INJECTED! Gained +5 XP industrial progression nodes parameters keys.");
                consoleOutput.setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
            }
            loadCurrentChallenge();
        } else {
            consoleOutput.setText("❌ CORE REJECTION: Structural loop logic patterns mismatched parameters tracking keys configurations variables.");
            consoleOutput.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    private void refreshMascotVisuals() {
        try {
            String updatedPath = MascotManager.getCurrentMascotPath(false);
            mascotView.setImage(new Image(getClass().getResource(updatedPath).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Firmware visual adjustment exception tracing: " + e.getMessage());
        }
    }

    private void triggerRoomVictory() {
        dialogueText.setText("🎉 CIRCUITS LOADED! Industrial computing logic processors fully operational!");
        hintText.setText("Mainframe evaluation loop trace complete. Return safely back to command base hubs mapping coordinates.");
        codeEditor.setDisable(true);
        stageTrackerLabel.setText("COMPLETED 10 / 10");
        consoleOutput.setText("SUCCESS: Hardware automation zone entirely clear. Exit links fully responsive.");
        consoleOutput.setStyle("-fx-text-fill: #ff8c00; -fx-font-weight: bold;");
    }

    private static class ChallengeData {
        final String prompt, hint, expectedToken;
        ChallengeData(String p, String h, String e) { prompt = p; hint = h; expectedToken = e; }
    }
}