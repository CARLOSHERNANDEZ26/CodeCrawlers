package codecrawl.ui;

import codecrawl.engine.AudioManager;
import javafx.scene.control.Button;

public class MuteButton extends Button {
    
    public MuteButton() {
        super("🎵"); 
        this.setStyle("-fx-background-color: transparent; -fx-text-fill: #a9b7c6; -fx-cursor: hand; -fx-font-size: 18px;");
        
        this.setOnAction(e -> {
            AudioManager.toggleMusic();
            if (AudioManager.isPlaying) {
                this.setText("🎵"); 
            } else {
                this.setText("🔇"); 
            }
        });
    }
}