package codecrawl.models;

import codecrawl.engine.AssetLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mascot {

    public int worldX, worldY;
    public int height, width;
    private String name;
    private int evolutionLevel = 0; // 0-5 (Egg to Master)
    private int mood = 0;           // 0=idle, 1=happy/excited, 2=talk
    private BufferedImage[][] sprites = new BufferedImage[6][3];

    private Image sprite;

    public Mascot(String name, int x, int y) {
        this.name = name.toLowerCase();
        this.worldX = x;
        this.worldY = y;
        loadSprites();
    }

       private void loadSprites() {
        String[] stages = {"egg", "kid", "teen", "adult",};
        String[] moods = {"idle", "happy"};
        for (int s = 0; s < 4; s++) {
            for (int m = 0; m < 2; m++) {
                // Builds: mascots/snake/adult_idle_snake
                String path = "mascot/" + name + "/" + stages[s] + "_" + name+ "_" + moods[m];
                sprites[s][m] = AssetLoader.loadImage(path);
            }
        }
    }


    public void draw(Graphics2D g2, int camX, int camY, java.awt.Component observer) {
        
        BufferedImage img = sprites[evolutionLevel][mood];
       if (img != null) {


        this.width = 300;
        this.height = 300;

       } g2.drawImage(sprite, worldX - camX, worldY - camY, width, height, observer);
    }
}