import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the "How to Play" screen, which displays instructions to the player.
 */
public class HowToPlayScreen extends JPanel {
    private JButton backButton;
    private JTextArea instructionsArea;

    /**
     * Constructs the HowToPlayScreen with instructions and a back button.
     * @param backAction the ActionListener for the back button
     */
    public HowToPlayScreen(ActionListener backAction) {
        setLayout(new BorderLayout());

        instructionsArea = new JTextArea();
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

    /**
     * Gets the back button for navigation.
     * @return the back button JButton
     */
    public JButton getBackButton() {
        return backButton;
    }

    /**
     * Gets the instructional text currently displayed.
     * @return the instructions text as a String
     */
    public String getInstructionsText() {
        return instructionsArea.getText();
    }

    /**
     * Sets the instructional text to display new instructions.
     * @param text the new instructions to set
     */
    public void setInstructionsText(String text) {
        instructionsArea.setText(text);
    }
}
