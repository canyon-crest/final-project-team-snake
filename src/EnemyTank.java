import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class EnemyTank {
        private int x, y, width, height;

        public EnemyTank(int x) {
            this.x = x;
            this.y = 0;
            width = 30;
            height = 30;
        }

        public void move() {
            y += 2;
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }