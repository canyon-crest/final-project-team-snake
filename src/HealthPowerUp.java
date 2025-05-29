import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * HealthPowerUp represents a red power-up that gives a tank +1 life when collected.
 */
public class HealthPowerUp extends PowerUp {
    /**
     * Constructs a HealthPowerUp at the specified position.
     * @param x the x-coordinate of the power-up
     * @param y the y-coordinate of the power-up
     */
    public HealthPowerUp(int x, int y) {
        super(x, y);
    }

    /**
     * Applies the effect to the specified tank by increasing its lives by one.
     * @param tank the tank that collects the power-up
     */
    @Override
    public void applyEffect(Tank tank) {
        tank.lives++;
    }

    /**
     * Gets the display color for this power-up (red).
     * @return the Color for drawing
     */
    @Override
    protected Color getColor() {
        return Color.RED;
    }

    /**
     * Draws a "+" symbol and "HP" label on or near the power-up.
     * @param g the Graphics object used for drawing
     */
    @Override
    protected void drawSymbol(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("+", x - 4, y + 6);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("HP", x - 12, y + radius + 12);
    }
}
