import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartScreen extends JPanel {
    private JButton startButton;
    private JButton howToPlayButton;
    private JButton exitButton;

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

        exitButton = new JButton("Exit");
        exitButton.addActionListener(exitAction);
        gbc.gridy = 3;
        add(exitButton, gbc);
    }
}