import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main game class for Hills of Fire.
 * Handles gameplay logic, rendering, and user input.
 */
public class Game extends JPanel implements ActionListener, KeyListener {
    // Main menu and card switching references
    private JPanel mainPanel;
    private CardLayout layout;
    public EndScreen endScreen;

    // Game display dimensions
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static int GROUND_HEIGHT = 10;

    // AI related fields
    private boolean aiMode = false;
    private enum Difficulty { EASY, MEDIUM, HARD }
    private Difficulty aiDifficulty = Difficulty.MEDIUM;
    private int aiMissCount = 0; // How many times the AI missed, used to make it 'learn'

    // Game objects and state
    private Timer timer;             // Game loop timer (60 FPS)
    private Tank player1, player2;   // The two tanks
    private static int turn = 1;     // Turn counter (odd = player1, even = player2)
    private Bullet bullet;           // The active bullet (if any)
    private int explosionTimer = 0;  // Timer for explosion animation
    private int explosionX = -1, explosionY = -1; // Explosion position
    private int currentPlayer;       // 1 or 2, whose turn is it

    private int[] terrain;           // Array holding the height of terrain at each x
    private int wind;                // Wind value affecting bullet movement
    private Random random;           // Random number generator

    private boolean gameOver;        // Has the game ended?
    private String winner;           // Who won

    private ArrayList<PowerUp> powerUps = new ArrayList<>();

    // Used to prevent drawing before setup
    private boolean it = false;

    // Graphics (can be replaced by custom images)
    private BufferedImage skyImage;
    private BufferedImage tankImage1, tankImage2;

    // Key press state for smooth tank movement
    private boolean leftPressed1 = false, rightPressed1 = false;
    private boolean leftPressed2 = false, rightPressed2 = false;

    // Controls for the human player(s)
    private JSlider angleSlider;
    private JSlider powerSlider;
    private JButton fireButton;
    
    private static int time = 0;

    /**
     * Main constructor with references for card layout navigation.
     */
    public Game(JPanel mainPanel, CardLayout layout, EndScreen endScreen) {
        // Image loading (disabled, uncomment to use images)
        loadImages();
        this.mainPanel = mainPanel;
        this.layout = layout;
        this.endScreen = endScreen;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        setLayout(new BorderLayout());

        random = new Random();
        resetGame();

        // UI controls for firing
        angleSlider = new JSlider(0, 180, 90);
        powerSlider = new JSlider(0, 100, 50);
        fireButton = new JButton("Fire!");
        fireButton.addActionListener(e -> fire());

        // Bottom control panel for firing controls
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Angle: "));
        controlPanel.add(angleSlider);
        controlPanel.add(new JLabel("Power: "));
        controlPanel.add(powerSlider);
        controlPanel.add(fireButton);
        add(controlPanel, BorderLayout.SOUTH);

        // Start the game loop timer
        timer = new Timer(1000 / 60, this); // 60 FPS
        timer.start();
        it = true;
    }

    /**
     * Default constructor (for testing).
     */
    public Game() {
        this(null, null, null);
    }

    /**
     * Resets the game state for a new game.
     */
    private void resetGame() {
        player1 = new Tank(100, 0, Color.RED, 100);
        player2 = new Tank(700, 0, Color.ORANGE, 100);
        bullet = null;
        explosionX = -1;
        explosionY = -1;
        explosionTimer = 0;
        currentPlayer = 1;
        generateTerrain();
        wind = random.nextInt(21) - 10; // Wind range: -10 to +10
        gameOver = false;
        winner = null;
    }

    /**
     * Generates a randomized rolling terrain using a smooth algorithm.
     */
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

        // Position tanks on top of the ground
        player1.y = HEIGHT - terrain[player1.x] - player1.height;
        player2.y = HEIGHT - terrain[player2.x] - player2.height;

        // Randomly add 1-3 power-ups on the hills
        powerUps.clear();
        int numPowerUps = 1 + random.nextInt(3); // 1 to 3 power-ups
        for (int i = 0; i < numPowerUps; i++) {
            int px = 50 + random.nextInt(WIDTH - 100);
            int py = HEIGHT - terrain[px] - 10;
            PowerUp powerUp;

            int type = random.nextInt(3); // 0, 1, or 2
            if (type == 0)
                powerUp = new HealthPowerUp(px, py);
            else if (type == 1)
                powerUp = new FuelPowerUp(px, py);
            else
                powerUp = new DoubleDamagePowerUp(px, py);

            powerUps.add(powerUp);
        }
    }


    /**
     * Paints the game scene, including terrain, tanks, bullet, wind, and explosion.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ---- 1. Draw blue sky gradient ----
        Graphics2D g2 = (Graphics2D) g;
        Paint oldPaint = g2.getPaint();
        GradientPaint skyGrad = new GradientPaint(0, 0, new Color(120, 170, 255), 0, HEIGHT, new Color(46, 83, 144));
        g2.setPaint(skyGrad);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setPaint(oldPaint);

        // ---- 2. Draw distant hills (optional parallax) ----
        g.setColor(new Color(40, 80, 170));
        int[] xHills = {0, 200, 400, 600, WIDTH};
        int[] yHills = {HEIGHT, HEIGHT - 200, HEIGHT - 150, HEIGHT - 220, HEIGHT};
        g.fillPolygon(xHills, yHills, xHills.length);

        // ---- 3. Draw terrain/ground ----
        g.setColor(new Color(9, 44, 87));
        java.awt.geom.GeneralPath ground = new java.awt.geom.GeneralPath();
        ground.moveTo(0, HEIGHT);
        for (int i = 0; i < WIDTH; i++) {
            ground.lineTo(i, HEIGHT - terrain[i]);
        }
        ground.lineTo(WIDTH, HEIGHT);
        ground.closePath();
        ((Graphics2D) g).fill(ground);

        // ---- 4. Draw power-ups first (so tanks sit on top) ----
        for (PowerUp p : powerUps) {
            p.draw(g);
        }

        // ---- 5. Draw tanks ----
        drawTank(g, player1);
        drawTank(g, player2);

        // ---- 6. Draw bullet ----
        if (bullet != null) {
            bullet.draw(g);
        }

        // ---- 7. Draw explosion (if any) ----
        if (explosionTimer > 0) {
            g.setColor(Color.ORANGE);
            g.fillOval(explosionX - 15, explosionY - 15, 30, 30);
        }

        // ---- 8. Draw wind info (center top) ----
        int windBoxW = 115, windBoxH = 32;
        int windBoxX = WIDTH/2 - windBoxW/2, windBoxY = 10;
        g.setColor(new Color(255,255,255,220));
        g.fillRoundRect(windBoxX, windBoxY, windBoxW, windBoxH, 18, 18);

        g.setColor(Color.BLUE.darker());
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Wind: " + wind, windBoxX + 8, windBoxY + 19);
        int arrowX1 = windBoxX + windBoxW/2;
        int arrowY1 = windBoxY + windBoxH - 10;
        int arrowX2 = arrowX1 + wind * 5;
        g.setColor(Color.RED);
        g.drawLine(arrowX1, arrowY1, arrowX2, arrowY1);
        g.fillOval(arrowX2 - 2, arrowY1 - 2, 5, 5);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        String windLabel = wind > 0 ? "→ Wind" : wind < 0 ? "← Wind" : "Wind";
        g.drawString(windLabel, windBoxX + windBoxW - 55, windBoxY + 26);

        // // ---- 9. Power-Up Legend (top right, separate from wind box) ----
        // int legendX = WIDTH - 135, legendY = 14;
        // g.setColor(new Color(255,255,255,230));
        // g.fillRoundRect(legendX - 9, legendY - 5, 118, 62, 13, 13);
        // g.setColor(Color.BLACK);
        // g.setFont(new Font("Arial", Font.BOLD, 13));
        // g.drawString("Power-Ups:", legendX, legendY + 10);

        // // Health
        // g.setColor(Color.RED);
        // g.fillOval(legendX, legendY + 18, 16, 16);
        // g.setColor(Color.WHITE);
        // g.setFont(new Font("Arial", Font.BOLD, 13));
        // g.drawString("+", legendX + 5, legendY + 30);
        // g.setColor(Color.BLACK);
        // g.setFont(new Font("Arial", Font.PLAIN, 11));
        // g.drawString("Health", legendX + 22, legendY + 30);

        // // Fuel
        // g.setColor(new Color(40, 180, 60));
        // g.fillOval(legendX, legendY + 36, 16, 16);
        // g.setColor(Color.BLACK);
        // g.setFont(new Font("Arial", Font.BOLD, 12));
        // g.drawString("F", legendX + 5, legendY + 48);
        // g.setFont(new Font("Arial", Font.PLAIN, 11));
        // g.drawString("Fuel", legendX + 22, legendY + 48);

        // // Double Damage (if used)
        // g.setColor(Color.YELLOW.darker());
        // g.fillOval(legendX, legendY + 54, 16, 16);
        // g.setColor(Color.BLACK);
        // g.setFont(new Font("Arial", Font.BOLD, 12));
        // g.drawString("D", legendX + 5, legendY + 66);
        // g.setFont(new Font("Arial", Font.PLAIN, 11));
        // g.drawString("Double", legendX + 22, legendY + 66);

        // ---- 10. HUD for Player 1 (left, vertical stack) ----
        int p1X = 15, p1Y = 42;
        g.setColor(new Color(255,255,255,210));
        g.fillRoundRect(p1X - 6, p1Y - 22, 110, 80, 13, 13);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("Player 1", p1X, p1Y);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString("Lives: " + player1.lives, p1X, p1Y + 18);
        g.drawString("Fuel: " + player1.energy, p1X, p1Y + 36);

        int statusY = p1Y + 54;
        if (player1.doubleDamage) {
            g.setColor(Color.YELLOW.darker());
            g.drawString("Double Dmg!", p1X, statusY);
            statusY += 16;
        }
        if (player1.shielded) {
            g.setColor(Color.BLUE);
            g.drawString("Shielded!", p1X, statusY);
        }

        // ---- 11. HUD for Player 2 (right, vertical stack) ----
        int p2X = WIDTH - 120, p2Y = 42;
        g.setColor(new Color(255,255,255,210));
        g.fillRoundRect(p2X - 6, p2Y - 22, 110, 80, 13, 13);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("Player 2", p2X, p2Y);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString("Lives: " + player2.lives, p2X, p2Y + 18);
        g.drawString("Fuel: " + player2.energy, p2X, p2Y + 36);

        int p2StatusY = p2Y + 54;
        if (player2.doubleDamage) {
            g.setColor(Color.YELLOW.darker());
            g.drawString("Double Dmg!", p2X, p2StatusY);
            p2StatusY += 16;
        }
        if (player2.shielded) {
            g.setColor(Color.BLUE);
            g.drawString("Shielded!", p2X, p2StatusY);
        }
    }

    /**
     * Main game loop. Called at each timer tick (60 times per second).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Ensure this panel has keyboard focus
        requestFocusInWindow();

        // AI acts if it's their turn, no bullet, and game not over
        if (aiMode && currentPlayer == 2 && bullet == null && !gameOver) {
            aiFire();
        }

        // Move bullet and handle collision
        if (bullet != null) {
            bullet.move(wind);
            checkCollision();
            if (gameOver) return;
        } else if (explosionTimer > 0) {
            explosionTimer--;
        }

        // Move tanks if keys held
        moveTanks();
        repaint();
        
        time++;
    }

    /**
     * Handles firing logic for the current player.
     */
    private void fire() {
        if (bullet == null) {
            int angle = angleSlider.getValue();
            int power = powerSlider.getValue();
            Tank currentTank = (currentPlayer == 1) ? player1 : player2;

            // Spawn bullet at tip of barrel
            bullet = new Bullet(currentTank.x + currentTank.width / 2, currentTank.y, angle, power);
        }
    }

    /**
     * Checks if the bullet hits terrain or a tank, or goes out of bounds.
     */
    private void checkCollision() {
        // If bullet hits ground or leaves screen, cause explosion and switch turn
        if (bullet.x <= 0 || bullet.x >= WIDTH - 1 || bullet.y > HEIGHT - terrain[bullet.x]) {
            if (bullet != null) {
                for (int i = Math.max(0, bullet.getX() - 10); i < Math.min(WIDTH, bullet.getX() + 10); i++) {
                    terrain[i] = Math.max(0, terrain[i] - 10); // Create crater
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

        // Tank hit detection and double damage logic
        Tank shooter = (currentPlayer == 1) ? player1 : player2;
        Tank targetTank = (currentPlayer == 1) ? player2 : player1;

        if (bullet.x >= targetTank.x && bullet.x <= targetTank.x + targetTank.width &&
            bullet.y >= targetTank.y && bullet.y <= targetTank.y + targetTank.height) {

            // Double damage check
            if (shooter.doubleDamage) {
                targetTank.lives -= 2;
                shooter.doubleDamage = false; // Power-up consumed after hit
            } else {
                targetTank.lives -= 1;
            }

            bullet = null;
            aiMissCount = 0; // Reset AI learning if hit

            if (targetTank.lives <= 0) {
                winner = (currentPlayer == 1) ? "Player 1" : "Player 2";
                endGame();
                return;
            }

            switchPlayer();
        }
    }

    /**
     * Handles game over: sets state, shows end screen if available.
     */
    private void endGame() {
        gameOver = true;
        if (endScreen != null && layout != null && mainPanel != null) {
            endScreen.setWinner(winner);
            layout.show(mainPanel, "END");
        }
    }

    /**
     * Set whether AI is enabled (true = AI Battle).
     */
    private void setAIMode(boolean ai) {
        this.aiMode = ai;
        this.aiDifficulty = Difficulty.MEDIUM; // Default
        this.aiMissCount = 0;
    }

    /**
     * Set AI difficulty by string ("easy", "medium", "hard").
     */
    public void setAIDifficulty(String level) {
        switch (level.toLowerCase()) {
            case "easy": aiDifficulty = Difficulty.EASY; break;
            case "hard": aiDifficulty = Difficulty.HARD; break;
            default: aiDifficulty = Difficulty.MEDIUM; break;
        }
    }

    /**
     * Switches to the next player, resets energy, and changes wind.
     */
    private void switchPlayer() {
        turn++;
        currentPlayer = (currentPlayer == 1) ? 2 : 1;

        // Restore fuel at start of turn
        if (turn % 2 == 1) {
            player2.energy = 100;
        } else {
            player1.energy = 100;
        }

        // Randomize wind
        wind = random.nextInt(21) - 10;
    }

    /**
     * AI logic to move, aim, and fire at player 1.
     */
    private void aiFire() {
        int direction = (player1.x < player2.x) ? -1 : 1;
        int distance = Math.abs(player1.x - player2.x);

        // AI tank tries to move closer if far away and has fuel
        if (player2.energy >= 2 && distance > 100) {
            int steps = Math.min(player2.energy / 2, 20);
            for (int i = 0; i < steps; i++) {
                int step = direction;
                if (player2.x + step >= 0 && player2.x + step <= WIDTH - player2.width) {
                    player2.x += step;
                    player2.energy -= 2;
                    player2.y = HEIGHT - terrain[player2.x] - player2.height;
                    try { Thread.sleep(5); } catch (InterruptedException ignored) {}
                } else {
                    break;
                }
            }
        }

        // Calculate angle to player 1
        int dx = player1.x - player2.x;
        int dy = player2.y - player1.y;
        double angleRad = Math.atan2(-dy, dx);
        int angle = (int) Math.toDegrees(angleRad);
        if (angle < 0) angle += 360;
        if (angle > 180) angle = 360 - angle;
        angle = Math.max(10, Math.min(angle, 170));

        // Add inaccuracy based on difficulty and misses
        int randomness = 0;
        switch (aiDifficulty) {
            case EASY: randomness = 2; break;   // least accurate
            case MEDIUM: randomness = 8; break;
            case HARD: randomness = 15; break;  // most accurate
        }
        randomness += aiMissCount * 1;

        int recoil = (aiDifficulty == Difficulty.HARD) ? 0 : (aiDifficulty == Difficulty.MEDIUM ? 5 : 10);
        int dxAdjusted = player1.x - player2.x;
        int dyAdjusted = player2.y - player1.y;
        double distanceToTarget = Math.sqrt(dxAdjusted * dxAdjusted + dyAdjusted * dyAdjusted);

        int playerMovementFactor = Math.abs(player1.x - dxAdjusted);
        int learnedCorrection = aiMissCount * 2;
        int power = (int) Math.min(100, Math.max(30,
            (distanceToTarget + playerMovementFactor * 0.1 + learnedCorrection + random.nextInt(randomness + 1 + recoil) - randomness / 2)));
        bullet = new Bullet(player2.x + player2.width / 2, player2.y, angle, power);
    }

    /**
     * Draws a tank at its current position (as an image or colored rectangle).
     */
    private void drawTank(Graphics g, Tank tank) {
        BufferedImage img = (tank == player1) ? tankImage1 : tankImage2;
        int angle = angleSlider.getValue();

        // Draw the tank body
        if (img != null) {
            g.drawImage(img, tank.x, tank.y, tank.width, tank.height, null);
        } else {
            g.setColor(tank.color);
            g.fillRect(tank.x, tank.y, tank.width, tank.height);
        }

        // Draw the tank barrel
        int baseX = tank.x + tank.width / 2;
        int baseY = tank.y + 5;
        double radians = Math.toRadians(angle);
        int barrelLength = 20;
        int endX = baseX + (int)(barrelLength * Math.cos(radians));
        int endY = baseY - (int)(barrelLength * Math.sin(radians));

        Graphics2D g2d = (Graphics2D) g;
        Stroke old = g2d.getStroke();
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(4)); // thicker barrel
        g2d.drawLine(baseX, baseY, endX, endY);
        g2d.setStroke(old);
    }

    /**
     * Loads image assets.
     */
    private void loadImages() {
        try {
             skyImage = ImageIO.read(getClass().getResource("/sky.png"));
             tankImage1 = ImageIO.read(getClass().getResource("/tank1.png"));
             tankImage2 = ImageIO.read(getClass().getResource("/tank2.png"));
//        } catch (IllegalArgumentException e) {
//            System.out.println("Could not load images.");
        } catch (IOException e) {
			// TODO Auto-generated catch block
        	System.out.println("Could not load images.");		}
    }

    /**
     * Moves tanks if movement keys are pressed, reducing their fuel.
     */
    private void moveTanks() {
        if (leftPressed1 && player1.x > 0 && turn % 2 == 1 && player1.energy > 0) {
            player1.x -= 2;
            player1.energy -= 2;
        }
        if (rightPressed1 && player1.x < WIDTH - player1.width && turn % 2 == 1 && player1.energy > 0) {
            player1.x += 2;
            player1.energy -= 2;
        }
        if (leftPressed2 && player2.x > 0 && turn % 2 == 0 && player2.energy > 0) {
            player2.x -= 2;
            player2.energy -= 2;
        }
        if (rightPressed2 && player2.x < WIDTH - player2.width && turn % 2 == 0 && player2.energy > 0) {
            player2.x += 2;
            player2.energy -= 2;
        }

        // Reposition tanks to sit on ground after moving
        player1.y = HEIGHT - terrain[player1.x] - player1.height;
        player2.y = HEIGHT - terrain[player2.x] - player2.height;

        // Power-up collection check (add this here)
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            boolean collected = false;
            if (p.isCollectedBy(player1)) {
                p.applyEffect(player1);
                collected = true;
            } else if (p.isCollectedBy(player2)) {
                p.applyEffect(player2);
                collected = true;
            }
            if (collected) {
                powerUps.remove(i);
            }
        }
    }

    // Keyboard controls: update movement key flags and reset on 'R'
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

    /**
     * Main entry point for the program. Creates window and screens.
     */
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
                }
            } else if (source.getText().equals("1 vs 1")) {
                gamePanel.resetGame();
                gamePanel.setAIMode(false); // ensure pure PvP
                layout.show(mainPanel, "GAME");
            } else {
                layout.show(mainPanel, "START");
            }
        });

        // Add all screens to the main card panel
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
