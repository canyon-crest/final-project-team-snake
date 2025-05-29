import java.awt.*;

public class FuelPowerUp extends PowerUp {
    public FuelPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.energy = Math.min(100, tank.energy + 40); // Give 40 fuel, max 100
    }

    @Override
    protected void drawSymbol(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("F", x - 4, y + 6);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Fuel", x - 16, y + radius + 12);
    }

    @Override
    protected Color getColor() {
        return Color.GREEN;
    }
}
