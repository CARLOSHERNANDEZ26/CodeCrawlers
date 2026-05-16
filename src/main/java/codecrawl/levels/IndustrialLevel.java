package codecrawl.levels;

import codecrawl.core.MainFrame;
import codecrawl.core.UserSession; // Import session tracking to reward XP updates
import codecrawl.ui.MuteButton;
import codecrawl.ui.NavButton;  // Import standardized confirmation routing
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class IndustrialLevel extends StackPane { // UPGRADED: Inherits from StackPane
    private TextArea codeEditor;
    private Label titleLabel;
    private Label instructionLabel;
    private Label progressLabel;
    private StackPane gamePreview;
    private int currentLevel = 0;
    private MainFrame mainApp;
    
    private final String BG_FACTORY = "#1a1c1e";   
    private final String HAZARD_ORANGE = "#ff8c00"; 
    private final String ELECTRIC_BLUE = "#00d4ff"; 
    private final String STEEL_BORDER = "#454d55";  

    private final LevelData[] allLevels = {
        new LevelData("UNIT_01", "SYSTEM_BOOT", "Print 'System Online' to the console. Don't forget the semicolon!", "public class Main {\n    public static void main(String[] args) {\n        \n    }\n}", "System.out.println(\"System Online\");"),
        new LevelData("UNIT_02", "PRESSURE_VALVE", "Declare an integer variable named 'pressure' and set it to 75.", "", "int pressure = 75;"),
        new LevelData("UNIT_03", "TEMP_MONITOR", "Declare a double variable named 'temperature' and set it to 98.6.", "", "double temperature = 98.6;"),
        new LevelData("UNIT_04", "SAFETY_LOCK", "Create a boolean named 'isLocked' and set it to true.", "", "boolean isLocked = true;"),
        new LevelData("UNIT_05", "SERIAL_ID", "Create a String variable named 'unitID' with the value 'AX-7'.", "", "String unitID = \"AX-7\";"),
        new LevelData("UNIT_06", "HEATER_LOGIC", "Write an if-statement: if temperature is greater than 100, print 'HOT'.", "if (temperature > 100) {\n    \n}", "System.out.println(\"HOT\");"),
        new LevelData("UNIT_07", "EMERGENCY_STOP", "Check if pressure is exactly equal to 0 using ==.", "", "pressure == 0"),
        new LevelData("UNIT_08", "STRESS_TEST", "Check if pressure is less than 50 AND isLocked is false.", "", "pressure < 50 && !isLocked"),
        new LevelData("UNIT_09", "ASSEMBLY_LINE", "Create a for-loop that runs 5 times.", "", "for(int i = 0; i < 5; i++)"),
        new LevelData("UNIT_10", "WHILE_ACTIVE", "Create a while-loop that runs as long as isLocked is true.", "", "while(isLocked)"),
        new LevelData("UNIT_11", "STORAGE_BIN", "Declare an integer array named 'parts' that can hold 5 numbers.", "", "int[] parts = new int[5];"),
        new LevelData("UNIT_12", "PART_ACCESS", "Assign the value 10 to the first index (0) of the 'parts' array.", "", "parts[0] = 10;"),
        new LevelData("UNIT_13", "ARRAY_SIZE", "Print the length of the 'parts' array.", "", "parts.length"),
        new LevelData("UNIT_14", "CORE_METHOD", "Define a public static void method named 'shutdown'.", "", "public static void shutdown()"),
        new LevelData("UNIT_15", "DATA_RETURN", "Create a method named 'getTemp' that returns a double.", "", "double getTemp()"),
        new LevelData("UNIT_16", "STRING_COMPARE", "Check if unitID equals 'AX-7' using the .equals() method.", "", "unitID.equals(\"AX-7\")"),
        new LevelData("UNIT_17", "CLASS_BLUEPRINT", "Create a new class named 'Robot'.", "", "class Robot"),
        new LevelData("UNIT_18", "OBJECT_GEN", "Create a new Robot object named 'bot1'.", "", "Robot bot1 = new Robot();"),
        new LevelData("UNIT_19", "CONSTRUCTOR", "Define a constructor for the Robot class.", "class Robot {\n    \n}", "public Robot()"),
        new LevelData("UNIT_20", "FINAL_ASSEMBLY", "Call the shutdown() method on bot1.", "", "bot1.shutdown();")
    };

    public IndustrialLevel(MainFrame mainApp) {
        this.mainApp = mainApp;

        HBox mainLayout = new HBox(30);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: " + BG_FACTORY + ";");

        setupGamePreview();
        VBox rightSide = setupCodeEditorContainer();

        mainLayout.getChildren().addAll(gamePreview, rightSide);
        HBox.setHgrow(gamePreview, Priority.ALWAYS);
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        
        // Assemble into root architecture layers
        this.getChildren().add(mainLayout);
        
        loadLevel(currentLevel);
    }

    private void setupGamePreview() {
        gamePreview = new StackPane();
        gamePreview.setStyle("-fx-background-color: #25282b; -fx-border-color: " + STEEL_BORDER + "; -fx-border-width: 6;");
              
        // UPGRADED: Direct injection of standardized confirmation NavButton passing 'this' StackPane anchor
        NavButton backToWorldBtn = new NavButton("← BACK TO WORLD", this, () -> mainApp.showWorldSelection());
        backToWorldBtn.setStyle("-fx-background-color: " + HAZARD_ORANGE + "; -fx-text-fill: black; -fx-font-family: 'Courier New'; -fx-font-weight: 900; -fx-font-size: 12px; -fx-padding: 8 16;");
        
        gamePreview.getChildren().add(backToWorldBtn);
        StackPane.setAlignment(backToWorldBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(backToWorldBtn, new Insets(0, 10, 10, 0));

        Label label = new Label("[ INDUSTRIAL_FEED_01 ]");
        label.setStyle("-fx-text-fill: " + ELECTRIC_BLUE + "; -fx-font-family: 'Courier New'; -fx-font-weight: bold;");
        gamePreview.getChildren().add(label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);
        StackPane.setMargin(label, new Insets(10));
    }

    private VBox setupCodeEditorContainer() {
        VBox container = new VBox(15); 
        container.setMinWidth(600);

        HBox topToolbar = new HBox();
        topToolbar.setAlignment(Pos.CENTER_RIGHT);
        MuteButton quickMute = new MuteButton();
        topToolbar.getChildren().add(quickMute);

        titleLabel = new Label();
        titleLabel.setStyle("-fx-text-fill: " + HAZARD_ORANGE + "; -fx-font-family: 'Courier New'; -fx-font-size: 28px; -fx-font-weight: 900;");
        
        instructionLabel = new Label();
        instructionLabel.setWrapText(true);
        instructionLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-family: 'Courier New'; -fx-font-size: 15px;");

        codeEditor = new TextArea();
        codeEditor.setPrefHeight(400);
        codeEditor.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: " + ELECTRIC_BLUE + "; -fx-font-family: 'Consolas'; -fx-font-size: 17px; -fx-border-color: " + STEEL_BORDER + "; -fx-border-width: 4;");

        Button nextButton = new Button("EXECUTE_COMMAND >");
        nextButton.setCursor(Cursor.HAND);
        nextButton.setPrefHeight(60);
        nextButton.setMaxWidth(Double.MAX_VALUE);
        nextButton.setStyle("-fx-background-color: " + HAZARD_ORANGE + "; -fx-text-fill: black; -fx-font-family: 'Courier New'; -fx-font-weight: 900; -fx-font-size: 18px; -fx-border-color: #cc7000; -fx-border-width: 0 0 8 0;");
        
        nextButton.setOnAction(e -> {
            String input = codeEditor.getText();
            String answerKey = allLevels[currentLevel].correctAnswer;

            if (input.contains(answerKey)) {
                // THE REWARD TRACKER: Inject 20 XP safely upon successful evaluations
                UserSession.addXp(20);
                
                currentLevel++;
                if (currentLevel < allLevels.length) {
                    loadLevel(currentLevel);
                } else {
                    titleLabel.setText("FACTORY_CERTIFIED");
                    instructionLabel.setText("All industrial core sub-systems fully initialized.");
                    nextButton.setDisable(true);
                }
            } else {
                instructionLabel.setText("SYNTAX ERROR: Command Rejected. Check your logic.");
                instructionLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-family: 'Courier New'; -fx-font-size: 15px;");
            }
        });

        progressLabel = new Label();
        progressLabel.setStyle("-fx-text-fill: #555c64; -fx-font-family: 'Courier New';");

        container.getChildren().addAll(topToolbar, titleLabel, instructionLabel, codeEditor, nextButton, progressLabel);
        return container;
    }

    private void loadLevel(int index) {
        LevelData data = allLevels[index];
        titleLabel.setText(data.id + ": " + data.name);
        instructionLabel.setText(data.instruction);
        instructionLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-family: 'Courier New'; -fx-font-size: 15px;");
        codeEditor.setText(data.startCode);
        progressLabel.setText("UNIT_PROGRESS: " + (index + 1) + "/" + allLevels.length + " | Lvl " + UserSession.getLevel() + " (" + UserSession.getXp() + "/100 XP)");
    }

    private static class LevelData {
        String id, name, instruction, startCode, correctAnswer;
        LevelData(String i, String n, String ins, String s, String ans) { 
            id=i; name=n; instruction=ins; startCode=s; correctAnswer=ans; 
        }
    }
}