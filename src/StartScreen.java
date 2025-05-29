import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the start screen of the game, providing navigation to start, instructions, or exit.
 */
public class StartScreen extends JPanel {
    private JButton startButton;
    private JButton howToPlayButton;
    private JButton exitButton;

    /**
     * Constructs the StartScreen with buttons for starting, instructions, and exiting.
     * @param startAction      the ActionListener for the Start Game button
     * @param howToPlayAction  the ActionListener for the How to Play button
     * @param exitAction       the ActionListener for the Exit button
     */
    public StartScreen(ActionListener startAction, ActionListener howToPlayAction, ActionListener exitAction) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Hills of Fire");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        startButton = new JButton("Start Game");
        startButton.addActionListener(startAction);
        gbc.gridy = 1;
        add(startButton, gbc);

        howToPlayButton = new JButton("How to Play");
        howToPlayButton.addActionListener(howToPlayAction);
        gbc.gridy = 2;
        add(howToPlayButton, gbc);

        exitButton = new JButton("Mr. Hare Button");
        exitButton.addActionListener(exitAction);
        gbc.gridy = 3;
        add(exitButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Sky gradient (deep blue to lighter blue)
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint skyGrad = new GradientPaint(
            0, 0, new Color(33, 64, 154), // deep blue
            0, getHeight(), new Color(135, 206, 250)); // sky blue
        g2.setPaint(skyGrad);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 2. Draw a pale moon or sun (top left)
        g.setColor(new Color(230, 230, 255, 180));
        g.fillOval(60, 50, 90, 90);

        // 3. Draw distant blue hills (layered parallax)
        // Back hill (darkest, farthest)
        g.setColor(new Color(22, 34, 84, 180)); // dark blue
        int[] x1 = {0, getWidth()/5, 2*getWidth()/5, 3*getWidth()/5, 4*getWidth()/5, getWidth()};
        int[] y1 = {getHeight(), getHeight()-140, getHeight()-120, getHeight()-130, getHeight()-110, getHeight()};
        g.fillPolygon(x1, y1, x1.length);

        // Middle hill (medium blue)
        g.setColor(new Color(45, 82, 170, 180));
        int[] x2 = {0, getWidth()/6, getWidth()/2, 5*getWidth()/6, getWidth()};
        int[] y2 = {getHeight(), getHeight()-80, getHeight()-100, getHeight()-70, getHeight()};
        g.fillPolygon(x2, y2, x2.length);

        // Front hill (lightest blue)
        g.setColor(new Color(120, 180, 240, 210));
        int[] x3 = {0, getWidth()/3, getWidth()/2, 2*getWidth()/3, getWidth()};
        int[] y3 = {getHeight(), getHeight()-40, getHeight()-60, getHeight()-45, getHeight()};
        g.fillPolygon(x3, y3, x3.length);

        // Optional: add a few faint white stars
        g.setColor(new Color(255, 255, 255, 120));
        for (int i = 0; i < 35; i++) {
            int starX = (int)(Math.random() * getWidth());
            int starY = (int)(Math.random() * (getHeight()/3));
            g.fillOval(starX, starY, 2, 2);
        }
    }


    /**
     * Gets the Start Game button.
     * @return the start button JButton
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * Gets the How to Play button.
     * @return the How to Play button JButton
     */
    public JButton getHowToPlayButton() {
        return howToPlayButton;
    }

    /**
     * Gets the Exit button.
     * @return the exit button JButton
     */
    public JButton getExitButton() {
        return exitButton;
    }
}
