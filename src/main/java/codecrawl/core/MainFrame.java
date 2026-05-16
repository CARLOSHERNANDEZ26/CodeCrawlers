package codecrawl.core;

import codecrawl.engine.AudioManager;
import codecrawl.auth.AuthService;
import codecrawl.levels.*;
import codecrawl.ui.HomeScreen;
import codecrawl.auth.LogInUI;
import codecrawl.ui.WorldSelection;
import codecrawl.ui.VideoPlayerScreen; // Restored Import
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFrame extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CodeCrawl - Byte-Sized Beasts");
        
        // RESTORED BOOT SEQUENCE: Instead of going straight to login, play intro flow!
        playBootSequence();
        
        this.primaryStage.show();
        
        // PRESERVED GLOBAL CLOSURE DATA PERSISTENCE HOOK
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("[SHUTDOWN TRACE] Intercepting native window termination... Forcing persistence commit.");
            UserSession.saveToDatabase(); 
            System.out.println("Closing app... stopping audio.");
            System.exit(0);
        });
    }
    
    // ══════════════════════════════════════════════════════════════════════
    // RESTORED VIDEO ENGINE SEQUENCING
    // ══════════════════════════════════════════════════════════════════════
    private void playBootSequence() {
        // 1. Play Loading -> Then Kyro Intro -> Then Login
        playVideo("/loading.mp4", () -> {
            playVideo("/kyro_intro.mp4", () -> {
                // Intro videos complete! Fire up audio loop engine and summon Login view
                AudioManager.playMusic(); 
                showLoginScreen();
            });
        });
    }

    private void playVideo(String path, Runnable onComplete) {
        VideoPlayerScreen videoScreen = new VideoPlayerScreen(path, onComplete);
        Scene scene = new Scene(videoScreen, 1280, 720);
        primaryStage.setScene(scene);
    }

    // ══════════════════════════════════════════════════════════════════════
    // INTERACTION WINDOW ROUTING PANELS
    // ══════════════════════════════════════════════════════════════════════
    public void showLoginScreen() {
        AuthService auth = new AuthService();
        LogInUI loginUI = new LogInUI(auth);
        
        // RESTORED: Play cinematic trailer upon entry authentication wrapper confirm!
        loginUI.setOnSuccess(() -> {
            if (AudioManager.isPlaying) {
                AudioManager.toggleMusic(); // Temporarily halt BGM threads for video crispness
            }
            
            playVideo("/play_intro.mp4", () -> {
                if (!AudioManager.isPlaying) {
                    AudioManager.toggleMusic(); // Wake up BGM tracks cleanly
                }
                showHomeScreen(); // Route straight into Dashboard
            });
        });
        
        Scene scene = new Scene(loginUI, 1280, 720);
        primaryStage.setScene(scene);
    }
    
    public void showJungleLevel() {
        JungleLevel jungleLevel = new JungleLevel(this);
        Scene scene = new Scene(jungleLevel, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showHomeScreen() {
        HomeScreen homeScreen = new HomeScreen(this);
        Scene scene = new Scene(homeScreen, 1280, 720);
        primaryStage.setScene(scene);
    }
    
    public void showProfileScreen() {
        codecrawl.ui.ProfileScreen profileScreen = new codecrawl.ui.ProfileScreen(this);
        Scene scene = new Scene(profileScreen, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showWorldSelection() {
        WorldSelection worldScreen = new WorldSelection(this);
        Scene scene = new Scene(worldScreen, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showHouse() {
        House houseScreen = new House(this);
        Scene scene = new Scene(houseScreen, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showHTMLRoom() {
        HTMLRoom htmlRoom = new HTMLRoom(this);
        Scene scene = new Scene(htmlRoom, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showCSSRoom() {
        CSSRoom cssRoom = new CSSRoom(this);
        Scene scene = new Scene(cssRoom, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showDatabaseRoom() {
        DatabaseRoom dbRoom = new DatabaseRoom(this);
        Scene scene = new Scene(dbRoom, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showIndustrialLevel() {
        IndustrialLevel indLevel = new IndustrialLevel(this);
        Scene scene = new Scene(indLevel, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showShopScreen() {
        codecrawl.ui.ShopScreen shopScreen = new codecrawl.ui.ShopScreen(this);
        Scene scene = new Scene(shopScreen, 1280, 720);
        primaryStage.setScene(scene);
    }

    public void showLeaderboard() {
        System.out.println("Leaderboard screen is not yet implemented.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}