import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static  int GROUND_HEIGHT = 10;
    
    private enum Screen {START, HOW_TO_PLAY, GAME_MODE, GAMEPLAY, END}
    private Screen currentScreen;
    private CardLayout cardLayout;

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
        
     // Initialize screens
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
     // Add screens
        add(createStartScreen(), "START");
        add(createHowToPlayScreen(), "HOW_TO_PLAY");
        add(createGameModeScreen(), "GAME_MODE");
        add(createGameplayScreen(), "GAMEPLAY");
        add(createEndScreen(), "END");
        
        showScreen(Screen.START);

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
    
    private JPanel createStartScreen() {
        JPanel startScreen = new JPanel(new GridBagLayout());
        startScreen.setBackground(Color.CYAN);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Hills of Fire");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        startScreen.add(titleLabel, gbc);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> showScreen(Screen.GAME_MODE));
        gbc.gridy = 1;
        startScreen.add(playButton, gbc);

        JButton howToPlayButton = new JButton("How to Play");
        howToPlayButton.addActionListener(e -> showScreen(Screen.HOW_TO_PLAY));
        gbc.gridy = 2;
        startScreen.add(howToPlayButton, gbc);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        startScreen.add(exitButton, gbc);

        return startScreen;
    }
    
    private JPanel createHowToPlayScreen() {
        JPanel howToPlayScreen = new JPanel(new BorderLayout());
        howToPlayScreen.setBackground(Color.LIGHT_GRAY);

        JTextArea instructionsArea = new JTextArea(
            "How to Play Hills of Fire:\n\n" +
            "1. Use A/D (Player 1) or Left/Right arrows (Player 2) to move tanks.\n" +
            "2. Adjust angle and power using sliders.\n" +
            "3. Press 'Fire' button to shoot.\n" +
            "4. Consider wind direction and strength.\n" +
            "5. Hit the enemy tank to reduce their lives.\n" +
            "6. Last tank standing wins!"
        );
        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setMargin(new Insets(10, 10, 10, 10));
        howToPlayScreen.add(new JScrollPane(instructionsArea), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> showScreen(Screen.START));
        howToPlayScreen.add(backButton, BorderLayout.SOUTH);

        return howToPlayScreen;
    }
    
    private JPanel createGameModeScreen() {
        JPanel gameModeScreen = new JPanel(new GridBagLayout());
        gameModeScreen.setBackground(Color.ORANGE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton pvpButton = new JButton("Player vs Player");
        pvpButton.addActionListener(e -> {
            resetGame();
            showScreen(Screen.GAMEPLAY);
        });
        gameModeScreen.add(pvpButton, gbc);

        JButton pveButton = new JButton("Player vs AI");
        pveButton.addActionListener(e -> {
            resetGame();
            // TODO: Implement AI logic
            showScreen(Screen.GAMEPLAY);
        });
        gbc.gridy = 1;
        gameModeScreen.add(pveButton, gbc);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showScreen(Screen.START));
        gbc.gridy = 2;
        gameModeScreen.add(backButton, gbc);

        return gameModeScreen;
    }
    
    private JPanel createGameplayScreen() {
        JPanel gameplayScreen = new JPanel(new BorderLayout());
        gameplayScreen.setBackground(Color.BLUE);

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Angle: "));
        controlPanel.add(angleSlider);
        controlPanel.add(new JLabel("Power: "));
        controlPanel.add(powerSlider);
        controlPanel.add(fireButton);
        gameplayScreen.add(controlPanel, BorderLayout.SOUTH);

        return gameplayScreen;
    }
    
    private JPanel createEndScreen() {
        JPanel endScreen = new JPanel(new GridBagLayout());
        endScreen.setBackground(Color.PINK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel winnerLabel = new JLabel();
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        endScreen.add(winnerLabel, gbc);

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> {
            resetGame();
            showScreen(Screen.GAMEPLAY);
        });
        gbc.gridy = 1;
        endScreen.add(playAgainButton, gbc);

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(e -> showScreen(Screen.START));
        gbc.gridy = 2;
        endScreen.add(mainMenuButton, gbc);

        return endScreen;
    }
    
    private void showScreen(Screen screen) {
        currentScreen = screen;
        cardLayout.show(this, screen.name());
        if (screen == Screen.GAMEPLAY) {
            requestFocusInWindow();
        }
    }
    
    

    private void resetGame() {
        player1 = new Tank(100, 0, Color.RED, 100);
        player2 = new Tank(700, 0, Color.ORANGE, 100);
        bullet = null;
        currentPlayer = 1;
        generateTerrain();
        wind = random.nextInt(21) - 10;
        gameOver = false;
        winner = null;
    }

    private void generateTerrain() {
        terrain = new int[WIDTH];
        terrain[0] = 100;
        boolean up = true;
        int count = 30;
        int last = terrain[0];
        for (int i = 1; i < WIDTH; i++) {
        	int add = 0;
        	if (up) {
        		add += (int) (Math.random()*2);
        	}
        	else {
        		add -= (int) (Math.random()*2);
        	}
        	
            terrain[i] = add + last;
            last += add;
            if (last < 50) {
            	last = 50;
            }
            count--;
            
            if (count == 0) {
            	int choose = (int)(Math.random() * 2);
            	if (choose == 0) {
            		up = true;
            	}
            	else {
            		up = false;
            	}
            	count = (int)(Math.random() * 60) - 29;
            }
            last += add;
            if (last < 50) {
            	last = 50;
            	up = true;
            }
            if (last > 300) {
            	last = 300;
            	up = false;
            }
        }
        player1.y = HEIGHT - terrain[player1.x] - player1.height;
        player2.y = HEIGHT - terrain[player2.x] - player2.height;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentScreen == Screen.GAMEPLAY) {
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
	        g.drawString("Player 1 Fuel: " + player1.energy, 10, 80);
	        g.drawString("Player 2 Fuel: " + player2.energy, 10, 100);
	
	        if (gameOver) {
	            g.setColor(Color.RED);
	            g.setFont(new Font("Arial", Font.BOLD, 30));
	            g.drawString(winner + " wins!", WIDTH/2 - 70, HEIGHT/2);
	        }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	requestFocusInWindow();
    	if (currentScreen == Screen.GAMEPLAY) {
	    	if (bullet != null) {
	        	bullet.move(wind);
	            checkCollision();
	       
	        }
	        moveTanks();
	        repaint();
    	}
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
    	if (player1.lives <= 0 || player2.lives <= 0) {
            gameOver = true;
            winner = "Player " + currentPlayer;
            endGame();
        }
        if (bullet.x <= 0 || bullet.x >= WIDTH-1) {
        	bullet = null;
            switchPlayer();
        } else if (bullet.y > HEIGHT - terrain[bullet.x]) {
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
        if (turn % 2 == 1) {
        	player2.energy = 100;
        }
        else if (turn % 2 == 0){
        	player1.energy = 100;
        }
        wind = random.nextInt(21) - 10;
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
        if (leftPressed2 && player2.x > 0 && turn % 2 == 0 && player2.energy > 0) {
            player2.x -= 2;
            player2.energy -= 2;
        }
        if (rightPressed2 && player2.x < WIDTH - player2.width && turn % 2 == 0 && player2.energy > 0) {
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
    
    private void endGame() {
        gameOver = true;
        JLabel winnerLabel = (JLabel) ((JPanel) getComponent(4)).getComponent(0);
        winnerLabel.setText(winner + " wins!");
        showScreen(Screen.END);
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