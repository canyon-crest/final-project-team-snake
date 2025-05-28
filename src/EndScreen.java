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
