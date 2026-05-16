package codecrawl.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class AssetLoader {

    public static BufferedImage loadImage(String path) {
        BufferedImage image = null;
        
        // This looks for the image inside your project's 'res' folder
        try (InputStream is = AssetLoader.class.getResourceAsStream("/" + path + ".png")) {
            if (is == null) {
                System.out.println("ERROR: Could not find file at /" + path + ".png");
            } else {
                image = ImageIO.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return image;
    }
}