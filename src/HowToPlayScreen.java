import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HowToPlayScreen extends JPanel {
    private JButton backButton;

    public HowToPlayScreen(ActionListener backAction) {
        setLayout(new BorderLayout());

        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setEditable(false);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);
        instructionsArea.setText("How to Play Hills of Fire:\n\n" +
                "1. Use arrow keys to move your tank left and right.\n" +
                "2. Adjust the angle and power of your shot using the sliders.\n" +
                "3. Click 'Fire!' to launch your projectile.\n" +
                "4. Take turns with your opponent, trying to hit their tank.\n" +
                "5. Watch out for the wind, it will affect your projectile's path.\n" +
                "6. Each tank has 3 lives. The last tank standing wins!");

        JScrollPane scrollPane = new JScrollPane(instructionsArea);
        add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(backAction);
        add(backButton, BorderLayout.SOUTH);
    }
}