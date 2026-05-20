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

public class JungleLevel extends StackPane {
    
    private final MainFrame mainFrame;
    private TextArea codeEditor;
    private ImageView mascotView;
    private Label consoleOutput;
    
    // --- DIALOGUE & QUESTION STATE CONTROLLERS ---
    private Label dialogueText;
    private Label hintText;
    private Label stageTrackerLabel;
    private int currentStage = 0;

    // --- 10 HARDCODED ARCHITECTURE CHALLENGES ---
    private final ChallengeData[] challenges = {
        new ChallengeData(
            "Print the phrase 'Jungle Run' to the console.",
            "Use System.out.println() with double quotes inside.",
            "System.out.println(\"Jungle Run\");"
        ),
        new ChallengeData(
            "Declare an integer variable named 'vines' and set it to 5.",
            "Whole numbers use the 'int' primitive data keyword type.",
            "int vines = 5;"
        ),
        new ChallengeData(
            "Create a boolean variable named 'hasMap' and set it to true.",
            "Booleans evaluate strictly to state tokens 'true' or 'false'.",
            "boolean hasMap = true;"
        ),
        new ChallengeData(
            "Declare a double variable named 'speed' and set it to 4.5.",
            "Decimal fractional numbers must utilize the type descriptor 'double'.",
            "double speed = 4.5;"
        ),
        new ChallengeData(
            "Create a String object named 'pet' containing the value 'Kyro'.",
            "String must start with a capital 'S' and values use double quotes.",
            "String pet = \"Kyro\";"
        ),
        new ChallengeData(
            "Write an if statement header evaluating if 'vines' is greater than 3.",
            "Do not include brackets, just write the evaluation block: if (vines > 3)",
            "if(vines>3)"
        ),
        new ChallengeData(
            "Increment your 'vines' variable by exactly 1 using postfix shorthand syntax.",
            "The increment operator uses two plus signs directly side-by-side.",
            "vines++;"
        ),
        new ChallengeData(
            "Compare if 'pet' matches 'Kyro' utilizing object equality evaluation methods.",
            "Never use == for Strings. Utilize the structural method lookup: pet.equals(\"Kyro\")",
            "pet.equals(\"Kyro\")"
        ),
        new ChallengeData(
            "Write a standard for-loop initialization header that runs exactly 3 times using tracking index 'i'.",
            "Structure it like: for (int i = 0; i < 3; i++) without trailing syntax closures.",
            "for(int i=0;i<3;i++)"
        ),
        new ChallengeData(
            "Invoke a member method sequence named 'escape()' on a local variable handle named 'kyro'.",
            "Access standard instance member routines via dot parameters.",
            "kyro.escape();"
        )
    };

    public JungleLevel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setupUI();
        loadCurrentChallenge();
    }

    private void setupUI() {
        // ==========================================
        // LEFT SIDE: CODE EDITOR FRAMEWORK
        // ==========================================
        VBox leftPane = new VBox(15);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #2b2b2b;"); 
        
        // ─── CRITICAL FIX: HARD LAYOUT BOUNDARY ───
        leftPane.setMinWidth(460); 

        Label editorLabel = new Label("CODE TERMINAL - JUNGLE DEV");
        editorLabel.setStyle("-fx-text-fill: #a2db4e; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Courier New';");

        stageTrackerLabel = new Label("STAGE: 1 / 10");
        stageTrackerLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-family: 'Courier New'; -fx-font-size: 14px;");

        codeEditor = new TextArea();
        codeEditor.setPromptText("// Feed compilation script parameters here...");
        codeEditor.setFont(Font.font("Consolas", 16)); 
        codeEditor.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: #a9b7c6; -fx-border-color: #444; -fx-border-width: 2;");
        VBox.setVgrow(codeEditor, Priority.ALWAYS); 

        Button runButton = new Button("EXECUTE CHALLENGE SCRIPT");
        runButton.setStyle("-fx-background-color: #a2db4e; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 15px; -fx-font-family: 'Courier New'; -fx-cursor: hand;");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> evaluateSubmission()); 
        
        consoleOutput = new Label("System Online. Awaiting code execution loop...");
        consoleOutput.setStyle("-fx-text-fill: #9ca3af; -fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        consoleOutput.setWrapText(true);

        leftPane.getChildren().addAll(editorLabel, stageTrackerLabel, codeEditor, runButton, consoleOutput);

        // ==========================================
        // RIGHT SIDE: CANVAS & STEADY MASCOT LAYER
        // ==========================================
        StackPane rightPane = new StackPane();
        rightPane.setStyle("-fx-background-color: #000000;"); 
        rightPane.setMinWidth(600); // Stop right pane from absorbing everything

        try {
            ImageView bgView = new ImageView(new Image(getClass().getResource("/world/jungle_bg.png").toExternalForm()));
            bgView.fitWidthProperty().bind(rightPane.widthProperty());
            bgView.fitHeightProperty().bind(rightPane.heightProperty());
            rightPane.getChildren().add(bgView);
        } catch (Exception e) {
            System.out.println("[UI ERROR] Core background asset stream failed to resolve: " + e.getMessage());
        }

        // --- RPG SPEECH DIALOGUE BUBBLE BOX ---
        VBox dialogueBubble = new VBox(8);
        dialogueBubble.setPadding(new Insets(15));
        dialogueBubble.setMaxWidth(420);
        dialogueBubble.setStyle("-fx-background-color: rgba(15, 25, 12, 0.92); -fx-background-radius: 12; -fx-border-color: #a2db4e; -fx-border-width: 2.5; -fx-border-radius: 10;");
        
        dialogueText = new Label();
        dialogueText.setWrapText(true);
        dialogueText.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        dialogueText.setTextFill(Color.WHITE);

        hintText = new Label();
        hintText.setWrapText(true);
        hintText.setFont(Font.font("Courier New", FontWeight.NORMAL, 13));
        hintText.setTextFill(Color.web("#ffcc00"));

        dialogueBubble.getChildren().addAll(dialogueText, hintText);

        // --- CENTRAL MASCOT ANCHOR HOOK ---
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

        // ==========================================
        // ASSEMBLE ARCHITECTURE LAYOUT
        // ==========================================
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.38); 
        
        NavButton backBtn = new NavButton("← LEAVE ROOM", this, () -> mainFrame.showWorldSelection());
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
        
        ChallengeData currentChallenge = challenges[currentStage];
        stageTrackerLabel.setText("STAGE PROGRESS: " + (currentStage + 1) + " / " + challenges.length);
        dialogueText.setText("💬 QUEST: " + currentChallenge.prompt);
        hintText.setText("💡 HINT: " + currentChallenge.hint);
        codeEditor.clear();
    }

    private void evaluateSubmission() {
        if (currentStage >= challenges.length) return;

        String rawInput = codeEditor.getText();
        String normalizedInput = rawInput.replaceAll("\\s+", "");
        String normalizedAnswer = challenges[currentStage].expectedToken.replaceAll("\\s+", "");

        if (normalizedInput.isEmpty()) {
            consoleOutput.setText("❌ RUNTIME FAILURE: Code editor viewport context cannot evaluate empty frames.");
            consoleOutput.setStyle("-fx-text-fill: #CC6666; -fx-font-family: 'Consolas'; -fx-font-weight: bold;");
            return;
        }

        if (normalizedInput.contains(normalizedAnswer)) {
            int previousLevel = UserSession.getLevel();
            UserSession.addXp(5);
            int postLevel = UserSession.getLevel();
            currentStage++;

            if (postLevel > previousLevel) {
                refreshMascotVisuals();
                consoleOutput.setText("⭐ EVOLUTION TRIGGERED! Mascot leveled up to Level " + postLevel + "!");
                consoleOutput.setStyle("-fx-text-fill: #ffcc00; -fx-font-family: 'Consolas'; -fx-font-weight: bold;");
            } else {
                consoleOutput.setText("✔ COMPILATION SUCCESS! Gained +5 XP points processing syntax token verification keys.");
                consoleOutput.setStyle("-fx-text-fill: #89C765; -fx-font-family: 'Consolas'; -fx-font-weight: bold;");
            }

            loadCurrentChallenge();

        } else {
            consoleOutput.setText("❌ SYNTAX ERROR: Evaluation rejected.\nUnexpected code patterns found. Reference the hint and try again.");
            consoleOutput.setStyle("-fx-text-fill: #CC6666; -fx-font-family: 'Consolas';");
        }
    }

    private void refreshMascotVisuals() {
        try {
            String updatedPath = MascotManager.getCurrentMascotPath(false);
            mascotView.setImage(new Image(getClass().getResource(updatedPath).toExternalForm()));
        } catch (Exception e) {
            System.err.println("[UI ERROR] Failed redrawing image framework views: " + e.getMessage());
        }
    }

    private void triggerRoomVictory() {
        dialogueText.setText("🎉 EXCELLENT WORK! You completed all jungle script assignments safely!");
        hintText.setText("Mission complete. Return back to world operations.");
        codeEditor.setDisable(true);
        stageTrackerLabel.setText("COMPLETED 10 / 10");
        consoleOutput.setText("SUCCESS: Sector cleared completely. Hit 'LEAVE ROOM' to log final session variables.");
        consoleOutput.setStyle("-fx-text-fill: #ffcc00; -fx-font-family: 'Consolas'; -fx-font-weight: bold;");
    }

    private static class ChallengeData {
        final String prompt;
        final String hint;
        final String expectedToken;

        ChallengeData(String prompt, String hint, String expectedToken) {
            this.prompt = prompt;
            this.hint = hint;
            this.expectedToken = expectedToken;
        }
    }
}