import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a player's tank in the game.
 */
class Tank {
    int x, y, width, height, lives, energy;
    Color color;
    public boolean doubleDamageNextShot = false;
    public boolean doubleDamage = false;
    public boolean shielded = false;

    /**
     * Constructs a new Tank with the given position, color, and energy.
     * @param x the x-coordinate of the tank
     * @param y the y-coordinate of the tank
     * @param color the color of the tank
     * @param energy the starting energy for the tank
     */
    public Tank(int x, int y, Color color, int energy) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 20;
        this.color = color;
        this.lives = 3;
        this.energy = energy;
    }

    /**
     * Returns the x-coordinate of the tank.
     * @return the x position
     */
    public int getX() {
        return x;
    }

    public void setDoubleDamageForNextShot() {
        doubleDamageNextShot = true;
    }

    /**
     * Returns the y-coordinate of the tank.
     * @return the y position
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the number of lives remaining for the tank.
     * @return the number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns the remaining energy of the tank.
     * @return the energy value
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Draws the tank on the game screen.
     * @param g the Graphics object used for drawing
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
     * Resets the tank's position, energy, and lives to default values.
     * Used for restarting the game or resetting the tank.
     */
    public void reset() {
        this.x = 100;
        this.y = 100;
        this.lives = 3;
        this.energy = 100;
    }

    /**
     * Reduces the tank's lives by one.
     */
    public void loseLife() {
        this.lives--;
    }
}
