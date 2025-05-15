 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Bullet {
        private int x, y, width, height;

        public Bullet(int x) {
            this.x = x;
            this.y = 600 - 40;
            width = 5;
            height = 10;
        }

        public void move() {
            y -= 10;
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }
