import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EndScreen extends JPanel {
    private JButton playAgainButton;
    private JButton mainMenuButton;

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

    public void setWinner(String winner) {
        ((JLabel)getComponent(1)).setText(winner + " wins!");
    }
}