import java.awt.Color;
import java.awt.Graphics;

class Tank {
    int x, y, width, height, lives;
    Color color;

    public Tank(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 20;
        this.color = color;
        this.lives = 3;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}