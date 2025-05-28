import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a projectile bullet in the game.
 */
class Bullet {
    int x, y;
    double dx, dy;
    final double GRAVITY = 0.1;

    /**
     * Constructs a new Bullet with the given position, angle, and power.
     * @param x the starting x position
     * @param y the starting y position
     * @param angle the firing angle in degrees
     * @param power the power of the shot
     */
    public Bullet(int x, int y, int angle, int power) {
        this.x = x;
        this.y = y;
        double radian = Math.toRadians(angle);
        dx = Math.cos(radian) * power / 10;
        dy = -Math.sin(radian) * power / 10;
    }

    /**
     * Moves the bullet according to its velocity and the given wind.
     * @param wind the wind speed affecting the bullet's horizontal movement
     */
    public void move(int wind) {
        x += dx + wind / 10.0;
        y += dy;
        dy += GRAVITY;
    }

    /**
     * Returns the current x-coordinate of the bullet.
     * @return the x position
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the current y-coordinate of the bullet.
     * @return the y position
     */
    public int getY() {
        return y;
    }

    /**
     * Draws the bullet on the screen.
     * @param g the Graphics object used for drawing
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(x - 3, y - 3, 6, 6);
    }

    /**
     * Resets the bullet's position to a new x and y.
     * This method does not affect the velocity.
     * @param newX the new x position
     * @param newY the new y position
     */
    public void reset(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    /**
     * Checks if the bullet is currently on the screen.
     * @param width the width of the screen
     * @param height the height of the screen
     * @return true if the bullet is on screen, false otherwise
     */
    public boolean isOnScreen(int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
