import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the end-of-game screen that displays the winner and allows the user to play again or return to the main menu.
 */
public class EndScreen extends JPanel {
    private JButton playAgainButton;
    private JButton mainMenuButton;

    /**
     * Constructs the EndScreen panel with play again and main menu buttons.
     * @param actionListener the ActionListener for button actions
     */
    public EndScreen(ActionListener actionListener) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gameOverLabel, gbc);

        JLabel winnerLabel = new JLabel();
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 1;
        add(winnerLabel, gbc);

        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(actionListener);
        gbc.gridy = 2;
        add(playAgainButton, gbc);

        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(actionListener);
        gbc.gridy = 3;
        add(mainMenuButton, gbc);
    }

    /**
    * Paints a custom night-themed background on the end screen.
    * This includes a deep blue gradient sky, a soft full moon, randomly placed stars,
    * and two layers of blue hills to create a calm night landscape.
    *
    * @param g the Graphics object used for drawing
    */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Deep blue to navy night sky
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint skyGrad = new GradientPaint(
            0, 0, new Color(18, 30, 85),           // Deep navy
            0, getHeight(), new Color(42, 60, 124)); // Blue
        g2.setPaint(skyGrad);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw a soft full moon
        g.setColor(new Color(230, 230, 255, 200));
        g.fillOval(getWidth()/2 - 70, 60, 100, 100);

        // Star field (random, but not too many)
        g.setColor(new Color(255,255,255,140));
        for (int i = 0; i < 45; i++) {
            int starX = (int)(Math.random() * getWidth());
            int starY = (int)(Math.random() * (getHeight()/2));
            g.fillOval(starX, starY, 2, 2);
        }

        // Darker distant hills
        g.setColor(new Color(33, 51, 98, 220));
        int[] x1 = {0, getWidth()/5, getWidth()/2, 4*getWidth()/5, getWidth()};
        int[] y1 = {getHeight(), getHeight()-130, getHeight()-115, getHeight()-140, getHeight()};
        g.fillPolygon(x1, y1, x1.length);

        // Foreground hill (almost black blue)
        g.setColor(new Color(20, 32, 53, 240));
        int[] x2 = {0, getWidth()/3, getWidth()/2, 2*getWidth()/3, getWidth()};
        int[] y2 = {getHeight(), getHeight()-55, getHeight()-65, getHeight()-60, getHeight()};
        g.fillPolygon(x2, y2, x2.length);
    }

    /**
     * Sets the winner label to display the name of the winner.
     * @param winner the name of the winning player
     */
    public void setWinner(String winner) {
        ((JLabel)getComponent(1)).setText(winner + " wins!");
    }

    /**
     * Resets the winner label to an empty string.
     * Useful when starting a new game or resetting the end screen.
     */
    public void resetWinnerLabel() {
        ((JLabel)getComponent(1)).setText("");
    }

    /**
     * Gets the "Play Again" button.
     * @return the play again JButton
     */
    public JButton getPlayAgainButton() {
        return playAgainButton;
    }

    /**
     * Gets the "Main Menu" button.
     * @return the main menu JButton
     */
    public JButton getMainMenuButton() {
        return mainMenuButton;
    }
}
