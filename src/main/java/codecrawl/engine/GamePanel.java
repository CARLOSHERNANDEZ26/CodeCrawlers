package codecrawl.engine;

import codecrawl.models.Mascot;
import codecrawl.engine.DialogueManager;
import codecrawl.engine.Camera;
import codecrawl.engine.AssetLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable {

    BufferedImage background;
    Camera camera = new Camera();
    DialogueManager dm = new DialogueManager();
    ArrayList<Mascot> mascot = new ArrayList<>();
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(400, 750));
        // Instantiate the Snake mascot to avoid "null" errors
        mascot.add(new Mascot("snake", 40, 140));
        background = AssetLoader.loadImage("world/jungle_bg");
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

   
    @Override
    public void run() {
        while (gameThread != null) {
            dm.update(); // Crucial for typewriter effect
            camera.follow(mascot.get(0).worldX); // Camera follows first mascot
            repaint();
            try { Thread.sleep(16); } catch (Exception e) {}
        }
    }

    public void giveHint(String message) {
        System.out.println("BRIDGE: Sending to DM -> " + message);   // We use invokeLater because we are coming from the JavaFX thread
        javax.swing.SwingUtilities.invokeLater(() -> {
        this.dm.startDialogue(message);
       
        repaint();
        });
    }
    
    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // 1. Draw Background (Naka-lock sa standard view area ng laro niyo)
    if (background != null) {
        g2.drawImage(background, 0, 0, 1280, 720, camera.x, 0, camera.x + 1280, 720, null);
    }

    for (Mascot m : mascot) {

     m.draw(g2, camera.x, 0, this);
    }

    dm.draw(g2, mascot.get(0), camera.x);
    
    g2.dispose();
}
}