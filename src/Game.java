import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Tank tank;
    private ArrayList<Bullet> projectiles;
    private ArrayList<EnemyTank> targets;
    private Random random;
    private int score;

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        tank = new Tank();
        projectiles = new ArrayList<>();
        targets = new ArrayList<>();
        random = new Random();
        score = 0;

        timer = new Timer(1000 / 60, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        tank.draw(g);
        for (Bullet p : projectiles) {
            p.draw(g);
        }
        for (EnemyTank t : targets) {
            t.draw(g);
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tank.move();
        moveProjectiles();
        moveTargets();
        checkCollisions();
        spawnTargets();
        repaint();
    }

    private void moveProjectiles() {
        Iterator<Bullet> it = projectiles.iterator();
        while (it.hasNext()) {
            Bullet p = it.next();
            p.move();
            if (p.getY() < 0) {
                it.remove();
            }
        }
    }

    private void moveTargets() {
        for (EnemyTank t : targets) {
            t.move();
        }
    }

    private void checkCollisions() {
        Iterator<Bullet> pIt = projectiles.iterator();
        while (pIt.hasNext()) {
            Bullet p = pIt.next();
            Iterator<EnemyTank> tIt = targets.iterator();
            while (tIt.hasNext()) {
                EnemyTank t = tIt.next();
                if (p.getBounds().intersects(t.getBounds())) {
                    pIt.remove();
                    tIt.remove();
                    score += 10;
                    break;
                }
            }
        }
    }

    private void spawnTargets() {
        if (random.nextInt(100) < 2) {
            targets.add(new EnemyTank(random.nextInt(getWidth())));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            tank.setDx(-5);
        } else if (key == KeyEvent.VK_RIGHT) {
            tank.setDx(5);
        } else if (key == KeyEvent.VK_SPACE) {
            projectiles.add(new Bullet(tank.getX() + tank.getWidth() / 2));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            tank.setDx(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tank Shooting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Game());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}