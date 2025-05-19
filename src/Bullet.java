import java.awt.Color;
import java.awt.Graphics;

class Bullet {
    int x, y;
    double dx, dy;
    final double GRAVITY = 0.1;

    public Bullet(int x, int y, int angle, int power) {
        this.x = x;
        this.y = y;
        double radian = Math.toRadians(angle);
        dx = Math.cos(radian) * power / 10;
        dy = -Math.sin(radian) * power / 10;
    }

    public void move(int wind) {
    	x += dx + wind / 10.0;
	    y += dy;
	    dy += GRAVITY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(x - 3, y - 3, 6, 6);
    }
}