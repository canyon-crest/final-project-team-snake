import java.awt.Color;
import java.awt.Graphics;

class EnemyTank extends Tank {
    int x, y, width, height, lives, energy;
    Color color;

    public EnemyTank(int x, int y, Color color, int energy) {
    	super(x,y,color,energy);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}