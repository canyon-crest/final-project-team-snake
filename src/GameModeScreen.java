import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the game mode selection screen,
 * allowing the user to choose between AI Battle, 1 vs 1, or go back.
 */
public class GameModeScreen extends JPanel {
    private JButton aiModeButton;
    private JButton twoPlayerModeButton;
    private JButton backButton;

    /**
     * Constructs the GameModeScreen with mode selection and navigation buttons.
     * @param actionListener the ActionListener for button actions
     */
    public GameModeScreen(ActionListener actionListener) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Select Game Mode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        aiModeButton = new JButton("AI Battle");
        aiModeButton.addActionListener(actionListener);
        gbc.gridy = 1;
        add(aiModeButton, gbc);

        twoPlayerModeButton = new JButton("1 vs 1");
        twoPlayerModeButton.addActionListener(actionListener);
        gbc.gridy = 2;
        add(twoPlayerModeButton, gbc);

        backButton = new JButton("Back");
        backButton.addActionListener(actionListener);
        gbc.gridy = 3;
        add(backButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Blue gradient sky
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint skyGrad = new GradientPaint(
            0, 0, new Color(33, 64, 154),        // Deep blue at top
            0, getHeight(), new Color(135, 206, 250)); // Lighter blue at bottom
        g2.setPaint(skyGrad);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Pale moon (top left)
        g.setColor(new Color(230, 230, 255, 180));
        g.fillOval(60, 50, 90, 90);

        // Distant dark blue hills
        g.setColor(new Color(22, 34, 84, 180)); // Farthest, darkest
        int[] x1 = {0, getWidth()/5, 2*getWidth()/5, 3*getWidth()/5, 4*getWidth()/5, getWidth()};
        int[] y1 = {getHeight(), getHeight()-140, getHeight()-120, getHeight()-130, getHeight()-110, getHeight()};
        g.fillPolygon(x1, y1, x1.length);

        // Middle hill
        g.setColor(new Color(45, 82, 170, 180));
        int[] x2 = {0, getWidth()/6, getWidth()/2, 5*getWidth()/6, getWidth()};
        int[] y2 = {getHeight(), getHeight()-80, getHeight()-100, getHeight()-70, getHeight()};
        g.fillPolygon(x2, y2, x2.length);

        // Foreground hill
        g.setColor(new Color(120, 180, 240, 210));
        int[] x3 = {0, getWidth()/3, getWidth()/2, 2*getWidth()/3, getWidth()};
        int[] y3 = {getHeight(), getHeight()-40, getHeight()-60, getHeight()-45, getHeight()};
        g.fillPolygon(x3, y3, x3.length);

        // Faint stars in the sky
        g.setColor(new Color(255, 255, 255, 120));
        for (int i = 0; i < 35; i++) {
            int starX = (int)(Math.random() * getWidth());
            int starY = (int)(Math.random() * (getHeight()/3));
            g.fillOval(starX, starY, 2, 2);
        }
    }


    /**
     * Gets the AI Battle mode button.
     * @return the aiModeButton JButton
     */
    public JButton getAIModeButton() {
        return aiModeButton;
    }

    /**
     * Gets the 1 vs 1 mode button.
     * @return the twoPlayerModeButton JButton
     */
    public JButton getTwoPlayerModeButton() {
        return twoPlayerModeButton;
    }

    /**
     * Gets the Back button.
     * @return the backButton JButton
     */
    public JButton getBackButton() {
        return backButton;
    }
}
