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
