import java.awt.*;

public abstract class PowerUp {
    protected int x, y;
    protected int radius = 15; // default size

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void applyEffect(Tank tank); // Abstract effect

    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        g.setColor(Color.BLACK);
        g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
        drawSymbol(g); 
    }

    public boolean isCollectedBy(Tank tank) {
        int tankCenterX = tank.x + tank.width / 2;
        int tankCenterY = tank.y + tank.height / 2;
        double dist = Math.sqrt(Math.pow(tankCenterX - x, 2) + Math.pow(tankCenterY - y, 2));
        return dist < (radius + Math.max(tank.width, tank.height) / 2);
    }

    protected abstract Color getColor();

    protected abstract void drawSymbol(Graphics g);
}
