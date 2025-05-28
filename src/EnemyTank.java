import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents an enemy tank in the game, which is a specialized type of Tank.
 */
class EnemyTank extends Tank {
    /**
     * Constructs a new EnemyTank with specified position, color, and energy.
     * @param x the x-coordinate of the enemy tank
     * @param y the y-coordinate of the enemy tank
     * @param color the color of the enemy tank
     * @param energy the starting energy for the enemy tank
     */
    public EnemyTank(int x, int y, Color color, int energy) {
        super(x, y, color, energy);
    }

    /**
     * Draws the enemy tank on the game screen.
     * @param g the Graphics object for drawing
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
     * Prints a taunt message to the console.
     * Can be used for debugging or flavor text.
     */
    public void taunt() {
        System.out.println("You'll never defeat me!");
    }

    /**
     * Determines if the enemy tank is defeated (energy is zero or less).
     * @return true if the tank is defeated, false otherwise
     */
    public boolean isDefeated() {
        return energy <= 0;
    }
}
