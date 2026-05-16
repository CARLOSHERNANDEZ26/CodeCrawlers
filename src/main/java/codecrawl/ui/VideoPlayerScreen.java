package codecrawl.ui;

import javafx.application.Platform; // NEW IMPORT
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoPlayerScreen extends StackPane {
    
    private MediaPlayer mediaPlayer;

    public VideoPlayerScreen(String videoPath, Runnable onFinished) {
        this.setStyle("-fx-background-color: black;"); // Black screen while loading

        try {
            var resource = getClass().getResource(videoPath);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);

                // Force the video to fit the 1280x720 window perfectly
                mediaView.setFitWidth(1280);
                mediaView.setFitHeight(720);
                mediaView.setPreserveRatio(false); 

                this.getChildren().add(mediaView);

                // When the video ends, trigger the next screen!
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.dispose(); // Free up memory
                    
                    // CRITICAL FIX: Force the scene change back onto the Main UI Thread
                    Platform.runLater(onFinished);
                });

                // Auto-play the video
                mediaPlayer.play();
            } else {
                System.err.println("Video not found: " + videoPath);
                // Safe thread execution for missing files too
                Platform.runLater(onFinished); 
            }
        } catch (Exception e) {
            System.err.println("Error playing video: " + e.getMessage());
            Platform.runLater(onFinished); 
        }
    }
}