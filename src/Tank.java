import java.awt.Color;
import java.awt.Graphics;

class Tank {
    int x, y, width, height, lives, energy;
    Color color;

    public Tank(int x, int y, Color color, int energy) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 20;
        this.color = color;
        this.lives = 3;
        this.energy = energy;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}