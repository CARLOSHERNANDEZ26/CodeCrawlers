package codecrawl.ui;

import codecrawl.core.UserSession;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class NavButton extends Button {

    public NavButton(String text, StackPane rootPane, Runnable onConfirm) {
        super(text);
        this.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: black; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 14px;");
        this.setOnAction(e -> showConfirmationModal(rootPane, onConfirm));
    }

    private void showConfirmationModal(StackPane rootPane, Runnable onConfirm) {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(460, 220); 
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: rgba(15, 15, 15, 0.98); -fx-border-color: #ff8c00; -fx-border-width: 2.5; -fx-background-radius: 12; -fx-border-radius: 12;");

        Label warningIcon = new Label("⚠");
        warningIcon.setTextFill(Color.web("#ff8c00"));
        warningIcon.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        Label title = new Label("LEAVE CURRENT SESSION?");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 16));

        // Choice matrix
        Button saveLeaveBtn = new Button("💾 SAVE & LEAVE");
        saveLeaveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 10 15;");
        
        Button justLeaveBtn = new Button("⚠ JUST LEAVE");
        justLeaveBtn.setStyle("-fx-background-color: #cc2222; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 10 15;");
        
        Button cancelBtn = new Button("✖ CANCEL");
        cancelBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 10 15;");

        HBox btnRow = new HBox(12, saveLeaveBtn, justLeaveBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(warningIcon, title, btnRow);

        StackPane overlay = new StackPane(card);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        overlay.setOpacity(0);
        
        rootPane.getChildren().add(overlay);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), overlay);
        fadeIn.setToValue(1);
        fadeIn.play();

        Runnable dismiss = () -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(180), overlay);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> rootPane.getChildren().remove(overlay));
            fadeOut.play();
        };

        cancelBtn.setOnAction(e -> dismiss.run());
        overlay.setOnMouseClicked(e -> { if (e.getTarget() == overlay) dismiss.run(); });

        justLeaveBtn.setOnAction(e -> {
            dismiss.run();
            onConfirm.run(); 
        });

        saveLeaveBtn.setOnAction(e -> {
            System.out.println("[NAVIGATION] Intercepting exit... Syncing state snapshot to database.");
            UserSession.saveToDatabase(); // Force immediate database push
            dismiss.run();
            onConfirm.run(); 
        });
    }
}