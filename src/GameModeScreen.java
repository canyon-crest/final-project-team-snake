import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameModeScreen extends JPanel {
    private JButton aiModeButton;
    private JButton twoPlayerModeButton;
    private JButton backButton;

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

        twoPlayerModeButton = new JButton("2 vs 2");
        twoPlayerModeButton.addActionListener(actionListener);
        gbc.gridy = 2;
        add(twoPlayerModeButton, gbc);

        backButton = new JButton("Back");
        backButton.addActionListener(actionListener);
        gbc.gridy = 3;
        add(backButton, gbc);
    }
}