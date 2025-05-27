// --- Game.java ---
// Full implementation with errors fixed

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends JPanel implements ActionListener, KeyListener {
    private JPanel mainPanel;
    private CardLayout layout;
    public EndScreen endScreen;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static int GROUND_HEIGHT = 10;
    private boolean aiMode = false;
    private enum Difficulty { EASY, MEDIUM, HARD }
    private Difficulty aiDifficulty = Difficulty.MEDIUM;
    private int aiMissCount = 0;

    private Timer timer;
    private Tank player1;
    private Tank player2;
    private static int turn = 1;
    private Bullet bullet;
    private int explosionTimer = 0;
    private int explosionX = -1, explosionY = -1;
    private int currentPlayer;
    private int[] terrain;
    private int wind;
    private Random random;
    private boolean gameOver;
    private String winner;
    private boolean it = false;

    private BufferedImage skyImage;
    private BufferedImage tankImage1;
    private BufferedImage tankImage2;

    private boolean leftPressed1 = false;
    private boolean rightPressed1 = false;
    private boolean leftPressed2 = false;
    private boolean rightPressed2 = false;

    private JSlider angleSlider;
    private JSlider powerSlider;
    private JButton fireButton;

    public Game(JPanel mainPanel, CardLayout layout, EndScreen endScreen) {
        // loadImages();
        this.mainPanel = mainPanel;
        this.layout = layout;
        this.endScreen = endScreen;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        setLayout(new BorderLayout());

        random = new Random();
        resetGame();

        angleSlider = new JSlider(0, 180, 90);
        powerSlider = new JSlider(0, 100, 50);
        fireButton = new JButton("Fire!");
        fireButton.addActionListener(e -> fire());

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Angle: "));
        controlPanel.add(angleSlider);
        controlPanel.add(new JLabel("Power: "));
        controlPanel.add(powerSlider);
        controlPanel.add(fireButton);
        add(controlPanel, BorderLayout.SOUTH);

        timer = new Timer(1000 / 60, this);
        timer.start();
        it = true;
    }

    public Game() {
        this(null, null, null);
    }

    private void resetGame() {
        player1 = new Tank(100, 0, Color.RED, 100);
        player2 = new Tank(700, 0, Color.ORANGE, 100);
        bullet = null;
        explosionX = -1;
        explosionY = -1;
        explosionTimer = 0;
        currentPlayer = 1;
        generateTerrain();
        wind = random.nextInt(21) - 10;
        gameOver = false;
        winner = null;
        if (aiMode) {
        	player1.x = 700;
        	player2.x = 100;
        }
    }

    private void generateTerrain() {
        terrain = new int[WIDTH];
        terrain[0] = 200;
        double smoothness = 1.0;
        for (int i = 1; i < WIDTH; i++) {
            double variation = (random.nextDouble() - 0.5) * 10;
            double slope = (terrain[i - 1] - (i >= 2 ? terrain[i - 2] : 200)) * 0.5;
            terrain[i] = terrain[i - 1] + (int)(variation - slope * 0.3);
            if (terrain[i] < 100) terrain[i] = 100;
            if (terrain[i] > 300) terrain[i] = 300;
        }

        player1.y = HEIGHT - terrain[player1.x] - player1.height;
        player2.y = HEIGHT - terrain[player2.x] - player2.height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (skyImage != null) {
            g.drawImage(skyImage, 0, 0, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        if (it) {
            g.setColor(Color.GREEN);
            for (int i = 0; i < WIDTH; i++) {
                g.drawLine(i, HEIGHT - terrain[i], i, HEIGHT);
            }

            drawTank(g, player1);
            drawTank(g, player2);

            if (bullet != null) {
                bullet.draw(g);
            }

            // Debug line for AI aim
            if (aiMode && currentPlayer == 2 && bullet == null) {
                int dx = player1.x - player2.x;
                int dy = player2.y - player1.y;
                double angleRad = Math.atan2(-dy, dx);
        int angle = (int) Math.toDegrees(angleRad);
        if (angle < 0) angle += 360;
        angle = Math.max(10, Math.min(angle, 170));
        angle = Math.max(10, Math.min(angle, 80));
                int power = 60;
                double radians = Math.toRadians(angle);
                int x1 = player2.x + player2.width / 2;
                int y1 = player2.y;
                int x2 = x1 + (int)(Math.cos(radians) * power * 1.5);
                int y2 = y1 - (int)(Math.sin(radians) * power * 1.5);
                g.setColor(Color.MAGENTA);
                g.drawLine(x1, y1, x2, y2);
            }

            if (explosionTimer > 0) {
                g.setColor(Color.ORANGE);
                g.fillOval(explosionX - 10, explosionY - 10, 20, 20);
            }

            g.setColor(Color.BLACK);
            g.drawString("Wind: " + wind, 10, 20);
            g.drawString("Player 1 Lives: " + player1.lives, 10, 40);
            g.drawString("Player 2 Lives: " + player2.lives, 10, 60);
            g.drawString("Player 1 Fuel: " + player1.energy, 10, 80);
            g.drawString("Player 2 Fuel: " + player2.energy, 10, 100);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        requestFocusInWindow();

        if (aiMode && currentPlayer == 2 && bullet == null && !gameOver) {
            aiFire();
        }

        if (bullet != null) {
            bullet.move(wind);
            checkCollision();
            if (gameOver) return;
        } else if (explosionTimer > 0) {
            explosionTimer--;
        }

        moveTanks();
        repaint();
    }

    private void fire() {
        if (bullet == null) {
            int angle = angleSlider.getValue();
            int power = powerSlider.getValue();
            Tank currentTank = (currentPlayer == 1) ? player1 : player2;

            

            bullet = new Bullet(currentTank.x + currentTank.width / 2, currentTank.y, angle, power);
        }
    }

    private void checkCollision() {
        if (bullet.x <= 0 || bullet.x >= WIDTH - 1 || bullet.y > HEIGHT - terrain[bullet.x]) {
            if (bullet != null) {
            for (int i = Math.max(0, bullet.getX() - 10); i < Math.min(WIDTH, bullet.getX() + 10); i++) {
                terrain[i] = Math.max(0, terrain[i] - 10);
            }
            explosionX = bullet.getX();
            explosionY = bullet.getY();
            explosionTimer = 15;
        }
        bullet = null;
        if (aiMode && currentPlayer == 2) aiMissCount++;
            switchPlayer();
            return;
        }

        Tank targetTank = (currentPlayer == 1) ? player2 : player1;
        if (bullet.x >= targetTank.x && bullet.x <= targetTank.x + targetTank.width &&
            bullet.y >= targetTank.y && bullet.y <= targetTank.y + targetTank.height) {

            targetTank.lives--;
            bullet = null;
            aiMissCount = 0;

            if (targetTank.lives <= 0) {
                winner = (currentPlayer == 1) ? "Player 1" : "Player 2";
                endGame();
                return;
            }

            switchPlayer();
        }
    }

    private void endGame() {
        gameOver = true;
        if (endScreen != null && layout != null && mainPanel != null) {
            endScreen.setWinner(winner);
            layout.show(mainPanel, "END");
        }
    }

    private void setAIMode(boolean ai) {
        this.aiMode = ai;
        this.aiDifficulty = Difficulty.MEDIUM; // Default
        this.aiMissCount = 0;
    }

    public void setAIDifficulty(String level) {
        switch (level.toLowerCase()) {
            case "easy": aiDifficulty = Difficulty.EASY; break;
            case "hard": aiDifficulty = Difficulty.HARD; break;
            default: aiDifficulty = Difficulty.MEDIUM; break;
        }
    }

    private void switchPlayer() {
        turn++;
        currentPlayer = (currentPlayer == 1) ? 2 : 1;

        if (turn % 2 == 1) {
            player2.energy = 100;
        } else {
            player1.energy = 100;
        }

        wind = random.nextInt(21) - 10;
    }

    private void aiFire() {
        int direction = (player1.x < player2.x) ? -1 : 1;
        int distance = Math.abs(player1.x - player2.x);

        if (player2.energy >= 2 && distance > 100) {
            int steps = Math.min(player2.energy / 2, 20);
            for (int i = 0; i < steps; i++) {
                int step = direction;
                if (player2.x + step >= 0 && player2.x + step <= WIDTH - player2.width) {
                    player2.x += step;
                    player2.energy -= 2;
                    player2.y = HEIGHT - terrain[player2.x] - player2.height;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    break;
                }
            }
        }

        int dx = player2.x - player1.x;
        int dy = player2.y - player1.y;
        double angleRad = Math.atan2(dx, dy);
        int angle = (int) Math.toDegrees(angleRad);
        if (angle < 0) angle += 360;
        angle = Math.max(10, Math.min(angle, 80));

        int randomness = 0;
        switch (aiDifficulty) {
            case EASY: randomness = 10; break;
            case MEDIUM: randomness = 5; break;
            case HARD: randomness = 2; break;
        }
        randomness += aiMissCount * 2;

        int recoil = (aiDifficulty == Difficulty.HARD) ? 0 : (aiDifficulty == Difficulty.MEDIUM ? 5 : 10);
        int power = (int) Math.min(100, Math.max(30,
            (distance + wind * 2) / 2.0 + aiMissCount + random.nextInt(randomness + 1 + recoil) - randomness / 2));
        bullet = new Bullet(player2.x + player2.width / 2, player2.y, angle, power);
    }


    
    private void drawTank(Graphics g, Tank tank) {
        BufferedImage img = (tank == player1) ? tankImage1 : tankImage2;
        int angle = angleSlider.getValue();

        if (img != null) {
            g.drawImage(img, tank.x, tank.y, tank.width, tank.height, null);
        } else {
            g.setColor(tank.color);
            g.fillRect(tank.x, tank.y, tank.width, tank.height);
        }

        int baseX = tank.x + tank.width / 2;
        int baseY = tank.y + 5;
        double radians = Math.toRadians(angle);
        int barrelLength = 20;
        int endX = baseX + (int)(barrelLength * Math.cos(radians));
        int endY = baseY - (int)(barrelLength * Math.sin(radians));

        g.setColor(Color.DARK_GRAY);
        g.drawLine(baseX, baseY, endX, endY);
        }

    private void loadImages() {
        try {
            // skyImage = ImageIO.read(getClass().getResource("/sky.png"));
            // tankImage1 = ImageIO.read(getClass().getResource("/tank1.png"));
            // tankImage2 = ImageIO.read(getClass().getResource("/tank2.png"));
        } catch (IllegalArgumentException e) {
            System.out.println("Could not load one or more images.");
        }
    }

    private void moveTanks() {
        if (leftPressed1 && player1.x > 0 && turn % 2 == 1 && player1.energy > 0) {
            player1.x -= 2;
            player1.energy -= 2;
        }
        if (rightPressed1 && player1.x < WIDTH - player1.width && turn % 2 == 1 && player1.energy > 0) {
            player1.x += 2;
            player1.energy -= 2;
        }
        if (!aiMode && leftPressed2 && player2.x > 0 && turn % 2 == 0 && player2.energy > 0) {
            player2.x -= 2;
            player2.energy -= 2;
        }
        if (!aiMode && rightPressed2 && player2.x < WIDTH - player2.width && turn % 2 == 0 && player2.energy > 0) {
            player2.x += 2;
            player2.energy -= 2;
        }

        player1.y = HEIGHT - terrain[player1.x] - player1.height;
        player2.y = HEIGHT - terrain[player2.x] - player2.height;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A: leftPressed1 = true; break;
            case KeyEvent.VK_D: rightPressed1 = true; break;
            case KeyEvent.VK_LEFT: leftPressed2 = true; break;
            case KeyEvent.VK_RIGHT: rightPressed2 = true; break;
            case KeyEvent.VK_R: resetGame(); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A: leftPressed1 = false; break;
            case KeyEvent.VK_D: rightPressed1 = false; break;
            case KeyEvent.VK_LEFT: leftPressed2 = false; break;
            case KeyEvent.VK_RIGHT: rightPressed2 = false; break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hills of Fire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);

        CardLayout layout = new CardLayout();
        JPanel mainPanel = new JPanel(layout);

        Game gamePanel = new Game(mainPanel, layout, null);
        EndScreen endScreen = new EndScreen(e -> {
            gamePanel.resetGame();
            gamePanel.setAIMode(false);
            layout.show(mainPanel, "START");
        });
        gamePanel.endScreen = endScreen;

        StartScreen startScreen = new StartScreen(
            e -> layout.show(mainPanel, "MODE"),
            e -> layout.show(mainPanel, "HOW"),
            e -> System.exit(0)
        );
        HowToPlayScreen howToPlayScreen = new HowToPlayScreen(
            e -> layout.show(mainPanel, "START")
        );
        GameModeScreen gameModeScreen = new GameModeScreen(e -> {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("AI Battle")) {
                String[] options = {"Easy", "Medium", "Hard"};
                String choice = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select AI Difficulty:",
                    "AI Settings",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    "Medium"
                );
                if (choice != null) {
                    gamePanel.setAIMode(true);
                    gamePanel.setAIDifficulty(choice);
                    layout.show(mainPanel, "GAME");
                    gamePanel.resetGame();
                }
            } else if (source.getText().equals("1 vs 1")) {
                gamePanel.resetGame();
                layout.show(mainPanel, "GAME");
            } else {
                layout.show(mainPanel, "START");
            }
        });

        mainPanel.add(startScreen, "START");
        mainPanel.add(howToPlayScreen, "HOW");
        mainPanel.add(gameModeScreen, "MODE");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(endScreen, "END");

        frame.add(mainPanel);
        layout.show(mainPanel, "START");

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
