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
