package codecrawl.engine;

import codecrawl.core.UserSession;

public class MascotManager {

    /**
     * Returns the correct file path for the mascot based on the player's current level.
     * Evolution Path: Level 1 (Idle/Egg) -> Lvl 2 (Baby/Hatch) -> Lvl 3 (Kid) -> Lvl 4 (Teen) -> Lvl 5 (Adult)
     */
    public static String getCurrentMascotPath(boolean isIdle) {
        String mascot = UserSession.getSelectedMascot().toLowerCase();
        int level = UserSession.getLevel();

        // 1. SNAKE LOGIC
        if (mascot.equals("snake")) {
            if (level == 1) return isIdle ? "/mascot/snake/egg_snake_idle.png" : "/mascot/snake/hatch_snake.gif";
            if (level == 2) return "/mascot/snake/hatch_snake.gif";
            if (level == 3) return "/mascot/snake/kid_snake.gif";
            if (level == 4) return "/mascot/snake/teen_snake.gif";
            return "/mascot/snake/adult_snake.gif"; // Level 5+
        }
        
        // 2. ELEPHANT LOGIC
        if (mascot.equals("elephant")) {
            if (level == 1) return isIdle ? "/mascot/elephant/baby_elephant_idle.PNG" : "/mascot/elephant/baby_elephant.gif";
            if (level == 2) return "/mascot/elephant/baby_elephant.gif";
            if (level == 3) return "/mascot/elephant/kid_elephant.gif";
            if (level == 4) return "/mascot/elephant/teen_elephant.gif";
            return "/mascot/elephant/adult_elephant.gif"; // Level 5+
        }

        // 3. CHAMELEON LOGIC
        if (mascot.equals("chameleon")) {
            if (level == 1) return isIdle ? "/mascot/chameleon/baby_chameleon_idle.png" : "/mascot/chameleon/baby_chameleon.gif";
            if (level == 2) return "/mascot/chameleon/baby_chameleon.gif";
            if (level == 3) return "/mascot/chameleon/kid_chameleon.gif";
            if (level == 4) return "/mascot/chameleon/teen_chameleon.gif";
            return "/mascot/chameleon/adult_chameleon.gif"; // Level 5+
        }

        return "/mascot/snake/egg_snake_idle.png"; // Safe Fallback
    }
}