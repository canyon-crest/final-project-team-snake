import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static  int GROUND_HEIGHT = 10;

    private Timer timer;
    private Tank player1;
    private Tank player2;
    private static int turn = 1;
    private Bullet bullet;
    private int currentPlayer;
    private int[] terrain;
    private int wind;
    private Random random;
    private boolean gameOver;
    private String winner;
    
    private boolean leftPressed1 = false;
    private boolean rightPressed1 = false;
    private boolean leftPressed2 = false;
    private boolean rightPressed2 = false;

    private JSlider angleSlider;
    private JSlider powerSlider;
    private JButton fireButton;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);

        random = new Random();
        resetGame();

        angleSlider = new JSlider(0, 90, 45);
        powerSlider = new JSlider(0, 100, 50);
        fireButton = new JButton("Fire!");
        fireButton.addActionListener(e -> fire());

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Angle:"));
        controlPanel.add(angleSlider);
        controlPanel.add(new JLabel("Power:"));
        controlPanel.add(powerSlider);
        controlPanel.add(fireButton);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.SOUTH);

        timer = new Timer(1000 / 60, this);
        timer.start();
    }

    private void resetGame() {
        player1 = new Tank(100, 0, Color.RED, 20);
        player2 = new Tank(700, 0, Color.ORANGE, 20);
        bullet = null;
        currentPlayer = 1;
        generateTerrain();
        wind = random.nextInt(21) - 10;
        gameOver = false;
        winner = null;
    }

    private void generateTerrain() {
        terrain = new int[WIDTH];
        terrain[0] = 50;
        for (int i = 1; i < WIDTH; i++) {
           // terrain[i] = random.nextInt(GROUND_HEIGHT) + 50;
            //GROUND_HEIGHT = terrain[i-1];
            terrain[i] = 50;
        }
        player1.y = HEIGHT - terrain[player1.x] - player1.height;
        player2.y = HEIGHT - terrain[player2.x] - player2.height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw terrain
        g.setColor(Color.GREEN);
        for (int i = 0; i < WIDTH; i++) {
            g.drawLine(i, HEIGHT - terrain[i], i, HEIGHT);
        }

        // Draw tanks
        player1.draw(g);
        player2.draw(g);

        // Draw shell
        if (bullet != null) {
        	bullet.draw(g);
        }

        // Draw wind indicator
        g.setColor(Color.BLACK);
        g.drawString("Wind: " + wind, 10, 20);

        // Draw player lives
        g.drawString("Player 1 Lives: " + player1.lives, 10, 40);
        g.drawString("Player 2 Lives: " + player2.lives, 10, 60);
//        g.drawString("Player 1 Energy: " + player1.energy, 10, 80);
//        g.drawString("Player 2 Energy: " + player2.energy, 10, 100);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(winner + " wins!", WIDTH/2 - 70, HEIGHT/2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	requestFocusInWindow();
    	if (bullet != null) {
        	bullet.move(wind);
            checkCollision();
       
        }
        moveTanks();
        repaint();
    }

    private void fire() {
        if (bullet == null) {
            int angle = angleSlider.getValue();
            int power = powerSlider.getValue();
            Tank currentTank = (currentPlayer == 1) ? player1 : player2;
            if (currentTank == player2) {
            	angle = 180 - angle;
            }
           bullet = new Bullet(currentTank.x + currentTank.width/2, currentTank.y, angle, power);
        }
    }

    private void checkCollision() {
        if (bullet.y > HEIGHT - terrain[bullet.x]) {
        	bullet = null;
            switchPlayer();
        } else if (bullet.x < 0 || bullet.x > WIDTH) {
        	bullet = null;
            switchPlayer();
        } else {
            Tank targetTank = (currentPlayer == 1) ? player2 : player1;
            if (bullet.x >= targetTank.x && bullet.x <= targetTank.x + targetTank.width &&
            		bullet.y >= targetTank.y && bullet.y <= targetTank.y + targetTank.height) {
                targetTank.lives--;
                bullet = null;
                if (targetTank.lives <= 0) {
                    gameOver = true;
                    winner = "Player " + currentPlayer;
                }
                switchPlayer();
            }
        }
    }

    private void switchPlayer() {
    	turn++;
    	System.out.println(turn);
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        wind = random.nextInt(21) - 10;
    }
    
    private void moveTanks() {
        if (leftPressed1 && player1.x > 0 && turn % 2 == 1) {
            player1.x -= 2;
            player1.energy -= 2;
        }
        if (rightPressed1 && player1.x < WIDTH - player1.width && turn % 2 == 1) {
            player1.x += 2;
            player1.energy -= 2;
        }
        if (leftPressed2 && player2.x > 0 && turn % 2 == 0) {
            player2.x -= 2;
            player2.energy -= 2;
        }
        if (rightPressed2 && player2.x < WIDTH - player2.width && turn % 2 == 0) {
            player2.x += 2;
            player2.energy -= 2;
        }

        // Adjust tank y position based on terrain
        player1.y = HEIGHT - terrain[player1.x] - player1.height;
        player2.y = HEIGHT - terrain[player2.x] - player2.height;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A: 
                leftPressed1 = true;
                break;
            case KeyEvent.VK_D: 
                rightPressed1 = true;
                break;
            case KeyEvent.VK_LEFT: 
                leftPressed2 = true;
                break;
            case KeyEvent.VK_RIGHT: 
                rightPressed2 = true;
                System.out.println("right presed");
                break;
            case KeyEvent.VK_R: 
                resetGame();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A: 
                leftPressed1 = false;
                break;
            case KeyEvent.VK_D: 
                rightPressed1 = false;
                break;
            case KeyEvent.VK_LEFT: 
                leftPressed2 = false;
                break;
            case KeyEvent.VK_RIGHT: 
                rightPressed2 = false;
                break;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hills of Fire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Game());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}