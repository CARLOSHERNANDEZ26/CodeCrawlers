package codecrawl.levels;

import codecrawl.engine.MascotManager; 
import codecrawl.core.UserSession;    
import codecrawl.ui.MuteButton;
import codecrawl.ui.NavButton; 
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import codecrawl.core.MainFrame;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class JungleLevel extends StackPane { // UPGRADED: Changed from SplitPane to StackPane
    
    private MainFrame mainFrame;
    private TextArea codeEditor;
    private ImageView mascotView;
    private Label consoleOutput;

    public JungleLevel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setupUI();
    }

    private void setupUI() {
        // ==========================================
        // LEFT SIDE: THE CODE EDITOR
        // ==========================================
        VBox leftPane = new VBox(15);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #2b2b2b;"); 

        Label editorLabel = new Label("Code Editor - Level 1");
        editorLabel.setStyle("-fx-text-fill: #a9b7c6; -fx-font-weight: bold; -fx-font-size: 18px;");

        codeEditor = new TextArea();
        codeEditor.setPromptText("Type your code here...\n\nExample:\nkyro.moveRight();");
        codeEditor.setFont(Font.font("Consolas", 16)); 
        codeEditor.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: #a9b7c6;");
        VBox.setVgrow(codeEditor, Priority.ALWAYS); 

        Button runButton = new Button("RUN CODE");
        runButton.setStyle("-fx-background-color: #6a8759; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        runButton.setMaxWidth(Double.MAX_VALUE);
        
        runButton.setOnAction(e -> executeCode()); 
        
        consoleOutput = new Label("System is Ready. Waiting for input...");
        consoleOutput.setStyle("-fx-text-fill: #a9b7c6; -fx-font-family: 'Consolas';");
        consoleOutput.setWrapText(true);

        leftPane.getChildren().addAll(editorLabel, codeEditor, runButton, consoleOutput);

        // ==========================================
        // RIGHT SIDE: THE GAME CANVAS
        // ==========================================
        Pane rightPane = new Pane();
        rightPane.setStyle("-fx-background-color: #000000;"); 
        
        try {
            ImageView bgView = new ImageView(new Image(getClass().getResource("/world/jungle_bg.png").toExternalForm()));
            bgView.fitWidthProperty().bind(rightPane.widthProperty());
            bgView.fitHeightProperty().bind(rightPane.heightProperty());
            
            String mascotPath = MascotManager.getCurrentMascotPath(false); 
            mascotView = new ImageView(new Image(getClass().getResource(mascotPath).toExternalForm()));
            mascotView.setX(50);  
            mascotView.setY(420); 
            mascotView.setFitWidth(120);
            mascotView.setPreserveRatio(true);

            rightPane.getChildren().addAll(bgView, mascotView);
        } catch (Exception e) {
            System.out.println("UI ERROR: Could not load images. " + e.getMessage());
        }

        // ==========================================
        // ASSEMBLE THE SPLITPANE VIEW
        // ==========================================
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.35); 
        
        // ==========================================
        // NAVIGATION & AUDIO TOOLBAR
        // ==========================================
        // FIXED: Now safely targets 'this' (the room StackPane) as the modal's root layer view
        NavButton backBtn = new NavButton("← LEAVE LEVEL", this, () -> mainFrame.showWorldSelection());

        HBox toolbar = new HBox(15, backBtn, new MuteButton());
        toolbar.setAlignment(Pos.CENTER_RIGHT); 
        toolbar.setPickOnBounds(false); // Prevents the invisible container box from swallowing mouse clicks
        
        // Add toolbar to the top stack positioning region inside the left side editor column
        leftPane.getChildren().add(0, toolbar); 

        // Add the primary split framework assembly directly onto the main root layer
        this.getChildren().add(splitPane);
    }

    private void executeCode() {
        String cleanCode = codeEditor.getText().replaceAll("\\s+", ""); 

        if (cleanCode.equals("kyro.moveRight();")) {
            
            // 1. Check current level BEFORE gaining XP
            int oldLevel = UserSession.getLevel();
            
            // 2. Grant XP!
            UserSession.addXp(20);
            
            // 3. Check level AFTER gaining XP
            int newLevel = UserSession.getLevel();

            // 4. DID WE LEVEL UP?! Trigger live evolution!
            if (newLevel > oldLevel) {
                String newMascot = MascotManager.getCurrentMascotPath(false);
                mascotView.setImage(new Image(getClass().getResource(newMascot).toExternalForm()));
                
                consoleOutput.setText("LEVEL UP!!! \nYour mascot has evolved to Level " + newLevel + "!");
                consoleOutput.setStyle("-fx-text-fill: #ffcc00; -fx-font-weight: bold; -fx-font-size: 18px;");
            } else {
                // Normal success message
                consoleOutput.setText("SUCCESS: Mascot is moving!\n+20 XP Gained! (Current XP: " + UserSession.getXp() + "/100)");
                consoleOutput.setStyle("-fx-text-fill: #89C765; -fx-font-weight: bold;");
            }
            
            // Play the walk animation
            javafx.animation.TranslateTransition walk = new javafx.animation.TranslateTransition();
            walk.setDuration(javafx.util.Duration.seconds(1.5)); 
            walk.setNode(mascotView);
            walk.setByX(200);
            walk.play();
            
        } else if (cleanCode.isEmpty()) {
            consoleOutput.setText("ERROR: Editor is currently empty.");
            consoleOutput.setStyle("-fx-text-fill: #CC6666;"); 
        } else {
            consoleOutput.setText("SYNTAX ERROR: Command not recognized.");
            consoleOutput.setStyle("-fx-text-fill: #CC6666; -fx-font-weight: bold;"); 
        }
    }
}