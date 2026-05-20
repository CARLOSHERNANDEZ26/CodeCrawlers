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

public class CSSRoom extends StackPane {
    
    private final MainFrame mainApp;
    private TextArea codeEditor;
    private ImageView mascotView;
    private Label consoleOutput;
    
    private Label dialogueText;
    private Label hintText;
    private Label stageTrackerLabel;
    private int currentStage = 0;

    private final ChallengeData[] challenges = {
        new ChallengeData("Open a layout styling block selector target rule map matching the global 'body' tag element node.", "Type the token target key keyword name followed by an opening curly bracket structural constraint '{'.", "body{"),
        new ChallengeData("Write the complete declaration assignment setting the background color property to pure black hex format.", "Use the property term 'background-color' assigned value parameter '#000000' closed with a semicolon.", "background-color:#000000;"),
        new ChallengeData("Set the foreground font character text layout layout paint color property variable value directly to white.", "Utilize the layout styling rule assignment key: color: #ffffff;", "color:#ffffff;"),
        new ChallengeData("Write a font family mapping assignment rule targeting system terminal fonts 'Consolas'.", "The property target is 'font-family' mapped using key string rules.", "font-family:'Consolas';"),
        new ChallengeData("Center align your inline visual elements completely using text direction configurations.", "Apply alignment keys utilizing value syntax patterns: text-align: center;", "text-align:center;"),
        new ChallengeData("Open a style definition layout rule query targeting custom document elements tagged with class identifier 'card'.", "CSS layout engine selectors map class targets using dot configuration prefixes: .card{", ".card{"),
        new ChallengeData("Target a single explicit distinct node structural layout matching unique wrapper id 'mainHeader'.", "Unique identification parameters use hash tag structural constraints: #mainHeader{", "#mainHeader{"),
        new ChallengeData("Assign an outer layout buffering buffer margin spacing distance layer setting it uniformly to 20 pixels.", "Outer layout element spacing dimensions use properties named 'margin: 20px;'", "margin:20px;"),
        new ChallengeData("Apply internal structure box context boundary padding spacing dimensions setting it safely to 15 pixels.", "Inner spacing constraints utilize internal boundary tracking parameters: padding: 15px;", "padding:15px;"),
        new ChallengeData("Close the current active selection rule context block layout container statement.", "Terminology block rule processing bounds terminate via standard closing bracket keys: }", "}")
    };

    public CSSRoom(MainFrame mainApp) {
        this.mainApp = mainApp;
        setupUI();
        loadCurrentChallenge();
    }

   private void setupUI() {
        VBox leftPane = new VBox(15);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #1e1e2e;"); 
        
        // Let the left pane have a normal flexible width, but never shrink too small
        leftPane.setMinWidth(350); 
        leftPane.setPrefWidth(420);
        leftPane.setMaxWidth(500);

        Label editorLabel = new Label("STYLING TERMINAL - CSS_CORE");
        editorLabel.setStyle("-fx-text-fill: #6366f1; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Courier New';");

        stageTrackerLabel = new Label("STAGE: 1 / 10");
        stageTrackerLabel.setStyle("-fx-text-fill: #cdd6f4; -fx-font-family: 'Courier New'; -fx-font-size: 14px;");

        codeEditor = new TextArea();
        codeEditor.setPromptText("/* Inject cascading style rules here */");
        codeEditor.setFont(Font.font("Consolas", 16)); 
        codeEditor.setStyle("-fx-control-inner-background: #11111b; -fx-text-fill: #a6e3a1; -fx-border-color: #45475a; -fx-border-width: 2;");
        VBox.setVgrow(codeEditor, Priority.ALWAYS); 

        Button runButton = new Button("INJECT CASCADING STYLE");
        runButton.setStyle("-fx-background-color: #6366f1; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-font-family: 'Courier New'; -fx-cursor: hand;");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> evaluateSubmission()); 
        
        consoleOutput = new Label("Layout Processing Unit Idle. Enter rules configuration vector loops.");
        consoleOutput.setStyle("-fx-text-fill: #6c7086; -fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        consoleOutput.setWrapText(true);

        leftPane.getChildren().addAll(editorLabel, stageTrackerLabel, codeEditor, runButton, consoleOutput);

        StackPane rightPane = new StackPane();
        
        // Now that SplitPane stops the layout freeze, we can safely use CSS cover on the right pane
        try {
            String bgUrl = getClass().getResource("/world/CSSroom.png").toExternalForm();
            rightPane.setStyle("-fx-background-image: url('" + bgUrl + "'); " +
                               "-fx-background-size: cover; " +
                               "-fx-background-position: center; " +
                               "-fx-background-repeat: no-repeat;");
        } catch (Exception e) {
            System.out.println("[CSS UI ERROR] Native layout background string failed to compile. Using solid fallback.");
            rightPane.setStyle("-fx-background-color: #181825;");
        }

        VBox dialogueBubble = new VBox(8);
        dialogueBubble.setPadding(new Insets(15));
        dialogueBubble.setMaxWidth(420);
        dialogueBubble.setStyle("-fx-background-color: rgba(17, 17, 27, 0.94); -fx-background-radius: 12; -fx-border-color: #6366f1; -fx-border-width: 2.5; -fx-border-radius: 10;");
        
        dialogueText = new Label();
        dialogueText.setWrapText(true);
        dialogueText.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        dialogueText.setTextFill(Color.WHITE);

        hintText = new Label();
        hintText.setWrapText(true);
        hintText.setFont(Font.font("Courier New", FontWeight.NORMAL, 13));
        hintText.setTextFill(Color.web("#cba6f7"));

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
        splitPane.setDividerPositions(0.40); 
        
        // Prevent the left terminal pane from being crushed when window resizes
        SplitPane.setResizableWithParent(leftPane, false);
        
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
        dialogueText.setText("💬 STYLE: " + challenge.prompt);
        hintText.setText("💡 HINT: " + challenge.hint);
        codeEditor.clear();
    }

    private void evaluateSubmission() {
        if (currentStage >= challenges.length) return;

        String normalizedInput = codeEditor.getText().replaceAll("\\s+", "");
        String normalizedAnswer = challenges[currentStage].expectedToken.replaceAll("\\s+", "");

        if (normalizedInput.isEmpty()) {
            consoleOutput.setText("❌ RENDER REJECTION: Workspace pipeline cannot calculate an empty theme parameters string.");
            consoleOutput.setStyle("-fx-text-fill: #f38ba8; -fx-font-weight: bold;");
            return;
        }

        if (normalizedInput.contains(normalizedAnswer)) {
            int prevLevel = UserSession.getLevel();
            UserSession.addXp(5);
            int postLevel = UserSession.getLevel();
            currentStage++;

            if (postLevel > prevLevel) {
                refreshMascotVisuals();
                consoleOutput.setText("⭐ STYLE EVOLUTION ACHIEVED! Mascot rendered layout updates to Level " + postLevel + "!");
                consoleOutput.setStyle("-fx-text-fill: #fab387; -fx-font-weight: bold;");
            } else {
                consoleOutput.setText("✔ DECLARATION PARSED! Gained +5 XP layout design metrics parameters.");
                consoleOutput.setStyle("-fx-text-fill: #a6e3a1; -fx-font-weight: bold;");
            }
            loadCurrentChallenge();
        } else {
            consoleOutput.setText("❌ LAYOUT MISMATCH: Rule assignment rejected. Ensure characters match parameters expectations syntax.");
            consoleOutput.setStyle("-fx-text-fill: #f38ba8;");
        }
    }

    private void refreshMascotVisuals() {
        try {
            String updatedPath = MascotManager.getCurrentMascotPath(false);
            mascotView.setImage(new Image(getClass().getResource(updatedPath).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Theme matrix display adjustment crash: " + e.getMessage());
        }
    }

    private void triggerRoomVictory() {
        dialogueText.setText("🎉 COMPONENT STYLED! Visual properties constraints match standard design parameters completely!");
        hintText.setText("Engine execution trace complete. Return to learning management operations mapping.");
        codeEditor.setDisable(true);
        stageTrackerLabel.setText("COMPLETED 10 / 10");
        consoleOutput.setText("SUCCESS: Style sheet compiled successfully. Use exit navigators safely.");
        consoleOutput.setStyle("-fx-text-fill: #fab387; -fx-font-weight: bold;");
    }

    private static class ChallengeData {
        final String prompt, hint, expectedToken;
        ChallengeData(String p, String h, String e) { prompt = p; hint = h; expectedToken = e; }
    }
}