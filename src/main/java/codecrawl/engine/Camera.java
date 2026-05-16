package codecrawl.engine;

public class Camera {
    public int x = 0; // The horizontal position of our "viewing window"
    private int worldWidth = 12500; // Your Webtoon landscape width
    private int screenWidth = 1280; // Your Java window width
    
    

    public void follow(int mascotWorldX) { 
        this.x = mascotWorldX - (screenWidth / 2);

        if (this.x < 0) this.x = 0;
        if (this.x > worldWidth - screenWidth) this.x = worldWidth - screenWidth;
    }
}
