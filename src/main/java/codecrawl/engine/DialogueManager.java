package codecrawl.engine;

import codecrawl.models.Mascot;
import java.awt.geom.*;
import java.awt.*;
    public class DialogueManager {
        private String visibleText = "";
        private String fullText = "";
        private int charIndex = 0;
        private boolean isVisible = false;
        
        private int typeCounter = 0;
        private final int TYPE_SPEED = 3;

    public void startDialogue(String text) {
        System.out.println("ENGINE: Dialogue Received -> " + text);
        this.fullText = text;
        this.visibleText = "";
        this.charIndex = 0;
        this.isVisible = true;
    }

        public void update() {
            if (isVisible && charIndex < fullText.length()) {
                typeCounter++;
                
                if (typeCounter >= TYPE_SPEED)  { 
                    visibleText += fullText.charAt(charIndex);
                    charIndex++;
                    typeCounter = 0;
                }   
            }
        }    
            

       public void draw(Graphics2D g2, Mascot speaker, int camX) {
   
        if (!isVisible || speaker == null) return;

         // 1. POSITIONING & SIZE (Smaller, comic-style oval)
        int bw = 200; 
        int bh = 90;
    
        // Calculate mascot screen center
        int mascotX = speaker.worldX - camX;
        int mascotY = speaker.worldY;

        // Place bubble to the RIGHT and ABOVE the face
        int bx = mascotX - (bw / 2) + 40; 
        int by = mascotY - 110;

        // 2. TURN ON SMOOTHING
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

         // 3. DRAW THE OVAL
        g2.setColor(Color.WHITE);
        g2.fillOval(bx, by, bw, bh);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3)); // Thick comic outline
        g2.drawOval(bx, by, bw, bh);
    
        // 4. DRAW THE CURVED TAIL 
        int tailStartX = bx + (bw / 2) - 20; // Start at the bottom-left of the oval
        int tailStartY = by + bh - 5;
        int targetX = mascotX + 60; // Points specifically to the mouth/cheek
        int targetY = mascotY + 40;

        Path2D tail = new Path2D.Double();
        tail.moveTo(tailStartX, tailStartY); 
        // This creates the "S-curve" from your picture
        tail.quadTo(tailStartX, tailStartY + 10, targetX, targetY); 
        tail.quadTo(tailStartX + 30, tailStartY + 10, tailStartX + 40, tailStartY);
    
        g2.setColor(Color.WHITE);
        g2.fill(tail);
        g2.setColor(Color.BLACK);
        g2.draw(tail);
        
     
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 12)); // Smaller font for smaller bubble
        g2.setColor(Color.BLACK);
        FontMetrics fm = g2.getFontMetrics();

    // If text is long, we split it into two lines manually
        if (fm.stringWidth(visibleText) > bw - 20) {
        String[] words = visibleText.split(" ");
        String line1 = "";
        String line2 = "";
        
        for (int i = 0; i < words.length; i++) {
            if (fm.stringWidth(line1 + words[i]) < bw - 30 && line2.isEmpty()) {
                line1 += words[i] + " ";
            } else {
                line2 += words[i] + " ";
            }
        }
        // Draw two lines
        g2.drawString(line1, bx + (bw - fm.stringWidth(line1)) / 2, by + (bh / 2) - 5);
        g2.drawString(line2, bx + (bw - fm.stringWidth(line2)) / 2, by + (bh / 2) + 15);
         } else {
        // Draw single line
        int tx = bx + (bw - fm.stringWidth(visibleText)) / 2;
        int ty = by + (bh / 2) + (fm.getAscent() / 2) - 5;
        g2.drawString(visibleText, tx, ty);    
        }
        
        
    }
}
