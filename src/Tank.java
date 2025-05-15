import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random; 

public class Tank {
        private int x, y, width, height, dx;

        public Tank() {
            width = 50;
            height = 30;
            x = (800 - width) / 2;
            y = 600 - height - 10;
            dx = 0;
        }

        public void move() {
            x += dx;
            x = Math.max(0, Math.min(x, 800 - width));
        }

        public void draw(Graphics g) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }

        public void setDx(int dx) {
            this.dx = dx;
        }

        public int getX() {
            return x;
        }

        public int getWidth() {
            return width;
        }
    }