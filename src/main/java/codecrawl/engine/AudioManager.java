package codecrawl.engine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    public static boolean isPlaying = true;
    private static MediaPlayer bgmPlayer;
    
    public static void toggleMusic() {
    if (bgmPlayer != null) {
        if (isPlaying) {
            bgmPlayer.stop();
            isPlaying = false;
        } else {
            bgmPlayer.play();
            isPlaying = true;
        }
    }
}

    public static void playMusic() {
    System.out.println("DEBUG: Attempting to play music...");
    try {
        var resource = AudioManager.class.getResource("/bgm.wav");
        if (resource != null) {
            System.out.println("DEBUG: Found bgm.mp3 at: " + resource.toExternalForm());
            Media media = new Media(resource.toExternalForm());
            bgmPlayer = new MediaPlayer(media);

            bgmPlayer.setOnError(() -> {
                System.out.println("MEDIA ERROR: " + bgmPlayer.getError().getMessage());
            });

            bgmPlayer.setOnPlaying(() -> System.out.println("DEBUG: Music is now officially PLAYING!"));

            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(0.70);
            bgmPlayer.play();
        } else {
            System.out.println("DEBUG: Resource bgm.mp3 was NOT found.");
        }
    } catch (Exception e) {
        System.out.println("AUDIO EXCEPTION: " + e.getMessage());
        e.printStackTrace();
    }
}

    public static void setVolume(double volume) {
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(volume);
        }
    }
}